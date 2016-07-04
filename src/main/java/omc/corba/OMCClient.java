/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 */

package omc.corba;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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

  public OMCClient() {
  }

  @Override
  public Result sendExpression(String expression) {
    if (!isConnected)
      throw new IllegalStateException("Client not connected! Connect first!");

    String erg = omc.sendExpression(expression);
    log.debug("sendExpression returned:\n{}", erg);
    return new Result(erg, getError());
  }

  @Override
  public void connect() throws IOException {
    Path refPath = getObjectReferencePath();
    String stringifiedRef = readObjectReference(refPath);
    omc = convertToObject(stringifiedRef);
    isConnected = true;
    log.debug("connected");
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
    log.debug("Read {} as {}", pathToObjRef, head);
    return head;
  }

  Path getObjectReferencePath() {
    Path resultingPath;
    if (Global.isLinuxOS() || Global.isMacOS()) {
      // objRef in <tmp>/openmodelica.<username>.objid
      resultingPath = Global.tmpDir.resolve("openmodelica." + Global.username + ".objid");
      log.debug("OS is Linux, Mac; looking for file at {}", resultingPath);
    } else {
      // objRef in <tmp>/openmodelica.objid
      resultingPath = Global.tmpDir.resolve("openmodelica.objid");
      log.debug("OS is Windows; looking for file at {}", resultingPath);
    }
    return resultingPath;
  }
}
