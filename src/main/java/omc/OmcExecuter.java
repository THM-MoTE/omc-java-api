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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class OmcExecuter {
  private final String omcExecutable;
  private final String locale;
  private final Logger log;
  private Optional<Process> process = Optional.empty();

  public OmcExecuter(String omcExecutable, String locale) {
    this.omcExecutable = omcExecutable;
    this.locale = locale;
    this.log = LoggerFactory.getLogger(OmcExecuter.class);
  }

  public Process startOmc(String... arguments) {
    List<String> cmd = new LinkedList<>();
    cmd.add(omcExecutable);
    cmd.addAll(Arrays.asList(arguments));

    ProcessBuilder pb = new ProcessBuilder(cmd);
    //set environment
    Map<String,String> env = pb.environment();
    env.put(OMCInterface.localeEnvVariable, locale);
    env.put("USER", Global.username);

    Path omcWorkingDir = Global.tmpDir.resolve("omc_home-"+ UUID.randomUUID().toString());
    Path logFile = omcWorkingDir.resolve("omc.log");
    try {
      //setup working directory & log file
      Files.createDirectories(omcWorkingDir);
      Files.deleteIfExists(logFile);
      Files.createFile(logFile);
    } catch (IOException e) {
      log.error("Couldn't create working directory or logfile for omc", e);
      throw new IllegalStateException("Couldn't create working directory or logfile for omc");
    }

    pb.directory(omcWorkingDir.toFile());
    pb.redirectErrorStream(true); //merge stderr into stdin
    pb.redirectOutput(logFile.toFile());

    try {
      Process process = pb.start();
      this.process = Optional.of(process);
      log.info("started {} locale {} - output redirecting to: {}",
        cmd, locale, logFile);
      return process;
    } catch (IOException e) {
      log.error("Couldn't start {} as subprocess in {}", cmd, omcWorkingDir,  e);
      throw new IllegalStateException("couldn't start omc!");
    }
  }

  public void shutdown() {
    process.ifPresent(p -> p.destroy());
  }
}
