/*
 * Copyright (c) 2012,2013 Martin Roth (mhroth@section6.ch)
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JsonDate extends JsonValue {

	/** An ISO 8601 time formatter. */
	protected static final DateFormat ISO8601_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	private final Date date;

	private final String dateString;

	public JsonDate(Date date) {
		if (date == null) { throw new NullPointerException("Date argument must be non-null."); }
		this.date = date;
		dateString = toIso8601String(date);
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

	// this method is synchronized because there is only one DateFormat object
	/** Convert an ISO-8601-format <code>String</code> into a <code>Date</code>. */
	public static Date asDate(String s) throws ParseException {
		synchronized (ISO8601_DATE_FORMAT) {
			return ISO8601_DATE_FORMAT.parse(s);
		}
	}

	/** Convert a <code>Date</code> into a ISO-8601-format <code>String</code>. */
	public static String toIso8601String(Date date) {
		synchronized (ISO8601_DATE_FORMAT) {
			return ISO8601_DATE_FORMAT.format(date);
		}
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
