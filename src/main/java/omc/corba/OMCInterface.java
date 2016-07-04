/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 */

package omc.corba;

import java.io.IOException;

public interface OMCInterface {
  public static String GET_ERRORS = "getErrorString()";

  public Result sendExpression(String expression);

  public void connect() throws IOException;
}
