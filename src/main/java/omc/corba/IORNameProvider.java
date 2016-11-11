package omc.corba;

import java.nio.file.Path;
import java.util.Optional;

public interface IORNameProvider {
  public Optional<String> getSuffix();

  public Path getPath();
}
