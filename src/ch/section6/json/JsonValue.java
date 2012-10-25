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

import java.util.List;

public abstract class JsonValue {
  
  /** A enumeration of all JSON data types. */
  public enum Type {
    BOOLEAN,
    NUMBER,
    STRING,
    ARRAY,
    MAP,
    NULL
  }
  
  /** A {@link JsonNull} singleton. */
  protected static final JsonNull JSON_NULL = new JsonNull();
  
  public String toString(int indent) {
    if (indent < 0) throw new IllegalArgumentException();
    
    // if there is no indent, use the faster toString() method
    if (indent == 0) return toString();
    
    List<String> tokenList = getTokenList();
    int currentIndent = 0;
    String currentIndentString = "";
    
    StringBuilder sb = new StringBuilder();
    final int len = tokenList.size();
    for (int i = 0; i < len; ++i) {
      String token = tokenList.get(i);
      switch (token.charAt(0)) {
        case '[': case '{': {
          sb.append(token);
          currentIndent += indent;
          currentIndentString = spaces(currentIndent);
          break;
        }
        case ']': case '}': {
          currentIndent -= indent;
          currentIndentString = spaces(currentIndent);
          if (!(tokenList.get(i-1).equals("{") || tokenList.get(i-1).equals("]")))
              sb.append(currentIndentString);
          sb.append(token);
          break;
        }
        case ':': sb.append(": "); break;
        case '\t': sb.append(currentIndentString); break;
        default: sb.append(token); break;
      }
    }
    
    return sb.toString();
  }
  
  protected abstract List<String> getTokenList();
  
  protected String spaces(int x) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < x; i++) sb.append(" ");
    return sb.toString();
  }
  
  public abstract Type getType();

  /** Returns this value as a {@link JsonObject}. */
  public JsonObject asMap() throws JsonCastException {
    throw new JsonCastException("This JsonValue cannot be cast to a JsonObject");
  }
  
  /** Returns this value as a {@link JsonArray}. */
  public JsonArray asArray() throws JsonCastException {
    throw new JsonCastException("This JsonValue cannot be cast to a JsonArray");
  }

  /** Returns this value as a {@link Number}. */
  public Number asNumber() throws JsonCastException {
    throw new JsonCastException("This JsonValue cannot be cast to a Number");
  }
  
  /** Returns this value as a {@link String}. */
  public String asString() throws JsonCastException {
    throw new JsonCastException("This JsonValue cannot be cast to a String");
  }
  
  /** Returns this value as a <code>boolean</code>. */
  public boolean asBoolean() throws JsonCastException {
    throw new JsonCastException("This JsonValue cannot be cast to a boolean");
  }
  
}
