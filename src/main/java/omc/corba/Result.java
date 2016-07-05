/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
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
