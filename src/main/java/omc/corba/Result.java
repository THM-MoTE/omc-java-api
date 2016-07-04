/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 */

package omc.corba;

import java.util.Optional;

public class Result {
  public final String result;
  public final Optional<String> error;

  public Result(String result, Optional<String> error) {
    super();
    this.result = result;
    this.error = error;
  }
}
