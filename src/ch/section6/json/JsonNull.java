package ch.section6.json;

import java.util.ArrayList;
import java.util.List;

public class JsonNull extends JsonValue {

  @Override
  public Type getType() {
    return Type.NULL;
  }
  
  @Override
  public String asString() {
    return "null";
  }
  
  @Override
  public String toString() {
    return "null";
  }

  @Override
  public Number asNumber() {
    return 0.0;
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
    array.add("null");
    return array;
  }

}
