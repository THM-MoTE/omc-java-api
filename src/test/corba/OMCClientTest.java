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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import omc.corba.idl.OmcCommunication;

import org.testng.annotations.Test;

public class OMCClientTest {
  @Test
  public void getObjectReferencePathTest() {
    OMCClient client = new OMCClient();
    Path path = Paths.get(System.getProperty("java.io.tmpdir"));
    System.setProperty("os.name", "Linux");
    String username = System.getProperty("user.name");
    assertEquals(client.getObjectReferencePath(), path.resolve("openmodelica." + username + ".objid"));

    System.setProperty("os.name", "Mac");
    assertEquals(client.getObjectReferencePath(), path.resolve("openmodelica." + username + ".objid"));

    System.setProperty("os.name", "Windows");
    assertEquals(client.getObjectReferencePath(), path.resolve("openmodelica.objid"));
  }

  @Test
  public void readObjectReferenceTest() throws IOException {
    OMCClient client = new OMCClient();
    System.setProperty("os.name", "Linux");
    Path path = client.getObjectReferencePath();
    String content = client.readObjectReference(path);
    assertTrue(content.contains("IOR"));
  }

  @Test
  public void convertToObjectTest() throws IOException {
    OMCClient client = new OMCClient("omc", "de_DE.UTF-8");
    System.setProperty("os.name", "Linux");
    Path path = client.getObjectReferencePath();
    String content = client.readObjectReference(path);
    OmcCommunication obj = client.convertToObject(content);
    assertNotNull(obj);
    String erg = obj.sendExpression("model test end test2;");
    assertTrue(erg.contains("The identifier at start and end are different"));
  }

  @Test
  public void connectTest() throws IOException {
    OMCClient client = new OMCClient("omc", "de_DE.UTF-8");
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
    OMCClient client = new OMCClient("omc", "de_DE.UTF-8");
    client.sendExpression("model t end t;");
  }

  @Test
  public void testErrors() throws IOException {
    OMCClient client = new OMCClient("omc", "de_DE.UTF-8");
    client.connect();
    Result res = client.sendExpression("loadFle(\"testbla\")");
    assertTrue(res.error.isPresent());
    assertTrue(res.error.get().contains("Klasse loadFle konnte nicht im"));
    Result res2 = client.sendExpression("model tst end tst2;");
    assertTrue(res2.result.contains("The identifier at start and end are different"));
  }

  @Test
  public void testExpressions() throws IOException {
    OMCClient client = new OMCClient("omc", "en_US.UTF-8");
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
    OMCClient client = new OMCClient();
    client.connect();
    Result res = client.call("loadFile", "\"testi\"");
    assertEquals(res.result, "false");
  }
}
