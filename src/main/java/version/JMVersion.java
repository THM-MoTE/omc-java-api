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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JMVersion extends Version {
  private static final Pattern versionPattern =
      Pattern.compile("\\w(\\d+)");

  public JMVersion(String versionString) {
    super(versionString);
  }

  @Override
  protected List<Integer> parseVersion(String rawString) {
    Matcher matcher = versionPattern.matcher(rawString);
    if(matcher.find())
      return Arrays.asList(Integer.parseInt(matcher.group(1)));
    else
      return Collections.emptyList();
  }

  @Override
  protected Boolean parseDevVersion(String rawString) {
    return false;
  }
}
