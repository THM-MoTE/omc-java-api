/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 */

package omc.corba;

import java.io.IOException;
import java.util.Arrays;

/** Represents a connection to omc over CORBA.
 *
 * A CORBA client is only usable after a call to
 * {@link OMCInterface#connect() connect} and until a call to
 * {@link OMCInterface#disconnect() disconnect}.
 *
 * <pre>
 * A typical workflow with implementations is:
 * {@code
 *  OMCInterface client = new OMCClient();
 *  client.connect();
 *  //client is connected; start using it
 *  client.sendExpression("model test end test;");
 *  ...
 *  client.disconnect();
 *  //client disconnected; stop using it
 * }
 * </pre>
 *
 * @author Nicola Justus
 */
public interface OMCInterface {
  public static String GET_ERRORS = "getErrorString()";
  public static final int maxTrys = 2;
  public static final int maxSleep = 3_000;

  /**
   * Sends the given expression to omc & returning the result from omc.
   * @param expression a modelica-expression
   * @return answer from omc
   */
  public Result sendExpression(String expression);
  /**
   * Connects this client to omc.
   * @see OMCInterface#disconnect()
   * @throws IOException if an io-error occurs
   */
  public void connect() throws IOException;
  /**
   * Disconnects this client from omc.
   * @see OMCInterface#connect()
   * @throws IOException if an io-error occurs
   */
  public void disconnect() throws IOException;

  public default Result call(String functionName, Object... args) {
    String params =
      (args.length == 0) ? "()" : "("+ ScriptingHelper.asParameterList(Arrays.asList(args)) +")";
    String expr = functionName + params;
    return sendExpression(expr);
  }
}
