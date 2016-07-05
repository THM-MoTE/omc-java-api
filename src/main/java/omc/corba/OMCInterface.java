/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 */

package omc.corba;

import java.io.IOException;

public interface OMCInterface {
  public static final String GET_ERRORS = "getErrorString();";
  public static final int maxTrys = 2;
  public static final int maxSleep = 3_000;

  public Result sendExpression(String expression);

  public void connect() throws IOException;
  public void disconnect() throws IOException;
}
