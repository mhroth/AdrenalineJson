package ch.section6.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class JSONArray extends JsonValue implements List<JsonValue> {
  
  private final ArrayList<JsonValue> list;
  
  public JSONArray() {
    list = new ArrayList<JsonValue>();
  }
  
  protected List<String> toStringArray() {
    ArrayList<String> strArray = new ArrayList<String>(2*list.size()+1); // minimum size
    
    switch (list.size()) {
      case 0: {
        strArray.add("[]");
        break;
      }
      case 1: {
        strArray.add("[");
        strArray.addAll(list.get(0).toStringArray());
        strArray.add("]");
        break;
      }
      default: {
        strArray.add("[");
        strArray.add("\n");
        final int sizem = list.size()-1;
        for (int i = 0; i < sizem; i++) {
          JsonValue value = list.get(i);
          List<String> strings = value.toStringArray();
          strArray.addAll(strings);
          strArray.add(",");
          strArray.add("\n");
        }
        strArray.addAll(list.get(sizem).toStringArray());
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
  public JSONArray asArray() {
    return this;
  }
  
  @Override
  public JSONObject asMap() throws JsonCastException {
    throw new JsonCastException();
  }

  @Override
  public Number asNumber() throws JsonCastException {
    throw new JsonCastException();
  }

  @Override
  public String asString() throws JsonCastException {
    throw new JsonCastException();
  }

  @Override
  public boolean add(JsonValue e) {
    return list.add(e);
  }

  @Override
  public void add(int index, JsonValue element) {
    if (index >= list.size()) {
      int additionalElements = index - list.size();
      for (int i = 0; i < additionalElements; i++) {
        list.add(null);
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
  public int indexOf(Object o) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public Iterator<JsonValue> iterator() {
    return list.iterator();
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
    JSONArray array = new JSONArray();
    array.add(new JsonNull());
    array.add(new JsonNumber(10.5));
    array.add(new JsonString("hello"));
    
    JSONObject obj = new JSONObject();
    obj.put("hello", "world");
    array.add(obj);
    
    System.out.println(array.toString(2));
  }
  
}
