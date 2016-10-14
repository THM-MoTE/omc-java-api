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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Map;

import omc.Global;
import omc.corba.idl.OmcCommunication;
import omc.corba.idl.OmcCommunicationHelper;

import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Implementation of OMCInterface.
 *
 * This implementation sets the language for omc using the given locale.
 * If no locale is given the language is extracted from the environment
 * variable identified by `OMCInterface.localeEnvVariable`.
 * If this fails too the constant `OMCInterface.fallbackLocale` is used.
 * <p>
 * This implementation is based on <B>OpenModelica's system documentation</B> which is available here:
 * <a href="https://www.openmodelica.org/svn/OpenModelica/tags/OPENMODELICA_1_9_0_BETA_4/doc/OpenModelicaSystem.pdf">System Documentation</a>.
 * The implementation awaits CORBA-object references in the following 2 path's:
 * <ul>
 *  <li>Windows: <code>$TMP/openmodelica.objid</code></li>
 *  <li>Linux, Mac: <code>$TMP/openmodelica.USER.objid</code></li>
 * </ul>
 * Further information about omc's corba-communication are available at System Documentation page 130.
 * </p>
 * @author Nicola Justus
 */
public class OMCClient extends OMCInterface {
  private OmcCommunication omc;
  private Optional<ORB> orbOpt = Optional.empty();
  private boolean isConnected;
  private final Optional<String> omcExecutable;
  private Optional<Process> omcProcess = Optional.empty();
  private final String omcLocale;

  public OMCClient() {
    super();
    omcExecutable = Optional.empty();
    omcLocale = findLocale();
  }

  public OMCClient(String omcExec) {
    super();
    omcExecutable = Optional.of(omcExec);
    omcLocale = findLocale();
  }
  
  public OMCClient(Path omcExec) {
    this(omcExec.toString());
  }

  public OMCClient(Path omcExec, String locale) {
    this(omcExec.toString(), locale);
  }

  public OMCClient(String omcExec, String locale) {
    super();
    omcExecutable = Optional.of(omcExec);
    omcLocale = locale;
  }

  private String findLocale() {
    String systemLang = System.getenv("LANG");
    if(systemLang != null)
      return systemLang;
    else {
      log.warn("environment variable `LANG` undefined; using fallback locale: {}", fallbackLocale);
      return fallbackLocale;
    }
  }

  @Override
  public Result sendExpression(String expression) {
    if (!isConnected)
      throw new IllegalStateException("Client not connected! Connect first!");

    String erg = omc.sendExpression(expression).trim();
    return new Result(erg, getError());
  }

  @Override
  public void connect() throws IOException, ConnectException {
    connectImpl(0);
    call("loadModel", "Modelica");
  }

  private void connectImpl(int tryCnt) throws FileNotFoundException, IOException {
    if(tryCnt < maxTrys) {
      try {
        //try to read the corba reference
        this.omc = createOmcConnection();
        omc.sendExpression("model t end t;");
        isConnected = true;
        log.debug("connected");
      } catch (FileNotFoundException | org.omg.CORBA.COMM_FAILURE e) {
        //corba ref not found or connection error
        if(omcExecutable.isPresent()) {
          //ok start omc; try again
          log.debug("Couldn't connect to omc; try starting omc-subprocess");
          omcProcess = Optional.of(startOMC());
          try {
            Thread.sleep(maxSleep);
          } catch (InterruptedException ex) {
            // ignore & try again
          }
          connectImpl(tryCnt+1);
        }
        else {
          //give up
          log.error("Couldn't connect to omc. Start omc as subprocess is disabled!");
          throw new ConnectException("Couldn't connect to omc!");
        }
      }
    } else {
      //tried enough; give up
      log.error("Couldn't connect to omc after {} attempts", tryCnt);
      throw new ConnectException("Couldn't connect to omc!");
    }
  }


