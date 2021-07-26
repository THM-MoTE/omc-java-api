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

package omc.ior;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/** Provider for custom/self-defined IOR name (the suffix).
 * If unique=true the IOR file will be made unique by appending a number to the given suffix.
 *
 *  <p><b>Note:</b><br>
 *  The check for uniqueness is triggered one's in the constructor-call.
 *  After instantiation the final IOR name is fixed!
 *  <p>
 */
public class CustomIORNameProvider implements IORNameProvider {
  private final String suffix;
  private final boolean unique;
  private final IORNameProvider stdProvider = new StdIORNameProvider();
  private final Path iorPath;

  public CustomIORNameProvider(String suffix, boolean unique) {
    this.unique = unique;
    this.suffix = createUniqueName(stdProvider.getPath(), "."+suffix);
    this.iorPath = Paths.get(stdProvider.getPath().toString() + this.suffix);
  }

  private String createUniqueName(Path startPath, String suffix) {
    final String suffixPath = startPath.toString();
    if (unique) {
      int cnt = 2;
      String uniqueSuffix = suffix;
      while (Files.exists(Paths.get(suffixPath + uniqueSuffix))) {
        uniqueSuffix = suffix + "-" + cnt;
        cnt++;
      }
      return uniqueSuffix;
    } else
      return suffix;
  }

  @Override
  public Path getPath() {
    return iorPath;
  }

  @Override
  public Optional<String> getSuffix() {
      //drop the '.'
    return Optional.of(suffix.substring(1));
  }
}
