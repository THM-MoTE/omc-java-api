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

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;

import omc.Global;

public class OMCClientStartOmcTest {

  @Test(expectedExceptions = ConnectException.class)
  public void testNotStartingOMC() throws ConnectException, IOException {
    OMCClient omc = new OMCClient();
    omc.connect();
    omc.disconnect();
  }

  @Test()
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

  @Test()
  public void testNoCorbaRefFoundSubProcess() throws ConnectException, IOException {
    OMCClient omc = new OMCClient("/usr/local/bin/omc");
    Files.deleteIfExists(omc.getObjectReferencePath());
    omc.connect();
    omc.disconnect();
  }

  @Test()
  public void testStartingOMC() throws ConnectException, IOException, InterruptedException {
    OMCClient omc = new OMCClient("/usr/local/bin/omc");
    omc.connect();

    Result result = omc.sendExpression("model t end t;");
    assertEquals(result.result, "{t}");

    omc.disconnect();
  }
}
