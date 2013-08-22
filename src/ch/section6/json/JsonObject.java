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

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** A JSON representation of a {@link Map}. */
public final class JsonObject extends JsonValue implements Map<String,JsonValue>, ImmutableJsonObject {
  
  private final Map<String,JsonValue> map;
  
  public JsonObject() {
    map = new HashMap<String,JsonValue>();
  }
  
  public JsonObject(String key, JsonValue value) {
    map = new HashMap<String,JsonValue>();
    map.put(key, value);
  }
  
  @Override
  protected void appendTokenList(List<String> tokenList) {
    if (map.isEmpty()) {
      tokenList.add("{");
      tokenList.add("}");
    } else {
      tokenList.add("{");
      tokenList.add("\n");
      tokenList.add("\t");
      for (Map.Entry<String,JsonValue> e : map.entrySet()) {
        tokenList.add(JsonString.jsonEscape(e.getKey()));
        tokenList.add(":");
        e.getValue().appendTokenList(tokenList);
        tokenList.add(",");
        tokenList.add("\n");
        tokenList.add("\t");
      }
      tokenList.remove(tokenList.size()-1); // remove trailing tab
      tokenList.remove(tokenList.size()-2); // remove trailing comma
      tokenList.add("}");
    }
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (Map.Entry<String,JsonValue> e : map.entrySet()) {
    	sb.append(JsonString.jsonEscape(e.getKey()));
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
  	if (map.containsKey(key)) {
  		return map.get(key);
  	} else {
  		throw new UnknownKeyException(key.toString());
  	}
  }
  
  public JsonValue get(Object key, JsonValue def) {
  	if (map.containsKey(key)) {
  		return map.get(key);
  	} else {
  		return (def != null) ? def : JsonValue.JSON_NULL;
  	}
  }
  
  /** Return a value according to its path, e.g. <code>/a/b/0/c</code>. */
  public JsonValue getByPath(String path) {
  	JsonValue value = this;
  	if (path != null && !path.isEmpty()) { // early exist condition
  		for (String p : path.split("/")) {
    		if (p.isEmpty()) continue;
    		switch (value.getType()) {
    			case MAP: value = value.asMap().get(p); break;
    			case ARRAY: value = value.asArray().get(Integer.parseInt(p)); break;
    			default: throw new IllegalArgumentException(String.format("Unknown path: %s", path));
    		}
    	}
  	}
  	return value;
  }

  @Override
  public String getString(String key) throws JsonCastException {
    return get(key).asString();
  }
  
  public String getString(String key, String s) throws JsonCastException {
  	if (containsKey(key)) {
  		return get(key).asString();
  	} else {
  		return s;
  	}
  }
  
  @Override
  public Date getDate(String key) throws JsonCastException {
  	return get(key).asDate();
  }
  
  public Date getDate(String key, Date date) throws JsonCastException {
  	if (map.containsKey(key)) {
  		return get(key).asDate();
  	} else {
  		return date;
  	}
  }
  
  @Override
  public boolean getBoolean(String key) throws JsonCastException {
    return get(key).asBoolean();
  }
  
  /**
   * A convenience function to return the keyed value as a boolean,
   * with a default if the value does not exist.
   */
  public boolean getBoolean(String key, boolean b) throws JsonCastException {
  	if (containsKey(key)) {
  		return get(key).asBoolean();
  	} else {
  		return b;
  	}
  }
  
  @Override
  public JsonObject getObject(String key) throws JsonCastException {
    return get(key).asMap();
  }
  
  @Override
  public JsonArray getArray(String key) throws JsonCastException {
    return get(key).asArray();
  }
  
  @Override
  public Number getNumber(String key) throws JsonCastException {
    return get(key).asNumber();
  }

  public Number getNumber(String key, Number n) throws JsonCastException {
  	if (containsKey(key)) {
  		return get(key).asNumber();
  	} else {
  		return n;
  	}
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
  
  public JsonValue put(String key, Date date) {
  	return map.put(key, new JsonDate(date));
  }

  public JsonValue put(String key, Number value) {
    return map.put(key, new JsonNumber(value));
  }
  
  public JsonValue put(String key, boolean value) {
    return map.put(key, getBoolean(value));
  }
  
  /**
   * Puts an array of <code>Object</code>s into the map, with the array automatically converted to
   * a {@link JsonArray}. The type of the objects is automatically detected and converted into
   * the corresponding JSON type.
   */
  public JsonValue put(String key, Object... values) {
    return map.put(key, new JsonArray(values));
  }

  @Override
  public JsonValue put(String key, JsonValue value) {
    return map.put(key, (value != null) ? value : JSON_NULL);
  }
  
  @Override
  public boolean equals(Object o) {
    if (o != null) {
      if (o instanceof JsonObject) {
        JsonObject obj = (JsonObject) o;
        if (map.size() == obj.size()) {
          for (String key : map.keySet()) {
            if (!map.get(key).equals(obj.get(key))) return false;
          }
          return true;
        }
      }
    }
    return false;
  }
  
  @Override
  public int hashCode() {
  	int keysHash = map.keySet().hashCode();
  	int valuesHash = new HashSet<JsonValue>(map.values()).hashCode();
  	return (keysHash ^ valuesHash);
  }

  @Override
  public JsonValue copy() {
    JsonObject obj = new JsonObject();
    for (Map.Entry<String,JsonValue> e : map.entrySet()) {
      obj.put(e.getKey(), e.getValue().copy());
    }
    return obj;
  }
  
  @Override
  public ImmutableJsonObject asImmutable() {
  	return this;
  }
  
  public static JsonValue parse(String jsonString) throws JsonParseException {
  	if (jsonString == null) {
  		return JsonValue.JSON_NULL;
  	} else if (jsonString.isEmpty()) {
  		return new JsonString("");
  	} else {
  		return parseValue(0, jsonString.length(), jsonString);
  	}
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
      case '"': { // string
        int k = i;
        do {
          k = str.indexOf('"', k+1);
        } while (str.charAt(k-1) == '\\'); // ignore \" escape
        if (k < j) return k+1;
        throw new JsonParseException("No balancing quote found for string.");
      }
      case '{': {
        int m = 1;
        for (int k = i+1; k < j; ++k) {
          char c = str.charAt(k);
          if (c == '}') --m;
          else if (c == '{') ++m;
          if (m == 0) return k+1;
        }
        throw new JsonParseException("No balancing } found dictionary.");
      }
      case '[': {
        int m = 1;
        for (int k = i+1; k < j; ++k) {
          char c = str.charAt(k);
          if (c == ']') --m;
          else if (c == '[') ++m;
          if (m == 0) return k+1;
        }
        throw new JsonParseException("No balancing ] found for array.");
      }
      default: throw new JsonParseException();
    }
  }
  
  private static JsonValue parseValue(int i, int j, String str) throws JsonParseException {
    switch (str.charAt(i)) {
      case 't': {
      	if (str.substring(i, i+4).equals("true")) {
      		return new JsonBoolean(true);
      	} else {
      		throw new JsonParseException();
      	}
      }
      case 'f': {
      	if (str.substring(i, i+5).equals("false")) {
      		return new JsonBoolean(false);
      	} else {
      		throw new JsonParseException();
      	}
      }
      case 'n': {
      	if (str.substring(i, i+4).equals("null")) {
      		return JsonValue.JSON_NULL;
      	} else {
      		throw new JsonParseException();
      	}
      }
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
        try {
					return new JsonDate(substr); // is this string a date?
				} catch (ParseException e) {
					substr = substr.replace("\\\\", "\\"); // '\\' -> '\'
          substr = substr.replace("\\\"", "\""); // '\"' -> '"'
          return new JsonString(substr);
				}
      }
      case '{': {
        JsonObject obj = new JsonObject();
        i = skipWhitespace(i+1, str);
        while (str.charAt(i) != '}') {
          int k = nextValueString(i, j, str);
          JsonValue key = parseValue(i, k, str);
          if (key.getType() != JsonValue.Type.STRING) {
            throw new JsonParseException(
                "Expected a string as a map key. Instead, parsed a " + key.getType() + ".");
          }
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
      default: throw new JsonParseException("Unknown character.");
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
