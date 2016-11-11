package omc.corba;

import java.nio.file.Path;
import java.util.Optional;

/** A provider that returns the name of the IOR file of omc.
 */
public interface IORNameProvider {
  /** Returns the suffix of the IOR. */
  public Optional<String> getSuffix();

  /** Returns the absolute path to the IOR. */
  public Path getPath();
}
