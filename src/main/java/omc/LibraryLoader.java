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

import omc.corba.OMCInterface;
import omc.corba.Result;
import omc.corba.ScriptingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryLoader {
    private final Logger log = LoggerFactory.getLogger(LibraryLoader.class);
    public static final String importFileName = "package.imports";
    private static final String packageFileName = "package.mo";
    private final Path importFile;

    public LibraryLoader(Path projectFile) {
        if(Files.isDirectory(projectFile)) {
            importFile = projectFile.resolve(importFileName);
            if(Files.notExists(importFile))
                throw new IllegalArgumentException("The given project directory does not contain a "+importFileName);
        } else if(!projectFile.endsWith(importFileName)) {
            throw new IllegalArgumentException("The given projectFile has to end with: "+importFileName);
        } else {
            importFile = projectFile;
        }
        log.info("Using file: {}", importFile);
    }

    List<Path> relativeImports(Path file) throws IOException {
        Path parent = file.getParent();
        return Files.lines(file).map(parent::resolve).collect(Collectors.toList());
    }

    public boolean loadLibraries(OMCInterface omc) throws IOException, FileNotFoundException {
        List<Path> libraries = relativeImports(importFile);
        log.debug("Loading {}", libraries);
        return libraries.stream().map(lib -> {
            try {
                return loadLibrary(omc, lib);
            } catch (FileNotFoundException e) {
                return false;
            }
        }).allMatch(b -> b);
    }

    boolean loadLibrary(OMCInterface omc, Path libraryDirectory) throws FileNotFoundException {
        Path packageFile = libraryDirectory.resolve(packageFileName);
        if(Files.notExists(packageFile)) {
            throw new FileNotFoundException("The library " + libraryDirectory + " does not have a " + packageFileName);
        } else {
            Result res = omc.call("loadFile", ScriptingHelper.convertPath(packageFile));
            log.debug("Loading {} returned {}", libraryDirectory, res);
            return res.result.equals("true");
        }
    }
}
