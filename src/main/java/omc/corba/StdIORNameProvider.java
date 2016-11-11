package omc.corba;

import java.nio.file.Path;
import java.util.Optional;

import omc.Global;

public class StdIORNameProvider implements IORNameProvider {

  public StdIORNameProvider() {
  }

  @Override
  public Optional<String> getSuffix() {
    return Optional.empty();
  }

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
