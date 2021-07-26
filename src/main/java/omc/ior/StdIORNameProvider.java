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

import java.nio.file.Path;
import java.util.Optional;

import omc.Global;

/** A provider for the default IOR-filename that is defined by omc if no
 *  {@code -c/--sessionName} flag is given to omc.
 *
 */
public class StdIORNameProvider implements IORNameProvider {

  public StdIORNameProvider() {
  }

  /** Always returns Optional.empty()! (There is no suffix if no -c flag is given!) */
  @Override
  public Optional<String> getSuffix() {
    return Optional.empty();
  }

  /** Returns the default path defined by OMC.
   * <p>
   * This is either:
   * </p>
   * <ul>
   *  <li> Linux/Mac OS: {@code ($TMP)/openmodelica.<USERNAME>.objid} </li>
   *  <li> Windows: {@code ($TMP)/openmodelica.objid} </li>
   *</ul>
   */
  @Override
  public Path getPath() {
    Path resultingPath;
    if (Global.isLinuxOS() || Global.isMacOS()) {
      // objRef in <tmp>/openmodelica.<username>.objid
      resultingPath = Global.tmpDir.resolve("openmodelica." + Global.username + ".objid");
    } else {
      // objRef in <tmp>/openmodelica.objid
      resultingPath = Global.tmpDir.resolve("openmodelica.objid");
    }
    return resultingPath;
  }

}