  @Override
  public void disconnect() throws IOException {
    omc._release();
    //IMPORTANT: kill ORB-instance so that
    //running RMI thread terminate
    orbOpt.ifPresent(orb -> {
      orb.shutdown(true);
      orb.destroy();
      log.debug("killed ORB");
    });
    omcProcess.ifPresent(p -> {
      p.destroy();
      log.debug("killed omc subprocess");
    });
    isConnected = false;
  }

  private OmcCommunication createOmcConnection() throws IOException, FileNotFoundException {
    Path refPath = getObjectReferencePath();
    if(Files.exists(refPath)) {
      String stringifiedRef = readObjectReference(refPath);
      return convertToObject(stringifiedRef);
    } else
      throw new FileNotFoundException("Couldn't find "+refPath);
  }

  public Optional<String> getError() {
    String erg = omc.sendExpression(GET_ERRORS).trim();

    if(erg.isEmpty() || erg.equals("\"\""))
      return Optional.empty();
    else if(erg.startsWith("\"")) {
      //kill dangling hyphens
     erg = erg.substring(1);
     if(erg.endsWith("\""))
       erg = erg.substring(0, erg.length()-1);
    }

    return Optional.of(erg.trim());
  }

  /** Converts the given String object into a OmcCommunication object. */
  OmcCommunication convertToObject(String stringifiedObject) {
    String args[] = new String[1];
    ORB orb = ORB.init(args, null);
    orbOpt = Optional.of(orb);
    return OmcCommunicationHelper.narrow(orb
        .string_to_object(stringifiedObject));
  }

  /** Reads a object reference from the given path into a String. */
  String readObjectReference(Path pathToObjRef) throws IOException {
    // read only 1 line, ignoring linebreak
    String head = Files.readAllLines(pathToObjRef).get(0);
    log.debug("Read CORBA-Reference from {}", pathToObjRef);
    return head;
  }

  /** Returns a path to the omc CORBA object. */
  Path getObjectReferencePath() {
    Path resultingPath;
    if (Global.isLinuxOS() || Global.isMacOS()) {
      // objRef in <tmp>/openmodelica.<username>.objid
      resultingPath = Global.tmpDir.resolve("openmodelica." + Global.username + ".objid");
    } else {
      // objRef in <tmp>/openmodelica.objid
      resultingPath = Global.tmpDir.resolve("openmodelica.objid");
    }
    return resultingPath;
  }

  /** Starts a new omc-instance as subprocess */
  private Process startOMC() {
    String arg = "+d=interactiveCorba";
    if(!omcExecutable.isPresent())
      throw new IllegalStateException("unknown omc executable!");

    ProcessBuilder pb = new ProcessBuilder(omcExecutable.get(), arg);
    //set environment
    Map<String,String> env = pb.environment();
    env.put(localeEnvVariable, omcLocale);

    Path omcWorkingDir = Global.tmpDir.resolve("omc_home");
    Path logFile = omcWorkingDir.resolve("omc.log");
    try {
      //setup working directory & log file
      Files.createDirectories(omcWorkingDir);
      Files.deleteIfExists(logFile);
      Files.createFile(logFile);
    } catch (IOException e) {
      log.error("Couldn't create working directory or logfile for omc", e);
      throw new IllegalStateException("Couldn't create working directory or logfile for omc");
    }

    pb.directory(omcWorkingDir.toFile());
    pb.redirectErrorStream(true); //merge stderr into stdin
    pb.redirectOutput(logFile.toFile());

    try {
      Process process = pb.start();
      log.info("started {} {} - locale {} - output redirecting to: {}",
        omcExecutable.get(), arg, omcLocale, logFile);
      return process;
    } catch (IOException e) {
      log.error("Couldn't start {} {} as subprocess in {}", omcExecutable.get(), arg, omcWorkingDir,  e);
      throw new IllegalStateException("couldn't start omc!");
    }
  }

}
