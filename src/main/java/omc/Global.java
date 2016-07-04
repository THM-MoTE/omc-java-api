/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
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
