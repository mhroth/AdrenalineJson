package ch.section6.json;

import java.util.ArrayList;
import java.util.List;

public class JsonString extends JsonValue {

  protected final String string;
  
  public JsonString(String string) {
    this.string = string;
  }

  @Override
  public Type getType() {
    return Type.STRING;
  }
  
  @Override
  public String asString() {
    return string;
  }

  @Override
  public Number asNumber() {
    try {
      return Double.parseDouble(string);
    } catch (NumberFormatException e) {
      throw new JsonCastException(e);
    }
  }

  @Override
  public JSONObject asMap() throws JsonCastException {
    throw new JsonCastException();
  }

  @Override
  public JSONArray asArray() throws JsonCastException {
    throw new JsonCastException();
  }
  
  @Override
  protected List<String> toStringArray() {
    ArrayList<String> array = new ArrayList<String>();
    array.add("\"" + string.replace("\"", "\\\"") + "\"");
    return array;
  }
  
  @Override
  public String toString() {
    return "\"" + string.replace("\"", "\\\"") + "\"";
  }

}
