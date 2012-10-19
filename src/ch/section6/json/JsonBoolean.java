package ch.section6.json;

import java.util.ArrayList;
import java.util.List;

public class JsonBoolean extends JsonValue {

  private final boolean bool;
  
  public JsonBoolean(boolean bool) {
    this.bool = bool;
  }
  
  @Override
  protected List<String> toStringArray() {
    ArrayList<String> array = new ArrayList<String>();
    array.add(bool ? "true" : "false");
    return array;
  }
  
  @Override
  public String toString() {
    return bool ? "true" : "false";
  }

  @Override
  public Type getType() {
    return Type.BOOLEAN;
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
  public Number asNumber() throws JsonCastException {
    return bool ? 1.0 : 0.0;
  }

  @Override
  public String asString() throws JsonCastException {
    return bool ? "true" : "false";
  }
  
  public boolean value() {
    return bool;
  }

}
