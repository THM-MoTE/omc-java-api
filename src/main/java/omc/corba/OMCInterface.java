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

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Represents a connection to omc over CORBA.
 *
 * A CORBA client is only usable after a call to
 * {@link OMCInterface#connect() connect} and until a call to
 * {@link OMCInterface#disconnect() disconnect}.
 *
 * <p>
 *  Note: Java's CORBA-API uses the java.util.logging framework.
 *  If you use another logging framework (for example Logback) you should supress
 * logging from java.util.logging. It's not possible to control the JUC framework from another logging
 * framework. SLF4J has an
 *  <a href="http://www.slf4j.org/api/org/slf4j/bridge/SLF4JBridgeHandler.html">adapter</a>
 *  to route JUC logging into SLF4J. Note that this adapter has performance issues!
 * </p>
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
public abstract class OMCInterface {
  public static String GET_ERRORS = "getErrorString()";
  public static String fallbackLocale = "en_US.UTF-8";
  public static String localeEnvVariable = "LANG";
  public static final int maxTrys = 2;
  public static final int maxSleep = 3_000;

  protected final Logger log;

  public OMCInterface() {
    log = LoggerFactory.getLogger(OMCInterface.class);
  }

  public OMCInterface(Logger log) {
    this.log = log;
  }

  /**
   * Sends the given expression to omc &amp; returning the result from omc.
   * @param expression a modelica-expression
   * @return answer from omc
   */
  public abstract Result sendExpression(String expression);
  /**
   * Connects this client to omc.
   * @see OMCInterface#disconnect()
   * @throws IOException if an io-error occurs
   */
  public abstract void connect() throws IOException;
  /**
   * Disconnects this client from omc.
   * @see OMCInterface#connect()
   * @throws IOException if an io-error occurs
   */
  public abstract void disconnect() throws IOException;

    /** Calls the function `functionName` with the given arguments.
     *  The arguments are converted into Strings using `toString()`.
     */
  public Result call(String functionName, Object... args) {
    String params =
      (args.length == 0) ? "()" : "("+ ScriptingHelper.asParameterList(Arrays.asList(args)) +")";
    String expr = functionName + params;
    Result res = sendExpression(expr);
    log.debug("calling {} returned: {}", expr, res.result);
    return res;
  }

  // =========== API functions

    /** Tests wether the `className` is a `type`.
     *  This function prepends `is` to `type`.
     */
  public boolean is_(String type, String className) {
    String functionName = "is"+type;
    Result res = call(functionName, className);
    if(res.error.isPresent()) return false;
    else {
      return res.result.equals("true") ? true : false;
    }
  }

    /** Calls the function `functionName` with the given arguments,
     *  converting the result into a List of Strings.
     *  <P> Note: If an error occurs the result is an empty list.</P>
     */
  public List<String> getList(String functionName, Object... args) {
    Result res = call(functionName, args);
    if(res.error.isPresent()) {
      return Collections.emptyList();
    } else {
      return ScriptingHelper.fromArray(res.result);
    }
  }

    /** Checks the model `modelName` with the scripting function checkModel().
     */
  public String checkModel(String modelName) {
    Result res = call("checkModel", modelName);
    if(res.error.isPresent()) {
      return res.error.get();
    } else {
      return res.result;
    }
  }

    /** Checks the model `modelName` with the scripting function checkModelsRecursive().
     */
  public String checkAllModelsRecursive(String modelName) {
    Result res = call("checkAllModelsRecursive", modelName);
    if(res.error.isPresent()) {
      return res.error.get();
    } else {
      return res.result;
    }
  }

    /** Returns the class informations about `className`.
     * <P> Note: If an error occurs the result is empty.</P>
     */
  public Optional<String> getClassInformation(String className) {
    Result res = call("getClassInformation", className);
    return (res.error.isPresent()) ?
            Optional.empty() :
            Optional.of(res.result);
  }

  public Result cd(Path path) {
    return call("cd", ScriptingHelper.convertPath(path));
  }
}
