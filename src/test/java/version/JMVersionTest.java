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

public class JMVersionTest {
  @Test()
  public void jmVersion() {
    Version v = new JMVersion("r33456");
    assertEquals(v.toString(), "V 33456");
    assertEquals(v.major, Optional.of(33456));
    assertEquals(v.minor, Optional.empty());
    assertEquals(v.patch, Optional.empty());
  }

  @Test()
  public void compareVersion() {
    Version v = new JMVersion("r33452");
    Version v2 = new JMVersion("r33456");
    assertEquals(v.compareTo(v2), -1);
    assertEquals(v2.compareTo(new JMVersion("r33456")), 0);
    assertEquals(v2.compareTo(new JMVersion("r33444")), 1);
  }
}
