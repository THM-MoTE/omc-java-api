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

package omc.ior;

import omc.Global;

import java.nio.file.Path;
import java.util.Optional;

/** A suffix-provider for ZeroMQ connections. This suffix is used to find the zmq port file. */
public class ZMQPortFileProvider implements IORNameProvider {
  public static final String DEFAULT_SUFFIX = "om_local";

  private final String suffix;

  /** Create a provider by using the DEFAULT_SUFFIX='om_local'. */
  public ZMQPortFileProvider() {
    this(DEFAULT_SUFFIX);
  }

  /** Create a provider with the given suffix. */
  public ZMQPortFileProvider(String suffix) {
    this.suffix = suffix;
  }

  @Override
  public Optional<String> getSuffix() {
    return Optional.of(suffix);
  }

  @Override
  public Path getPath() {
    return Global.tmpDir.resolve(String.format("openmodelica.%s.port.%s", Global.username, suffix));
  }
}
