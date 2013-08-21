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

import java.util.Date;

/**
 * An interface rendering a {@link JsonArray} immutable. It is effectively a subset of the
 * <code>List</code> interface.
 */
public interface ImmutableJsonArray {

	/** A convenience function to return the indexed value as a {@link String}. */
	public String getString(int index) throws JsonCastException;

	/** A convenience function to return the indexed value as a {@link Number}. */
	public Number getNumber(int index) throws JsonCastException;

	/**
	 * A convenience function to return the indexed value as a <code>boolean</code>.
	 */
	public boolean getBoolean(int index) throws JsonCastException;

	/**
	 * A convenience function to return the indexed value as a {@link JsonArray} .
	 */
	public ImmutableJsonArray getArray(int index) throws JsonCastException;

	/**
	 * A convenience function to return the indexed value as a {@link JsonObject}.
	 */
	public ImmutableJsonObject getObject(int index) throws JsonCastException;

	/** A convenience function to return the indexed value as a {@link Date}. */
	public Date getDate(int index) throws JsonCastException;

	public int size();

	public boolean isEmpty();

	/** @see JsonValue */
	public String toString(int indent);

	/** @see JsonValue */
	public JsonValue copy();

	/** Returns this {@link JsonArray} as an {@link ImmutableJsonArray}. */
	public ImmutableJsonArray asImmutable();

	/**
	 * Returns an {@link Iterable} for iterating across {@link Strings}. Elements are cast to
	 * strings if necessary.
	 */
	public Iterable<String> stringIterable();

	/**
	 * Returns an {@link Iterable} for iterating across {@link Boolean}s. Elements are cast to
	 * booleans if necessary.
	 */
	public Iterable<Boolean> booleanIterable();

	/**
	 * Returns an {@link Iterable} for iterating across {@link Number}s. Elements are cast to
	 * numbers if necessary.
	 */
	public Iterable<Number> numberIterable();

}
