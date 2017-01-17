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

public class SemanticVersionTest {
  @Test()
  public void parseFullVersion() {
		Version v = new SemanticVersion("V 1.2.3");
		assertEquals(v.major, Optional.of(1));
		assertEquals(v.minor, Optional.of(2));
		assertEquals(v.patch, Optional.of(3));
		assertEquals(v.getRaw(), "V 1.2.3");
		assertEquals(v.toString(), "V 1.2.3");

		Version v2 = new SemanticVersion("v 1.2.3");
		assertEquals(v2.major, Optional.of(1));
		assertEquals(v2.minor, Optional.of(2));
		assertEquals(v2.patch, Optional.of(3));
		assertEquals(v2.getRaw(), "v 1.2.3");
		assertEquals(v2.toString(), "V 1.2.3");

		Version v3 = new SemanticVersion("1.2.3");
		assertEquals(v3.major, Optional.of(1));
		assertEquals(v3.minor, Optional.of(2));
		assertEquals(v3.patch, Optional.of(3));
		assertEquals(v3.getRaw(), "1.2.3");
		assertEquals(v3.toString(), "V 1.2.3");
	}

	@Test()
	public void parseMinor() {
		Version v = new SemanticVersion("V 1.2");
		assertEquals(v.major, Optional.of(1));
		assertEquals(v.minor, Optional.of(2));
		assertEquals(v.patch, Optional.empty());
		assertEquals(v.getRaw(), "V 1.2");
		assertEquals(v.toString(), "V 1.2");
	}

	@Test()
	public void parseMajor() {
		Version v = new SemanticVersion("V 1");
		assertEquals(v.major, Optional.of(1));
		assertEquals(v.minor, Optional.empty());
		assertEquals(v.patch, Optional.empty());
		assertEquals(v.getRaw(), "V 1");
		assertEquals(v.toString(), "V 1");
	}
}
