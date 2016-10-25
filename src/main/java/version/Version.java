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

package version;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Version implements Comparable<Version> {
  protected final String rawString;
  protected final Optional<Integer> major;
  protected final Optional<Integer> minor;
  protected final Optional<Integer> patch;
  public final Boolean isDevVersion;
  protected final Logger log = LoggerFactory.getLogger(Version.class);

  protected Version(String raw) {
    rawString = raw;
    List<Optional<Integer>> numbers = parseVersion(rawString).stream()
        .map(Optional::of).collect(Collectors.toList());

    Optional<Integer> mj = Optional.empty();
    Optional<Integer> mi = Optional.empty();
    Optional<Integer> pt = Optional.empty();
    for (int i = 0; i < numbers.size(); i++) {
      if (i == 0)
        mj = numbers.get(i);
      else if (i == 1)
        mi = numbers.get(i);
      else if (i == 2)
        pt = numbers.get(i);
    }

    major = mj;
    minor = mi;
    patch = pt;

    isDevVersion = parseDevVersion(rawString);

    log.debug("raw: {} - major: {} minor: {} patch: {} isDev: {}", rawString,
        major, minor, patch, isDevVersion);
  }

  public String getRaw() {
    return rawString;
  }

  protected abstract List<Integer> parseVersion(String rawString);
  protected abstract Boolean parseDevVersion(String rawString);

  @Override
  public int compareTo(Version v) {
    Optional<Pair<Integer>> majorPair = this.major.flatMap(n1 -> v.major.map(n2 -> new Pair<Integer>(n1,n2)));
    Optional<Pair<Integer>> minorPair = this.minor.flatMap(n1 -> v.minor.map(n2 -> new Pair<Integer>(n1,n2)));
    Optional<Pair<Integer>> patchPair = this.patch.flatMap(n1 -> v.patch.map(n2 -> new Pair<Integer>(n1,n2)));

    return majorPair.flatMap(pair -> {
      if(pair.v1.equals(pair.v2)) {
        return minorPair.flatMap(minPair -> {

          if(minPair.v1.equals(minPair.v2)) {

            return patchPair.flatMap(ptPair -> {
              if(ptPair.v1.equals(ptPair.v2)) {
                return Optional.of(0);
              } else return Optional.of(ptPair.v1.compareTo(ptPair.v2));
            });

          } else return Optional.of(minPair.v1.compareTo(minPair.v2));
        });

      } else return Optional.of(pair.v1.compareTo(pair.v2));
    }).orElse(0);
  }

  @Override
  public String toString() {
    String s = "V ";
    if(major.isPresent())
      s += major.get();
    if(minor.isPresent())
      s += "." + minor.get();
    if(patch.isPresent())
      s+= "."+patch.get();

    return s;
  }
}
