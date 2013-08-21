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

import java.util.List;

/** A JSON representation of a {@link Number}. */
public class JsonNumber extends JsonValue {

	private final long value;

	/**
	 * <code>true</code> if this value should be treated as an integral value.
	 * <code>false</code> if this value should be treated as real-valued.
	 */
	private final boolean isInteger;

	public JsonNumber(float value) {
		this.value = Double.doubleToRawLongBits(value);
		isInteger = false;
	}

	public JsonNumber(double value) {
		this.value = Double.doubleToRawLongBits(value);
		isInteger = false;
	}

	public JsonNumber(short value) {
		this.value = value;
		isInteger = true;
	}

	public JsonNumber(int value) {
		this.value = value;
		isInteger = true;
	}

	public JsonNumber(long value) {
		this.value = value;
		isInteger = true;
	}

	protected JsonNumber(String str) throws NumberFormatException {
		value = Double.doubleToRawLongBits(Double.parseDouble(str));
		isInteger = false;
	}

	public JsonNumber(Number value) {
		if (value == null) { throw new NullPointerException("Number argument may not be null."); }
		isInteger = (value.doubleValue() == ((double) value.longValue()));
		this.value = isInteger ? value.longValue() : Double
				.doubleToRawLongBits(value.doubleValue());
	}

	public boolean isInteger() {
		return isInteger;
	}

	@Override
	public Type getType() {
		return Type.NUMBER;
	}

	@Override
	public String asString() {
		return isInteger ? Long.toString(value) : Double.toString(Double.longBitsToDouble(value));
	}

	@Override
	public Number asNumber() {
		return isInteger ? value : Double.longBitsToDouble(value);
	}

	@Override
	public boolean asBoolean() {
		return isInteger ? (value != 0L) : (Double.longBitsToDouble(value) != 0.0);
	}

	@Override
	protected void appendTokenList(List<String> tokenList) {
		tokenList.add(toString());
	}

	@Override
	public String toString() {
		return isInteger ? Long.toString(value) : Double.toString(Double.longBitsToDouble(value));
	}

	@Override
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof JsonNumber) {
				JsonNumber jsonNumber = (JsonNumber) o;
				return (value == jsonNumber.value);
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (int) ((value >>> 32) ^ value);
	}

}
