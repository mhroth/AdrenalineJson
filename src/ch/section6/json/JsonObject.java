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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonObject extends JsonValue implements Map<String,JsonValue> {
  
  private final Map<String,JsonValue> map;
  
  public JsonObject() {
    map = new HashMap<String,JsonValue>();
  }
  
  /**
   * Construct a JSON object from a Java Object by reflection. This is a very simple implementation
   * which only records publicly accessible fields.
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException
   */
  public JsonObject(Object o) throws IllegalArgumentException, IllegalAccessException {
    map = new HashMap<String,JsonValue>();
    for (Field field : o.getClass().getDeclaredFields()) {
      if (field.isAccessible()) {
        if (Number.class.isAssignableFrom(field.getDeclaringClass())) {
          map.put(field.getName(), new JsonNumber((Number) field.get(o)));
        } else if (String.class.isAssignableFrom(field.getDeclaringClass())) {
          map.put(field.getName(), new JsonString((String) field.get(o)));
        } else if (Boolean.class.isAssignableFrom(field.getDeclaringClass())) {
          map.put(field.getName(), new JsonBoolean((Boolean) field.get(o)));
        } else {
          map.put(field.getName(), new JsonObject(field.get(o)));
        }
      }
    }
  }
  
  public JsonObject(Map<String,Object> m) {
    map = new HashMap<String,JsonValue>();
    for (Map.Entry<String,Object> e : m.entrySet()) {
      if (Number.class.isAssignableFrom(e.getValue().getClass().getDeclaringClass())) {
        map.put(e.getKey().toString(), new JsonNumber((Number) e.getValue()));
      } else if (String.class.isAssignableFrom(e.getValue().getClass().getDeclaringClass())) {
        map.put(e.getKey().toString(), new JsonString((String) e.getValue()));
      } else if (Boolean.class.isAssignableFrom(e.getValue().getClass().getDeclaringClass())) {
        map.put(e.getKey().toString(), new JsonBoolean((Boolean) e.getValue()));
      } else {
//        map.put(e.getKey().toString(), new JSONObject(e.getValue()));
      }
    }
  }
  
  @Override
  protected List<String> toStringArray() {
    ArrayList<String> array = new ArrayList<String>();
    if (map.isEmpty()) {
      array.add("{ }");
    } else {
      array.add("{");
      array.add("\n");
      for (Map.Entry<String,JsonValue> e : map.entrySet()) {
        array.add("\"" + e.getKey() + "\"");
        array.add(":");
        array.addAll(e.getValue().toStringArray());
        array.add(",");
        array.add("\n");
      }
      array.remove(array.size()-2); // remove trailing comma
      array.add("}");
    }
    return array;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (Map.Entry<String,JsonValue> e : map.entrySet()) {
      sb.append("\"").append(e.getKey()).append("\"");
      sb.append(":");
      sb.append(e.getValue().toString());
      sb.append(",");
    }
    if (!map.isEmpty()) sb.deleteCharAt(sb.length()-1); // clear trailing comma
    sb.append("}");
    return sb.toString();
  }

  @Override
  public Type getType() {
    return Type.MAP;
  }
  
  @Override
  public JsonObject asMap() {
    return this;
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  @Override
  public Set<java.util.Map.Entry<String, JsonValue>> entrySet() {
    return map.entrySet();
  }

  @Override
  public JsonValue get(Object key) {
    return map.get(key);
  }
  
  /** A convenience function to return the keyed value as a {@link String}. */
  public String getString(String key) throws JsonCastException {
    return map.get(key).asString();
  }
  
  /** A convenience function to return the keyed value as a {@link Boolean}. */
  public boolean getBoolean(String key) throws JsonCastException {
    return map.get(key).asBoolean();
  }
  
  /** A convenience function to return the keyed value as a {@link JsonObject}. */
  public JsonObject getObject(String key) throws JsonCastException {
    return map.get(key).asMap();
  }
  
  /** A convenience function to return the keyed value as a {@link JsonArray}. */
  public JsonArray getArray(String key) throws JsonCastException {
    return map.get(key).asArray();
  }
  
  /** A convenience function to return the keyed value as a {@link Number}. */
  public Number getNumber(String key) throws JsonCastException {
    return map.get(key).asNumber();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public Set<String> keySet() {
    return map.keySet();
  }

  @Override
  public void putAll(Map<? extends String, ? extends JsonValue> m) {
    map.putAll(m);    
  }

  @Override
  public JsonValue remove(Object key) {
    return map.remove(key);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public Collection<JsonValue> values() {
    return map.values();
  }
  
  public JsonValue put(String key, String value) {
    return map.put(key, new JsonString(value));
  }

  public JsonValue put(String key, Number value) {
    return map.put(key, new JsonNumber(value));
  }
  
  public JsonValue put(String key, Boolean value) {
    return map.put(key, new JsonBoolean(value));
  }
  
  public JsonValue put(String key, Object o) throws IllegalArgumentException, IllegalAccessException {
    return map.put(key, new JsonObject(o));
  }

  @Override
  public JsonValue put(String key, JsonValue value) {
    return map.put(key, value);
  }
  
  public static JsonValue parse(String jsonString) throws JsonParseException {
    IndexValuePair pair = new JsonObject().new IndexValuePair();
    return JsonObject.parsePartial(0, jsonString, pair).value;
  }
  
  /**
   * 
   * @param i
   * @param jsonString
   * @param pair
   * @return  <code>IndexValuePair.index</code> contains the index of the character after the parsed <code>JsonValue</code>.
   * @throws JsonParseException
   */
  private static IndexValuePair parsePartial(int i, String jsonString, IndexValuePair pair) throws JsonParseException {
    switch (jsonString.charAt(i)) {
      case ' ': case '\n': case '\t': case '\r': {
        return parsePartial(i+1, jsonString, pair); // skip whitespace
      }
      case '{': {
        JsonObject obj = new JsonObject();
        while (jsonString.charAt(i++) != '}') {
          String key = parsePartial(i, jsonString, pair).value.asString();
          i = skipWhitespace(pair.index, jsonString);
          if (jsonString.charAt(i) != ':') throw new JsonParseException(
              "Expected ':' in dictionary description. Found '" + jsonString.charAt(i) + "");
          i = skipWhitespace(i+1, jsonString);
          JsonValue value = parsePartial(i, jsonString, pair).value;
          obj.put(key, value);
          i = skipWhitespace(pair.index, jsonString);
          if (jsonString.charAt(i) == ',') {
            i = skipWhitespace(i+1, jsonString);
          }
        }
        pair.value = obj;
        pair.index = skipWhitespace(i+1, jsonString);
        return pair;
      }
      case '[': {
        JsonArray array = new JsonArray();
        while (jsonString.charAt(i) != ']') {
          pair = parsePartial(i+1, jsonString, pair); // skip ']' or ','
          array.add(pair.value);
          i = pair.index;
        }
        pair.value = array;
        pair.index += 1; // advance past the trailing ']'
        return pair;
      }
      case '"': {
        int j = i;
        do {
          j = jsonString.indexOf('"', j+1);
        } while (jsonString.charAt(j-1) == '\\'); // ignore \" escape
        pair.value = new JsonString(jsonString.substring(i+1, j));
        pair.index = j+1;
        return pair;
      }
      case 't': {
        pair.value = new JsonBoolean(true);
        pair.index = i + 4;
        return pair;
      }
      case 'f': {
        pair.value = new JsonBoolean(false);
        pair.index = i + 5;
        return pair;
      }
      case 'n': {
        pair.value = new JsonNull();
        pair.index = i + 4;
        return pair;
      }
      case '-':
      case '0': case '1': case '2': case '3': case '4':
      case '5': case '6': case '7': case '8': case '9': {
        int k = jsonString.length();
        int j = i + 1;
        for (; j < k; j++) {
          char c = jsonString.charAt(j);
          if (c == ']' || c == '}' || c == ',') break;
        }
        try {
          pair.value = new JsonNumber(jsonString.substring(i, j));
          pair.index = j;
          return pair;
        } catch (NumberFormatException e) {
          throw new JsonParseException(e);
        }
      }
      default: throw new JsonParseException(
          "Unknown character near index " + i + ": \"" + jsonString + "\"");
      }
  }
  
  private static int skipWhitespace(int i, String str) {
    while (
        i < str.length() &&
        (str.charAt(i) == ' ' || str.charAt(i) == '\n' ||
        str.charAt(i) == '\r' || str.charAt(i) == '\t')) {
      ++i;
    }
    return i;
  }

  public static void main(String[] args) {
    JsonObject jsonObj = new JsonObject();
    jsonObj.put("hello", new JsonString("world"));
    String jsonString = jsonObj.toString(0);
    System.out.println(jsonString);
    
    try {
      JsonValue value = JsonObject.parse(jsonString);
      System.out.println(value.toString(2));
    } catch (JsonParseException e) {
      e.printStackTrace();
    }
    
    try {
      JsonObject jsonObj2 = new JsonObject("jo");
      System.out.println(jsonObj2.toString(2));
    } catch (IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
    
    String str = "{\n  \"hello\" : \"world\",\n  \"good\"  :  \"bad\"\n}";
    JsonObject jsonObj3 = JsonObject.parse(str).asMap();
    System.out.println(jsonObj3);
  }
  
  private class IndexValuePair {
    public int index;
    public JsonValue value;
    
    public IndexValuePair() {
      index = 0;
      value = null;
    }
  }
}
