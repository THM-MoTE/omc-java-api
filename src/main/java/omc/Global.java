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

package omc;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Global {
  private Global() {
  }

  public static boolean isLinuxOS() {
    return System.getProperty("os.name").contains("Linux");
  }

  public static boolean isMacOS() {
    return System.getProperty("os.name").contains("Mac");
  }

  public static boolean isWindowsOS() {
    return System.getProperty("os.name").contains("Windows");
  }

  public static final Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
  public static final String username = System.getProperty("user.name");

  public static final Charset encoding = Charset.forName("UTF-8");
}
