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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OMVersion extends SemanticVersion {
  private static final Pattern versionPattern =
      Pattern.compile("OpenModelica\\s+(?:(\\d+))?(?:\\.(\\d+))?(?:\\.(\\d+))?(?:~dev)?");

  public OMVersion(String versionString) {
    super(versionString);
  }

  @Override
  protected List<Integer> parseVersion(String rawString) {
    Matcher matcher = versionPattern.matcher(rawString);
    return Version.extractNumbers(matcher);
  }

  @Override
  protected Boolean parseDevVersion(String rawString) {
   return rawString.contains("dev");
  }
}
