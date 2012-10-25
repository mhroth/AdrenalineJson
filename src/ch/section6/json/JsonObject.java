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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonObject extends JsonValue implements Map<String,JsonValue> {
  
  private final Map<String,JsonValue> map;
  
  public JsonObject() {
    map = new HashMap<String,JsonValue>();
  }
  
  @Override
  protected List<String> getTokenList() {
    ArrayList<String> array = new ArrayList<String>();
    if (map.isEmpty()) {
      array.add("{");
      array.add("}");
    } else {
      array.add("{");
      array.add("\n");
      array.add("\t");
      for (Map.Entry<String,JsonValue> e : map.entrySet()) {
        array.add("\"" + e.getKey() + "\"");
        array.add(":");
        array.addAll(e.getValue().getTokenList());
        array.add(",");
        array.add("\n");
        array.add("\t");
      }
      array.remove(array.size()-1); // remove trailing tab
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

  @Override
  public JsonValue put(String key, JsonValue value) {
    return map.put(key, value);
  }
  
  public static JsonValue parse(String jsonString) throws JsonParseException {
    return parseValue(0, jsonString.length(), jsonString);
  }
  
  private static int nextValueString(int i, final int j, String str) throws JsonParseException {
    switch (str.charAt(i)) {
      case 't': return i + 4; // true
      case 'f': return i + 5; // false
      case 'n': return i + 4; // null
      case '-':               // number
      case '0': case '1': case '2': case '3': case '4':
      case '5': case '6': case '7': case '8': case '9': {
        int k = i + 1;
        for (; k < j; ++k) {
          char c = str.charAt(k);
          if (c == ']' || c == '}' || c == ',') break;
        }
        return k;
      }
      case '"': {             // string
        int k = i;
        do {
          k = str.indexOf('"', k+1);
        } while (str.charAt(k-1) == '\\'); // ignore \" escape
        if (k < j) return k+1;
        throw new JsonParseException();
      }
      case '{': {
        int m = 1;
        for (int k = i+1; k < j; ++k) {
          char c = str.charAt(k);
          if (c == '}') --m;
          else if (c == '{') ++m;
          if (m == 0) return k+1;
        }
        throw new JsonParseException();
      }
      case '[': {
        int m = 1;
        for (int k = i+1; k < j; ++k) {
          char c = str.charAt(k);
          if (c == ']') --m;
          else if (c == '[') ++m;
          if (m == 0) return k+1;
        }
        throw new JsonParseException();
      }
      default: throw new JsonParseException();
    }
  }
  
  private static JsonValue parseValue(int i, int j, String str) throws JsonParseException {
    switch (str.charAt(i)) {
      case 't': return new JsonBoolean(true);
      case 'f': return new JsonBoolean(false);
      case 'n': return JsonValue.JSON_NULL;
      case '-':
      case '0': case '1': case '2': case '3': case '4':
      case '5': case '6': case '7': case '8': case '9': {
        try {
          return new JsonNumber(str.substring(i, j));
        } catch (NumberFormatException e) {
          throw new JsonParseException(e);
        }
      }
      case '"': {
        String substr = str.substring(i+1, j-1);
        return new JsonString(substr.replace("\\\"", "\""));
      }
      case '{': {
        JsonObject obj = new JsonObject();
        i = skipWhitespace(i+1, str);
        while (str.charAt(i) != '}') {
          int k = nextValueString(i, j, str);
          JsonValue key = parseValue(i, k, str);
          if (key.getType() != JsonValue.Type.STRING)
            throw new JsonParseException(
                "Expected a string as a map key. Instead, parsed a " + key.getType() + ".");
          i = skipWhitespace(k, str);
          if (str.charAt(i) != ':') throw new JsonParseException();
          i = skipWhitespace(i+1, str);
          k = nextValueString(i, j, str);
          JsonValue value = parseValue(i, k, str);
          i = skipWhitespace(k, str);
          if (str.charAt(i) == ',') i = skipWhitespace(i+1, str);
          
          obj.put(key.asString(), value);
        }
        return obj;
      }
      case '[': {
        JsonArray array = new JsonArray();
        i = skipWhitespace(i+1, str);
        while (str.charAt(i) != ']') {
          int k = nextValueString(i, j, str);
          JsonValue value = parseValue(i, k, str);
          array.add(value);
          i = skipWhitespace(k, str);
          if (str.charAt(i) == ',') i = skipWhitespace(i+1, str);
        }
        return array;
      }
      default: throw new JsonParseException();
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
}
