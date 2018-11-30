/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package omc;

import omc.ZeroMQClient;
import omc.corba.OMCInterface;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ImportHandlerTest {
  
  private Path projectRoot;
  private Path projectFile;
  private final List<String> fileContent = Arrays.asList("../lib1", "../../lib2", "/lib/SHM", "/var/lib/SH");
  private List<Path> libPaths;
  
  private final OMCInterface omc = new ZeroMQClient("omc", "en_US.UTF-8");

  @BeforeClass
  public void init() throws IOException {
    projectRoot = Files.createTempDirectory("ext-libs");
    projectFile = projectRoot.resolve("package.imports");
    libPaths = fileContent.stream().map(projectRoot::resolve).collect(Collectors.toList());
    Files.write(projectFile, fileContent, Global.encoding);
    
    omc.connect();
  }
  
  @AfterClass
  public void release() throws IOException {
    omc.disconnect();
  }
  
  @Test
  public void testValidConstructor() {
    ImportHandler libs = new ImportHandler(projectFile);
    ImportHandler libs2 = new ImportHandler(projectRoot);
    assertNotNull(libs);
    assertNotNull(libs2);
  }
  
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testInvalidConstructor() {
    ImportHandler libs = new ImportHandler(projectRoot.resolve("test"));
  }
  
  @Test
  public void testRelativeImports() throws IOException {
    ImportHandler libs = new ImportHandler(projectRoot);
    List<Path> libraries = libs.relativeImports(projectFile);
    assertEquals(libraries, libPaths);
  }
  
  @Test
  public void testLoadLibrary() throws IOException {
    ImportHandler libs = new ImportHandler(projectRoot);
    assertEquals(libs.loadLibrary(omc, Paths.get("/Users/nico/2014-modelica-kotani/SHM")), true);
  }
  
  @Test(expectedExceptions = FileNotFoundException.class)
  public void testLoadLibraryFail() throws IOException {
    ImportHandler libs = new ImportHandler(projectRoot);
    assertEquals(libs.loadLibrary(omc, projectRoot), true);
  }
}
