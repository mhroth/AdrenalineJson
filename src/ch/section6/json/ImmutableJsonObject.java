/*
 * Copyright (c) 2012, Martin Roth (mhroth@section6.ch)
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

import java.util.Collection;
import java.util.Set;

/** An interface rendering a {@link JsonObject} immutable. */
public interface ImmutableJsonObject {
	
	/** A convenience function to return the keyed value as a {@link String}. */
  public String getString(String key)
  		throws JsonCastException, UnknownKeyException;
  
  /** A convenience function to return the keyed value as a {@link Boolean}. */
  public boolean getBoolean(String key)
  		throws JsonCastException, UnknownKeyException;
  
  /** A convenience function to return the keyed value as a {@link JsonObject}. */
  public ImmutableJsonObject getObject(String key)
  		throws JsonCastException, UnknownKeyException;
  
  /** A convenience function to return the keyed value as a {@link JsonArray}. */
  public ImmutableJsonArray getArray(String key)
  		throws JsonCastException, UnknownKeyException;
  
  /** A convenience function to return the keyed value as a {@link Number}. */
  public Number getNumber(String key)
  		throws JsonCastException, UnknownKeyException;
  
  public boolean isEmpty();
  
  public Set<String> keySet();
  
  public int size();
  
  public Collection<JsonValue> values();
  
  /** Returns this {@link JsonObject} as an {@link ImmutableJsonObject}. */
  public ImmutableJsonObject asImmutable();
  
  /** @see JsonValue */
  public JsonValue copy();
  
  /** @see JsonValue */
  public String toString(int indent);

}
