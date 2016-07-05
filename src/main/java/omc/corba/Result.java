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
}
