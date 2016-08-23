/** Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  */

package omc.corba;

import java.util.Optional;

/**
 * Result from a call to omc containing the result string and an optional error-message.
 * @author Nicola Justus
 */
public class Result {
  /** The result from the call */
  public final String result;
  /** Optional Error message */
  public final Optional<String> error;

  public Result(String result, Optional<String> error) {
    super();
    this.result = result;
    this.error = error;
  }

  @Override
  public String toString() {
    return "Result [result=" + result + ", error=" + error + "]";
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Result other = (Result) obj;
    if (error == null) {
      if (other.error != null)
        return false;
    } else if (!error.equals(other.error))
      return false;
    if (result == null) {
      if (other.result != null)
        return false;
    } else if (!result.equals(other.result))
      return false;
    return true;
  }
}
