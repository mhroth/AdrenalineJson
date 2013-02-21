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

/** A JSON representation of an ordered list of {@link JsonValue}s. */
public final class JsonArray extends JsonValue implements List<JsonValue>, ImmutableJsonArray {
  
  private final ArrayList<JsonValue> list;
  
  public JsonArray() {
    list = new ArrayList<JsonValue>();
  }
  
  public JsonArray(Number[] values) {
    list = new ArrayList<JsonValue>();
    add(values);
  }
  
  public JsonArray(String[] values) {
    list = new ArrayList<JsonValue>();
    add(values);
  }
  
  @Override
  protected void appendTokenList(List<String> tokenList) {
    if (list.isEmpty()) {
      tokenList.add("[");
      tokenList.add("]");
    } else {
      tokenList.add("[");
      tokenList.add("\n");
      tokenList.add("\t");
      final int sizem = list.size()-1;
      for (int i = 0; i < sizem; i++) {
        list.get(i).appendTokenList(tokenList);
        tokenList.add(",");
        tokenList.add("\n");
        tokenList.add("\t");
      }
      list.get(sizem).appendTokenList(tokenList);
      tokenList.add("\n");
      tokenList.add("]");
    }
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
  
  public boolean add(Number[] values) {
    list.ensureCapacity(list.size() + values.length);
    for (Number value : values) add(value);
    return true;
  }
  
  public boolean add(String[] values) {
    list.ensureCapacity(list.size() + values.length);
    for (String value : values) add(value);
    return true;
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
  
  @Override
  public String getString(int index) {
    return list.get(index).asString();
  }
  
  @Override
  public Number getNumber(int index) {
    return list.get(index).asNumber();
  }
  
  @Override
  public boolean getBoolean(int index) {
    return list.get(index).asBoolean();
  }
  
  @Override
  public JsonArray getArray(int index) {
    return list.get(index).asArray();
  }
  
  @Override
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
   * Returns an {@link Iterable} for iterating across {@link JsonArray}s.
   * Elements are cast to {@link JsonArray}s if necessary.
   */
  public Iterable<JsonArray> arrayIteratable() {
    return new Iterable<JsonArray>() {
      @Override
      public Iterator<JsonArray> iterator() {
        return new Iterator<JsonArray>() {
          private int currentIndex = 0;
          
          @Override
          public boolean hasNext() {
            return (currentIndex < list.size());
          }

          @Override
          public JsonArray next() {
            return hasNext() ? list.get(currentIndex++).asArray() : null;
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

  @Override
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
            throw new UnsupportedOperationException();
          }
        };
      }
    };
  }

  @Override
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
          	throw new UnsupportedOperationException();
          }
        };
      }
    };
  }

  @Override
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
          	throw new UnsupportedOperationException();
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
  
  public boolean remove(String s) {
    for (int i = 0; i < list.size(); ++i) {
      if (list.get(i).asString().equals(s)) {
        list.remove(i);
        return true;
      }
    }
    return false;
  }
  
  public boolean remove(Number n) {
    for (int i = 0; i < list.size(); ++i) {
      if (list.get(i).asNumber().equals(n)) {
        list.remove(i);
        return true;
      }
    }
    return false;
  }
  
  public boolean remove(Boolean b) {
    for (int i = 0; i < list.size(); ++i) {
      if (list.get(i).asBoolean() == b.booleanValue()) {
        list.remove(i);
        return true;
      }
    }
    return false;
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
  
  public ImmutableJsonArray asImmutable() {
  	return this;
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
  
  @Override
  public boolean equals(Object o) {
    if (o != null) {
      if (o instanceof JsonArray) {
        JsonArray array = (JsonArray) o;
        if (list.size() == array.size()) {
          for (int i = 0; i < list.size(); ++i) {
            if (!list.get(i).equals(array.get(i))) return false;
          }
        }
        return true;
      }
    }
    return false;
  }
  
  @Override
  // http://www.linuxtopia.org/online_books/programming_books/thinking_in_java/TIJ313_029.htm
  public int hashCode() {
  	int h = 17;
  	for (int i = 0; i < list.size(); ++i) {
      JsonValue v = list.get(i);
      h = 37*h + v.hashCode();
  	}
  	return h;
  }
  
  @Override
  public JsonValue copy() {
    JsonArray array = new JsonArray();
    for (int i = 0; i < list.size(); ++i) {
      array.add(list.get(i).copy());
    }
    return array;
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
