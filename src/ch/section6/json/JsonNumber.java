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

import java.util.List;

/** A JSON representation of a {@link Number}. */
public class JsonNumber extends JsonValue {

  private final Number number;
  
  public JsonNumber(Number number) {
    if (number == null) throw new IllegalArgumentException();
    this.number = number.doubleValue();
  }
  
  protected JsonNumber(String str) throws NumberFormatException {
    number = Double.parseDouble(str);
  }

  @Override
  public Type getType() {
    return Type.NUMBER;
  }
  
  @Override
  public String asString() {
    return Double.toString(number.doubleValue());
  }

  @Override
  public Number asNumber() {
    return number;
  }

  @Override
  protected void appendTokenList(List<String> tokenList) {
    tokenList.add(toString());
  }
  
  @Override
  public String toString() {
    long num = number.longValue();
    return ((double) num == number.doubleValue())
        ? Long.toString(num) : Double.toString(number.doubleValue());
  }
  
  @Override
  public boolean equals(Object o) {
    if (o != null) {
      if (o instanceof JsonNumber) {
        JsonNumber jsonNumber = (JsonNumber) o;
        return number.equals(jsonNumber.number);
      }
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return number.hashCode();
  }

}
