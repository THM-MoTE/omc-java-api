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

package omc;

import static org.testng.Assert.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExternalLibrariesTest {

    private Path projectRoot;
    private Path projectFile;
    private List<String> fileContent = Arrays.asList("../lib1", "../../lib2", "/lib/SHM", "/var/lib/SH");
    private List<Path> libPaths;

    @BeforeClass
    public void init() throws IOException {
        projectRoot = Files.createTempDirectory("ext-libs");
        projectFile = projectRoot.resolve("package.imports");
        libPaths = fileContent.stream().map(projectRoot::resolve).collect(Collectors.toList());
        Files.write(projectFile, fileContent, Global.encoding);
    }

    @AfterClass
    public void release() {

    }

    @Test
    public void testValidConstructor() {
        ExternalLibraries libs = new ExternalLibraries(projectFile);
        ExternalLibraries libs2 = new ExternalLibraries(projectRoot);
        assertNotNull(libs);
        assertNotNull(libs2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        ExternalLibraries libs = new ExternalLibraries(projectRoot.resolve("test"));
    }

    @Test
    public void testRelativeImports() throws IOException {
        ExternalLibraries libs = new ExternalLibraries(projectRoot);
        List<Path> libraries = libs.relativeImports(projectFile);
        assertEquals(libraries, libPaths);
    }
}
