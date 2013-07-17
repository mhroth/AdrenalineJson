package ch.section6.json;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JsonDate extends JsonValue {
	
	/** An ISO 8601 time formatter. */
  protected static final DateFormat ISO8601_DATE_FORMAT =
  		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	
	private final Date date;
	
	private final String dateString;
	
	public JsonDate(Date date) {
		this.date = date;
		dateString = ISO8601_DATE_FORMAT.format(date);
	}
	
	public JsonDate(String dateString) throws ParseException {
		date = asDate(dateString);
		this.dateString = dateString;
	}

	@Override
	protected void appendTokenList(List<String> tokenList) {
		tokenList.add(toString());
	}

	@Override
	public Type getType() {
		return Type.DATE;
	}
	
	@Override
	public Date asDate() {
		return date;
	}
	
	@Override
	public String asString() {
		return dateString;
	}
	
	public static Date asDate(String s) throws ParseException {
		return ISO8601_DATE_FORMAT.parse(s);
	}
	
	@Override
	public String toString() {
		return String.format("\"%s\"", dateString);
	}
	
	@Override
  public boolean equals(Object o) {
    if (o != null) {
      if (o instanceof JsonDate) {
      	JsonDate jsonDate = (JsonDate) o;
        return date.equals(jsonDate.date);
      }
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return date.hashCode();
  }

}
