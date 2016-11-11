package omc.corba;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomIORNameProvider implements IORNameProvider {
  private final String suffix;
  private final boolean unique;
  private final IORNameProvider stdProvider = new StdIORNameProvider();
  private final Path iorPath;
  private final Logger log = LoggerFactory.getLogger(CustomIORNameProvider.class);
  
  public CustomIORNameProvider(String suffix, boolean unique) {
    this.unique = unique;
    this.suffix = createUniqueName(stdProvider.getPath(), "."+suffix);
    this.iorPath = Paths.get(stdProvider.getPath().toString() + this.suffix);
    log.debug("created suffix: {} path: {}", this.suffix, this.iorPath);
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
      log.debug("created unique suffix: {}", uniqueSuffix);
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