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

import java.nio.file.Path;
import java.util.Optional;

/** A provider that returns the name of the IOR file of omc.
 */
public interface IORNameProvider {
  /** Returns the suffix of the IOR.
   * <p>
   * This suffix is the file-extension used in the {@code --sessionName} parameter of omc.
   * </p>
   */
  public Optional<String> getSuffix();

  /** Returns the absolute path to the IOR. */
  public Path getPath();
}
