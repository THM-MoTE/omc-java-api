package omc.ior;

import omc.Global;

import java.nio.file.Path;
import java.util.Optional;

public class ZMQNameProvider implements IORNameProvider {
  public static final String DEFAULT_PORT = "om_local";
  @Override
  public Optional<String> getSuffix() {
    return Optional.empty();
  }

  @Override
  public Path getPath() {
    return Global.tmpDir.resolve(String.format("openmodelica.%s.port.&s", Global.username, DEFAULT_PORT));
  }
}
