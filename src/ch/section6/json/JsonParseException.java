package ch.section6.json;

public class JsonParseException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  
  public JsonParseException() {
    super();
  }
  
  public JsonParseException(String message) {
    super(message);
  }
  
  public JsonParseException(Exception e) {
    super(e);
  }

}
