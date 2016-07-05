/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 */

package omc.corba;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.Scanner;

import omc.Global;
import omc.corba.idl.OmcCommunication;
import omc.corba.idl.OmcCommunicationHelper;

import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OMCClient implements OMCInterface {
  private Logger log = LoggerFactory.getLogger(OMCClient.class);
  private OmcCommunication omc;
  private boolean isConnected;
  private final Optional<String> omcExecutable;
  private Optional<Process> omcProcess = Optional.empty();

  public OMCClient() {
    omcExecutable = Optional.empty();
  }

  public OMCClient(String omcExec) {
    omcExecutable = Optional.of(omcExec);
  }

  @Override
  public Result sendExpression(String expression) {
    if (!isConnected)
      throw new IllegalStateException("Client not connected! Connect first!");

    String erg = omc.sendExpression(expression).trim();
    log.debug("sendExpression returned:\n{}", erg);
    return new Result(erg, getError());
  }

  @Override
  public void connect() throws IOException, ConnectException {
    omc = createOmcConnection();
    checkLiveness(omc);
    isConnected = true;
    log.debug("connected");
  }

  @Override
  public void disconnect() throws IOException {
    omcProcess.ifPresent(p -> {
      log.debug("kill sub-omc");
      p.destroy();
    });
  }

  private OmcCommunication createOmcConnection() throws IOException {
    Path refPath = getObjectReferencePath();
    String stringifiedRef = readObjectReference(refPath);
    return convertToObject(stringifiedRef);
  }

  private void checkLiveness(OmcCommunication omc) throws IOException, ConnectException {
    checkLivenessImpl(omc, 0);
  }

  private void checkLivenessImpl(OmcCommunication omc, int tryCnt) throws IOException, ConnectException {
    if (tryCnt < maxTrys) {
      try {
        omc.sendExpression("model t end t;");
        this.omc = omc;
      } catch (Exception ex) {
        if (omcExecutable.isPresent()) {
          omcProcess = Optional.of(startOMC());
          try {
            Thread.sleep(maxSleep);
          } catch (InterruptedException e) {
            // ignore & try again
          }
          OmcCommunication newConn = createOmcConnection();
          assert(newConn != omc);
          assert(!newConn.equals(omc));
          checkLivenessImpl(newConn, tryCnt + 1);
        } else {
          log.error("Couldn't connect to omc. Start omc as subprocess is disabled!");
          throw new ConnectException("Couldn't connect to omc!");
        }
      }
    } else {
      log.error("Couldn't connect to omc after {} attempts", tryCnt);
      throw new ConnectException("Couldn't connect to omc!");
    }
  }

  public Optional<String> getError() {
    String erg = omc.sendExpression(GET_ERRORS);
    return (erg.isEmpty()) ? Optional.empty() : Optional.of(erg);
  }

  OmcCommunication convertToObject(String stringifiedObject) {
    String args[] = new String[1];
    ORB orb = ORB.init(args, null);
    return OmcCommunicationHelper.narrow(orb
        .string_to_object(stringifiedObject));
  }

  String readObjectReference(Path pathToObjRef) throws IOException {
    // read only 1 line, ignoring linebreak
    String head = Files.readAllLines(pathToObjRef).get(0);
    log.debug("Read CORBA-Reference from {}", pathToObjRef);
    return head;
  }

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

  private Process startOMC() {
    String arg = "+d=interactiveCorba";
    if(!omcExecutable.isPresent())
      throw new IllegalStateException("unknown omc executable!");

    ProcessBuilder pb = new ProcessBuilder(omcExecutable.get(), arg);
    Path omcWorkingDir = Global.tmpDir.resolve("omc_home");
    Path logFile = omcWorkingDir.resolve("omc.log");
    try {
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
      log.debug("started {} as omc-instance", omcExecutable.get());
      return process;
    } catch (IOException e) {
      log.debug("exc", e);
      log.error("Couldn't start {} {} as subprocess in {}", omcExecutable.get(), arg, omcWorkingDir);
      throw new IllegalStateException("couldn't start omc!");
    }
  }

}
