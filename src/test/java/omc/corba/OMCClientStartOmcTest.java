/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 */

package omc.corba;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ConnectException;

import org.junit.Test;

public class OMCClientStartOmcTest {
  @Test(expected = ConnectException.class)
  public void testNotStartingOMC() throws ConnectException, IOException {
    OMCClient omc = new OMCClient();
    omc.connect();
  }
  
  @Test
  public void testStartingOMC() throws ConnectException, IOException {
    OMCClient omc = new OMCClient("/usr/local/bin/omc");
    omc.connect();
    
    Result result = omc.sendExpression("model t end t;");
    assertEquals("{t}", result.result);
  }
}
