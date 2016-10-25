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

package version;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

import java.util.*;

public class OMVersionTest {
  @Test()
  public void parseFull() {
    String s = "OpenModelica 1.11.23";
    Version v = new OMVersion(s);
    assertEquals(v.major, Optional.of(1));
    assertEquals(v.minor, Optional.of(11));
    assertEquals(v.patch, Optional.of(23));
    assertEquals(v.getRaw(), s);
    assertEquals(v.toString(), "V 1.11.23");
  }

  @Test()
  public void parseMinor() {
    String s = "OpenModelica 1.234";
    Version v = new OMVersion(s);
    assertEquals(v.major, Optional.of(1));
    assertEquals(v.minor, Optional.of(234));
    assertEquals(v.patch, Optional.empty());
    assertEquals(v.getRaw(), s);
    assertEquals(v.toString(), "V 1.234");
  }

  @Test()
  public void parseMajor() {
    String s = "OpenModelica    23";
    Version v = new OMVersion(s);
    assertEquals(v.major, Optional.of(23));
    assertEquals(v.minor, Optional.empty());
    assertEquals(v.patch, Optional.empty());
    assertEquals(v.getRaw(), s);
    assertEquals(v.toString(), "V 23");
  }
  @Test()
  public void isDev() {
    String s = "OpenModelica    23.3~dev";
    Version v = new OMVersion(s);
    assertEquals(v.major, Optional.of(23));
    assertEquals(v.minor, Optional.of(3));
    assertTrue(v.isDevVersion);

    Version v2 = new OMVersion("OpenModelica    23.3");
    assertEquals(v2.major, Optional.of(23));
    assertEquals(v2.minor, Optional.of(3));
    assertFalse(v2.isDevVersion);

  }

  @Test
  public void compareToMajor() {
    String v1 = "OpenModelica 25.3.1";
    String v2 = "OpenModelica 23.5.1";
    Version ver = new OMVersion(v1);
    Version ver2 = new OMVersion(v2);
    assertEquals(ver.compareTo(ver2), 1);
  }

  @Test
  public void compareToMinor() {
    String v1 = "OpenModelica 23.3.1";
    String v2 = "OpenModelica 23.5.1";
    Version ver = new OMVersion(v1);
    Version ver2 = new OMVersion(v2);
    assertEquals(ver.compareTo(ver2), -1);
  }

  @Test
  public void compareToPatch() {
    String v1 = "OpenModelica 25.3.4";
    String v2 = "OpenModelica 25.3.4";
    Version ver = new OMVersion(v1);
    Version ver2 = new OMVersion(v2);
    assertEquals(ver.compareTo(ver2), 0);

    String vs1 = "OpenModelica 25.3.5";
    String vs2 = "OpenModelica 25.3.4";
    Version vers = new OMVersion(vs1);
    Version vers2 = new OMVersion(vs2);
    assertEquals(vers.compareTo(vers2), 1);
  }

  @Test
  public void compareTest() {
    assertEquals(new OMVersion("OpenModelica 25").compareTo(new OMVersion("OpenModelica 25")), 0);
    assertEquals(new OMVersion("OpenModelica 34").compareTo(new OMVersion("OpenModelica 25")), 1);
    assertEquals(new OMVersion("OpenModelica 10").compareTo(new OMVersion("OpenModelica 25")), -1);

    assertEquals(new OMVersion("OpenModelica 10.1").compareTo(new OMVersion("OpenModelica 10.2")), -1);
    assertEquals(new OMVersion("OpenModelica 10.2").compareTo(new OMVersion("OpenModelica 10.2")), 0);
    assertEquals(new OMVersion("OpenModelica 10.3").compareTo(new OMVersion("OpenModelica 10.2")), 1);

    assertEquals(new OMVersion("OpenModelica 10.2.4").compareTo(new OMVersion("OpenModelica 10.2.3")), 1);
    assertEquals(new OMVersion("OpenModelica 10.2.3").compareTo(new OMVersion("OpenModelica 10.2.3")), 0);
    assertEquals(new OMVersion("OpenModelica 10.2.1").compareTo(new OMVersion("OpenModelica 10.2.3")), -1);
  }
}
