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

import java.util.function.Function;
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
    Function<Integer, Integer> mul100 = i -> i*100;
    Function<Integer, Integer> mul10 = i -> i*10;
      //calculate digit of this version
    Integer thisMji = this.major.map(mul100).orElse(0);
    Integer thisMii = this.minor.map(mul10).orElse(0);
    Integer thisPti = this.patch.orElse(0);
    Integer thisVersion = thisMji + thisMii + thisPti;

    //calculate digit of that version
    Integer thatMji = v.major.map(mul100).orElse(0);
    Integer thatMii = v.minor.map(mul10).orElse(0);
    Integer thatPti = v.patch.orElse(0);
    Integer thatVersion = thatMji + thatMii + thatPti;

    return thisVersion.compareTo(thatVersion);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Version)) return false;

    Version version = (Version) o;
    return rawString.equals(version.rawString);
  }

  @Override
  public int hashCode() {
    return rawString.hashCode();
  }

  @Override
  public String toString() {
    Function<Integer,String> asString = i -> i.toString();
    Function<Integer,String> withDot = i -> "." + i.toString();
    return "V " +
      major.map(asString).orElse("") +
      minor.map(withDot).orElse("") +
      patch.map(withDot).orElse("");
  }
}
