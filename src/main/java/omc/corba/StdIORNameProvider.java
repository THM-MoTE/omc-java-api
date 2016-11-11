package omc.corba;

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
   * <ul>
   *  <li> Linux/Mac OS: {@code ($TMP)/openmodelica.<USERNAME>.objid} </li>
   *  <li> Windows: {@code ($TMP)/openmodelica.objid} </li>
   *</ul>
   * </p>
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
