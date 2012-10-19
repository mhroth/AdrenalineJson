package ch.section6.json;

import java.util.List;

public abstract class JsonValue {
  
  /** A enumeration of all JSON data types. */
  public enum Type {
    BOOLEAN,
    FLOAT,
    STRING,
    ARRAY,
    MAP,
    NULL
  }
  
  public String toString(int indent) {
    // if there is no indent, use the faster toString() method
    if (indent == 0) return toString();
    
    List<String> strings = toStringArray();
    int currentIndent = 0;
    String currentIndentString = "";
    
    StringBuilder sb = new StringBuilder();
    for (String string : strings) {
      switch (string.charAt(0)) {
        case '[': case '{': {
          sb.append(string);
          currentIndent += indent;
          currentIndentString = spaces(currentIndent);
          break;
        }
        case ']': case '}': {
          sb.insert(sb.length()-indent, string);
          currentIndent -= indent;
          currentIndentString = spaces(currentIndent);
          break;
        }
        case ':': {
          sb.append(": ");
          break;
        }
        case '\n': {
          sb.append(string);
          sb.append(currentIndentString);
          break;
        }
        default: {
          sb.append(string);
          break;
        }
      }
    }
    
    return sb.toString();
  }
  
  protected abstract List<String> toStringArray();
  
  protected String spaces(int x) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < x; i++) sb.append(" ");
    return sb.toString();
  }
  
  public abstract Type getType();

  /** Returns this value as a {@link JSONObject}. */
  public abstract JSONObject asMap() throws JsonCastException;
  
  /** Returns this value as a {@link JSONArray}. */
  public abstract JSONArray asArray() throws JsonCastException;

  /** Returns this value as a {@link Number}. */
  public abstract Number asNumber() throws JsonCastException;
  
  /** Returns this value as a {@link String}. */
  public abstract String asString() throws JsonCastException;
  
}
