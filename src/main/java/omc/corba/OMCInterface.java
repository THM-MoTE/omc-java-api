package omc.corba;

import java.io.IOException;

public interface OMCInterface {
  public static String GET_ERRORS = "getErrorString();";

  public Result sendExpression(String expression);

  public void connect() throws IOException;
}
