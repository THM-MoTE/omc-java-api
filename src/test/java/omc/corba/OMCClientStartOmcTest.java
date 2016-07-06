/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 */

package omc.corba;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;

import omc.Global;

import org.junit.Test;

public class OMCClientStartOmcTest {

  @Test(expected = ConnectException.class)
  public void testNotStartingOMC() throws ConnectException, IOException {
    OMCClient omc = new OMCClient();
    omc.connect();
    omc.disconnect();
  }

  @Test
  public void testNoCorbaRefFound() throws ConnectException, IOException {
    OMCClient omc = new OMCClient();
    Files.deleteIfExists(omc.getObjectReferencePath());
    try {
      omc.connect();
      fail("connect didn't through a ConnectException");
    } catch(ConnectException e) {
      //success
    }
  }

  @Test
  public void testNoCorbaRefFoundSubProcess() throws ConnectException, IOException {
    OMCClient omc = new OMCClient("/usr/local/bin/omc");
    Files.deleteIfExists(omc.getObjectReferencePath());
    omc.connect();
    omc.disconnect();
  }

  @Test
  public void testStartingOMC() throws ConnectException, IOException, InterruptedException {
    OMCClient omc = new OMCClient("/usr/local/bin/omc");
    omc.connect();

    Result result = omc.sendExpression("model t end t;");
    assertEquals("{t}", result.result);

    omc.disconnect();
  }
}
