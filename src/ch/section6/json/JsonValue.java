/*
 * Copyright (c) 2012,2013, Martin Roth (mhroth@section6.ch)
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** An abstract superclass of all JSON values. */
public abstract class JsonValue implements Cloneable {

	/** A enumeration of all JSON data types. */
	public enum Type {
		BOOLEAN, NUMBER, STRING, DATE, ARRAY, MAP, NULL
	}

	/** A {@link JsonNull} singleton. */
	protected static final JsonNull JSON_NULL = new JsonNull();

	/** A <code>true</code> <code>JsonBoolean</code> singleton. */
	protected static final JsonBoolean JSON_TRUE = new JsonBoolean(true);

	/** A <code>false</code> <code>JsonBoolean</code> singleton. */
	protected static final JsonBoolean JSON_FALSE = new JsonBoolean(false);

	/** Returns a pretty-printed <code>String</code> of this value with the given indent. */
	public String toString(int indent) {
		if (indent < 0) {
			throw new IllegalArgumentException("Indent argument may not be negative.");
		}

		// if there is no indent, use the faster toString() method
		if (indent == 0) {
			return toString();
		}

		List<String> tokenList = new ArrayList<String>();
		appendTokenList(tokenList);
		int currentIndent = 0;
		String currentIndentString = "";

		StringBuilder sb = new StringBuilder();
		final int len = tokenList.size();
		for (int i = 0; i < len; ++i) {
			String token = tokenList.get(i);
			switch (token.charAt(0)) {
				case '[':
				case '{': {
					sb.append(token);
					currentIndent += indent;
					currentIndentString = spaces(currentIndent);
					break;
				}
				case ']':
				case '}': {
					currentIndent -= indent;
					currentIndentString = spaces(currentIndent);
					if (!(tokenList.get(i-1).equals("{") || tokenList.get(i-1).equals("[")))
						sb.append(currentIndentString);
					sb.append(token);
					break;
				}
				case ':':
					sb.append(": ");
					break;
				case '\t':
					sb.append(currentIndentString);
					break;
				default:
					sb.append(token);
					break;
			}
		}

		return sb.toString();
	}

	/** Appends all string tokens for this object to the given list. */
	protected abstract void appendTokenList(List<String> tokenList);

