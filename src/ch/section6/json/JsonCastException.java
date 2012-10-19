package ch.section6.json;

/** An exception indicating that a cast between JSON data types is not possible or failed. */
public class JsonCastException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  
  public JsonCastException() {
    super();
  }
  
  public JsonCastException(Exception e) {
    super(e);
  }
  
  public JsonCastException(String message) {
    super(message);
  }

}
