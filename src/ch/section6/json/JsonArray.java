/*
 * Copyright (c) 2012, Martin Roth (mhroth@section6.ch)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the AdrenalineJson nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.section6.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class JsonArray extends JsonValue implements List<JsonValue> {
  
  private final ArrayList<JsonValue> list;
  
  public JsonArray() {
    list = new ArrayList<JsonValue>();
  }
  
  protected List<String> getTokenList() {
    ArrayList<String> strArray = new ArrayList<String>(2*list.size()+1); // minimum size
    
    switch (list.size()) {
      case 0: {
        strArray.add("[");
        strArray.add("]");
        break;
      }
      case 1: {
        strArray.add("[");
        strArray.addAll(list.get(0).getTokenList());
        strArray.add("]");
        break;
      }
      default: {
        strArray.add("[");
        strArray.add("\n");
        strArray.add("\t");
        final int sizem = list.size()-1;
        for (int i = 0; i < sizem; i++) {
          JsonValue value = list.get(i);
          List<String> strings = value.getTokenList();
          strArray.addAll(strings);
          strArray.add(",");
          strArray.add("\n");
          strArray.add("\t");
        }
        strArray.addAll(list.get(sizem).getTokenList());
        strArray.add("\n");
        strArray.add("]");
      }
    }
    
    return strArray;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    final int sizem = list.size()-1;
    for (int i = 0; i < sizem; ++i) {
      JsonValue value = list.get(i);
      sb.append(value.toString());
      sb.append(",");
    }
    if (!list.isEmpty()) sb.append(list.get(sizem).toString());
    sb.append("]");
    return sb.toString();
  }

  @Override
  public Type getType() {
    return Type.ARRAY;
  }
  
  @Override
  public JsonArray asArray() {
    return this;
  }

  @Override
  public boolean add(JsonValue e) {
    return list.add(e);
  }
  
  public boolean add(String string) {
    return list.add(new JsonString(string));
  }
  
  public boolean add(Number number) {
    return list.add(new JsonNumber(number));
  }
  
  public boolean add(Boolean bool) {
    return list.add(new JsonBoolean(bool));
  }

  @Override
  public void add(int index, JsonValue element) {
    if (index >= list.size()) {
      int additionalElements = index - list.size() + 1;
      for (int i = 0; i < additionalElements; i++) {
        list.add(JsonValue.JSON_NULL);
      }
    }
    list.add(index, element);
  }

  @Override
  public boolean addAll(Collection<? extends JsonValue> c) {
    return list.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends JsonValue> c) {
    return list.addAll(index, c);
  }

  @Override
  public void clear() {
    list.clear();
  }

  @Override
  public boolean contains(Object o) {
    return list.contains(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }

  @Override
  public JsonValue get(int index) {
    return list.get(index);
  }
  
  public String getString(int index) {
    return list.get(index).asString();
  }
  
  public Number getNumber(int index) {
    return list.get(index).asNumber();
  }
  
  public boolean getBoolean(int index) {
    return list.get(index).asBoolean();
  }
  
  public JsonArray getArray(int index) {
    return list.get(index).asArray();
  }
  
  public JsonObject getObject(int index) {
    return list.get(index).asMap();
  }
  
  /** Always returns a {@link JsonValue} object. <code>get()</code> may return <code>null</code>. */
  public JsonValue getValue(int index) {
    JsonValue value = list.get(index);
    return (value == null) ? JSON_NULL : value;
  }

  @Override
  public int indexOf(Object o) {
    return list.indexOf(o);
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public Iterator<JsonValue> iterator() {
    return list.iterator();
  }
  
  /**
   * Returns an {@link Iterable} for iterating across {@link JsonObject}s.
   * Elements are cast to {@link JsonObject}s if necessary.
   */
  public Iterable<JsonObject> mapIteratable() {
    return new Iterable<JsonObject>() {
      @Override
      public Iterator<JsonObject> iterator() {
        return new Iterator<JsonObject>() {
          private int currentIndex = 0;
          
          @Override
          public boolean hasNext() {
            return (currentIndex < list.size());
          }

          @Override
          public JsonObject next() {
            return hasNext() ? list.get(currentIndex++).asMap() : null;
          }

          @Override
          public void remove() {
            list.remove(currentIndex);
          }
        };
      }
    };
  }
  
  /**
   * Returns an {@link Iterable} for iterating across {@link Strings}.
   * Elements are cast to strings if necessary.
   */
  public Iterable<String> stringIterable() {
    return new Iterable<String>() {
      @Override
      public Iterator<String> iterator() {
        return new Iterator<String>() {
          private int currentIndex = 0;
          
          @Override
          public boolean hasNext() {
            return (currentIndex < list.size());
          }

          @Override
          public String next() {
            return hasNext() ? list.get(currentIndex++).asString() : null;
          }

          @Override
          public void remove() {
            list.remove(currentIndex);
          }
        };
      }
    };
  }
  
  /**
   * Returns an {@link Iterable} for iterating across {@link Number}s.
   * Elements are cast to numbers if necessary.
   */
  public Iterable<Number> numberIterable() {
    return new Iterable<Number>() {
      @Override
      public Iterator<Number> iterator() {
        return new Iterator<Number>() {
          private int currentIndex = 0;
          
          @Override
          public boolean hasNext() {
            return (currentIndex < list.size());
          }

          @Override
          public Number next() {
            return hasNext() ? list.get(currentIndex++).asNumber() : null;
          }

          @Override
          public void remove() {
            list.remove(currentIndex);
          }
        };
      }
    };
  }
  
  /**
   * Returns an {@link Iterable} for iterating across {@link Boolean}s.
   * Elements are cast to booleans if necessary.
   */
  public Iterable<Boolean> booleanIterable() {
    return new Iterable<Boolean>() {
      @Override
      public Iterator<Boolean> iterator() {
        return new Iterator<Boolean>() {
          private int currentIndex = 0;
          
          @Override
          public boolean hasNext() {
            return (currentIndex < list.size());
          }

          @Override
          public Boolean next() {
            return hasNext() ? list.get(currentIndex++).asBoolean() : null;
          }

          @Override
          public void remove() {
            list.remove(currentIndex);
          }
        };
      }
    };
  }
  
  @Override
  public int lastIndexOf(Object o) {
    return list.lastIndexOf(o);
  }

  @Override
  public ListIterator<JsonValue> listIterator() {
    return list.listIterator();
  }

  @Override
  public ListIterator<JsonValue> listIterator(int index) {
    return list.listIterator(index);
  }

  @Override
  public boolean remove(Object o) {
    return list.remove(o);
  }

  @Override
  public JsonValue remove(int index) {
    return list.remove(index);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return list.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return list.retainAll(c);
  }

  @Override
  public JsonValue set(int index, JsonValue element) {
    return list.set(index, element);
  }
  
  public JsonValue set(int index, String string) {
    return list.set(index, new JsonString(string));
  }
  
  public JsonValue set(int index, Number number) {
    return list.set(index, new JsonNumber(number));
  }
  
  public JsonValue set(int index, Boolean bool) {
    return list.set(index, new JsonBoolean(bool));
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public List<JsonValue> subList(int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }

  @Override
  public Object[] toArray() {
    return list.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return list.toArray(a);
  }
  
  public static void main(String[] args) {
    JsonArray array = new JsonArray();
    array.add(new JsonNull());
    array.add(new JsonNumber(10.5));
    array.add(new JsonString("hello"));
    
    JsonObject obj = new JsonObject();
    obj.put("hello", "world");
    array.add(obj);
    
    System.out.println(array.toString(2));
  }
  
}