	/** Returns a <code>String</code> with the given number of spaces. */
	private String spaces(int x) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < x; i++)
			sb.append(" ");
		return sb.toString();
	}

	/** Returns a static reference to JSON <code>null</code>. */
	public static JsonNull getNull() {
		return JSON_NULL;
	}

	/**
	 * Returns a static reference to JSON <code>true</code> or <code>false</code>.
	 */
	public static JsonBoolean getBoolean(Boolean b) {
		return b ? JSON_TRUE : JSON_FALSE;
	}

	/** Returns a deep copy of this value. */
	public JsonValue copy() {
		return this;
	}

	/**
	 * This method is similar to <code>copy()</code> method but complies with the
	 * <code>Cloneable</code> interface. A deep copy of this value is returned.
	 */
	@Override
	public Object clone() {
		return copy();
	}

	/** Returns the JSON type of this value. */
	public abstract Type getType();

	/** Returns this value as a {@link JsonObject}. */
	public JsonObject asMap() throws JsonCastException {
		throw new JsonCastException(String.format("JsonValue %s cannot be cast to a JsonObject.",
				toString()));
	}

	/** Returns this value as a {@link JsonArray}. */
	public JsonArray asArray() throws JsonCastException {
		throw new JsonCastException(String.format("JsonValue %s cannot be cast to a JsonArray.",
				toString()));
	}

	/** Returns this value as a {@link Number}. */
	public Number asNumber() throws JsonCastException {
		throw new JsonCastException(String.format("JsonValue %s cannot be cast to a Number.",
				toString()));
	}

	/** Returns this value as a {@link String}. */
	public String asString() throws JsonCastException {
		throw new JsonCastException(String.format("JsonValue %s cannot be cast to a string.",
				toString()));
	}

	/** Returns this value as a <code>boolean</code>. */
	public boolean asBoolean() throws JsonCastException {
		throw new JsonCastException(String.format("JsonValue %s cannot be cast to a boolean.",
				toString()));
	}

	/** Returns this value as a <code>Date</code>. */
	public Date asDate() throws JsonCastException {
		throw new JsonCastException(String.format("JsonValue %s cannot be cast to a date.",
				toString()));
	}
	
	/**
	 * Casts a generic <code>Object</code> to a <code>JsonValue</code>, if possible. Converts a
	 * <code>null</code> object to <code>JsonNull</code>.
	 * 
	 * @throws JsonCastException
	 *           If the cast is not possible.
	 */
	protected static JsonValue objectToJsonValue(Object o) {
		if (o == null) {
			return JSON_NULL;
		} else if (o instanceof JsonValue) {
			return (JsonValue) o;
		} else  if (o instanceof String) {
			return new JsonString((String) o);
		} else if (o instanceof Number) {
			return new JsonNumber((Number) o);
		} else if (o instanceof Boolean) {
			return JsonValue.getBoolean((Boolean) o);
		} else if (o instanceof Date) {
			return new JsonDate((Date) o);
		} else {
			throw new JsonCastException(
					String.format("Uncastable object type: %s", o.getClass().getCanonicalName()));
		}
	}
	
	/**
	 * Return a value according to its path, e.g. <code>/a/b/0/c</code>.
	 * 
	 * @throws IllegalArgumentException
	 *             If no value could be found at that path.
	 * @throws NumberFormatException
	 *             If an array index is expected but the next path address is
	 *             not an integer.
	 */
	protected JsonValue getByPath(String path) throws IllegalArgumentException, NumberFormatException {
		JsonValue value = this;
		if (path != null && !path.isEmpty()) { // early exit condition
			for (String p : path.split("/")) {
				if (p.isEmpty()) continue; // ignore ""
				switch (value.getType()) {
					case MAP: value = value.asMap().get(p); break;
					case ARRAY: value = value.asArray().get(Integer.parseInt(p)); break;
					default: {
						throw new IllegalArgumentException(
								String.format("Unknown path: %s", path));
					}
				}
			}
		}
		return value;
	}
	
	/**
	 * Set a value according to its path.
	 * 
	 * @param path
	 *          A non-null string of the format <code>/a/b/0/c</code>.
	 * @param newValue
	 *          The new value.
	 * @return <code>true</code> if successful. <code>false</code> otherwise.
	 */
	protected boolean setByPath(String path, Object o)
			throws IllegalArgumentException, NumberFormatException {
		if (path != null && !path.isEmpty()) {
			if (path.endsWith("/")) {
				throw new IllegalArgumentException(
						String.format("Path %s cannot end with trailing \"/\".", path));
			}
			
			JsonValue newValue = objectToJsonValue(o);
			
			int lastSlash = path.lastIndexOf("/");
			String lastPath = path.substring(lastSlash+1);

			JsonValue value = getByPath(path.substring(0, lastSlash));
			switch (value.getType()) {
				case MAP: value.asMap().put(lastPath, newValue); break;
				case ARRAY: value.asArray().set(Integer.parseInt(lastPath), newValue); break;
				default: throw new IllegalArgumentException(String.format("Unknown path: %s", path));
			}
			
			return true;
		} else {
			return false;
		}
	}

	/**
	 * A convenience method to Save this value to file with the given indent, assuming a UTF-8
	 * character set.
	 * 
	 * @throws IOException
	 *             If something goes wrong!
	 */
	public void saveToFile(File file, int indent) throws IOException {
		saveToFile(file, indent, "UTF-8");
	}

	/**
	 * A convenience method to save this value to file with the given indent and the character set
	 * name.
	 * 
	 * @throws IOException
	 *             If something goes wrong!
	 */
	public void saveToFile(File file, int indent, String charsetName) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		String jsonString = this.toString(indent);
		byte[] data = jsonString.getBytes(charsetName);
		out.write(data);
		out.flush();
		out.close();
	}

	/**
	 * A convenience method to load a value from file assuming a UTF-8 character set.
	 * 
	 * @throws FileNotFoundException
	 *             If the file could not be found.
	 * @throws IOException
	 *             If the file could not be read.
	 * @throws JsonParseException
	 *             If the file could not be parsed into valid JSON.
	 */
	public static JsonValue loadFromFile(File file) throws IOException {
		return loadFromFile(file, "UTF-8");
	}

	/**
	 * A convenience method to load a value from file with the given character set name.
	 * 
	 * @throws FileNotFoundException
	 *             If the file could not be found.
	 * @throws IOException
	 *             If the file could not be read.
	 * @throws JsonParseException
	 *             If the file could not be parsed into valid JSON.
	 */
	public static JsonValue loadFromFile(File file, String charsetName)
			throws FileNotFoundException, IOException, JsonParseException {
		return loadFromStream(new BufferedInputStream(new FileInputStream(file)), charsetName);
	}

	/**
	 * A convenience method to load a value from an input stream assuming a UTF-8 character set.
	 * 
	 * @throws IOException
	 *             If the input stream could not be read.
	 * @throws JsonParseException
	 *             If the file could not be parsed into valid JSON.
	 */
	public static JsonValue loadFromStream(InputStream in) throws IOException, JsonParseException {
		return loadFromStream(in, "UTF-8");
	}

	/**
	 * A convenience method to load a value from an input stream with the given character set name.
	 * 
	 * @throws IOException
	 *             If the input stream could not be read.
	 * @throws JsonParseException
	 *             If the file could not be parsed into valid JSON.
	 */
	public static JsonValue loadFromStream(InputStream in, String charsetName) throws IOException,
			JsonParseException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(in.available());
		byte[] buffer = new byte[1024]; // a 1KB buffer

		int len = in.read(buffer);
		while (len >= 0) {
			baos.write(buffer, 0, len);
			len = in.read(buffer);
		}

		return JsonObject.parse(new String(baos.toByteArray(), charsetName));
	}

}
