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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ExternalLibraries {
    private final Logger log = LoggerFactory.getLogger(ExternalLibraries.class);
    private final String importFileName = "package.imports";
    private final Path importFile;

    public ExternalLibraries(Path projectFile) {
        if(Files.isDirectory(projectFile)) {
            importFile = projectFile.resolve(importFileName);
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

    load
}
