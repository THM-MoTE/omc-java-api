/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package omc;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import omc.corba.OMCInterface;
import omc.corba.Result;
import omc.ior.ZMQPortFileProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Optional;

import omc.ZeroMQClient;

public class ZMQClientTest {

  @Test
  public void connectTest() throws IOException {
    OMCInterface client = new ZeroMQClient("omc");
    client.connect();
  }

  @Test
  public void simpleExpressionTest() throws IOException {
    ZeroMQClient client = new ZeroMQClient("omc", OMCInterface.fallbackLocale, new ZMQPortFileProvider());
    client.connect();
    Result successResult = client.sendExpression("model test end test;");
    assertTrue(successResult.result.length() > 1);
    assertFalse(successResult.error.isPresent());

    Result errorResult = client.sendExpression("model test ed test;");
    assertTrue(errorResult.result.length() > 0);
    assertFalse(errorResult.error.isPresent());
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void sendWithoutConnectTest() throws IOException {
    ZeroMQClient client = new ZeroMQClient("omc", OMCInterface.fallbackLocale, new ZMQPortFileProvider());
    client.sendExpression("model t end t;");
  }

  @Test
  public void testErrors() throws IOException {
    ZeroMQClient client = new ZeroMQClient("omc", OMCInterface.fallbackLocale, new ZMQPortFileProvider());
    client.connect();
    Result res = client.sendExpression("loadFle(\"testbla\")");
    assertTrue(res.error.isPresent());
    assertTrue(res.error.get().contains("Class loadFle not found in scope"));
    Result res2 = client.sendExpression("model tst end tst2;");
    assertTrue(res2.result.contains("The identifier at start and end are different"));
  }

  @Test
  public void testExpressions() throws IOException {
    ZeroMQClient client = new ZeroMQClient("omc", "en_US.UTF-8", new ZMQPortFileProvider());
    client.connect();
    Result r = client.sendExpression("model test Real x = 0.0; end test;");
    assertEquals(r, new Result("{test}", Optional.empty()));

    String resString = "\"Check of test completed successfully.\n" +
      "Class test has 1 equation(s) and 1 variable(s).\n" +
      "1 of these are trivial equation(s).\"";
    Result r2 = client.sendExpression("checkModel(test)");
    assertEquals(r2, new Result(resString, Optional.empty()));
  }

  @Test
  public void testCall() throws IOException {
    ZeroMQClient client = new ZeroMQClient("omc");
    client.connect();
    Result res = client.call("loadFile", "\"testi\"");
    assertEquals(res.result, "false");
  }
}
