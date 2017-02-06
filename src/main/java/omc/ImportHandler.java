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

import omc.corba.OMCInterface;
import omc.corba.Result;
import omc.corba.ScriptingHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImportHandler {
  private static final String importFileName = "package.imports";
  private static final String packageFileName = "package.mo";
  
  private final Logger log = LoggerFactory.getLogger(ImportHandler.class);
  private final Path importFile;
  
  private final List<Pair<String, Path>> importedLibs = new ArrayList<>();
  
  public ImportHandler(Path projectFile) {
    if (projectFile.endsWith(packageFileName)) projectFile = projectFile.getParent();
    
    if (Files.isDirectory(projectFile)) {
      importFile = projectFile.resolve(importFileName);
    } else if (!projectFile.endsWith(importFileName)) {
      throw new IllegalArgumentException("The given projectFile has to end with: " + importFileName);
    } else {
      importFile = projectFile;
    }
    log.info("Using file: {}", importFile);
  }
  
  List<Path> relativeImports(Path file) throws IOException {
    Path parent = file.getParent();
    if (Files.exists(file)) return Files.lines(file).map(parent::resolve).collect(Collectors.toList());
    else return new ArrayList<>();
  }
  
  public void loadLibraries(OMCInterface omc) throws IOException, LoadLibraryException {
    List<Path> libraries = relativeImports(importFile);
    log.debug("Loading {}", libraries);
    List<String> errors = libraries.stream().map(lib -> {
      try {
        return loadLibrary(omc, lib) ? "" : "Couldn't load library " + lib;
      } catch (FileNotFoundException e) {
        return e.getMessage();
      }
    }).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    
    if (!errors.isEmpty())
      throw new LoadLibraryException("Couldn't load all libraries", errors);
  }
  
  boolean loadLibrary(OMCInterface omc, Path packageFile) throws FileNotFoundException {
    packageFile = packageFile.toAbsolutePath().normalize();
    if (Files.notExists(packageFile)) throw new FileNotFoundException("The library " + packageFile + " does not exist");
  
    Result res = omc.call("loadFile", ScriptingHelper.convertPath(packageFile));
    log.debug("Loading {} returned {}", packageFile, res);
    if (res.result.equals("true")) {
      res = omc.call("getLoadedLibraries");
      List<Pair<String, Path>> libs = ScriptingHelper.fromNestedArrayToNestedList(res.result).stream().map(o -> {
        List<String> l = (List<String>) o;
        return new ImmutablePair<>(l.get(0), Paths.get(l.get(1)));
      }).collect(Collectors.toList());
      for (Pair<String, Path> p : libs) {
        if (p.getValue().equals(packageFile.getParent())) {
          if (!this.importedLibs.contains(p)) this.importedLibs.add(p);
          break;
        }
      }
      return true;
    }
    return false;
  }
  
  public List<Pair<String, Path>> getImportedLibs() {
    return importedLibs;
  }
}
