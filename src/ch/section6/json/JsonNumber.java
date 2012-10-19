package ch.section6.json;

import java.util.ArrayList;
import java.util.List;

public class JsonNumber extends JsonValue {

  private final double number;
  
  public JsonNumber(Number number) {
    this.number = number.doubleValue();
  }
  
  protected JsonNumber(String str) throws NumberFormatException {
    number = Double.parseDouble(str);
  }

  @Override
  public Type getType() {
    return Type.FLOAT;
  }
  
  @Override
  public String asString() {
    return Double.toString(number);
  }

  @Override
  public Number asNumber() {
    return number;
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
    array.add(Double.toString(number));
    return array;
  }
  
  @Override
  public String toString() {
    long num = (long) number;
    return ((double) num == number) ? Long.toString(num) : Double.toString(number);
  }
  
  public double doubleValue() {
    return number;
  }
  
  public int intValue() {
    return (int) number;
  }

}
