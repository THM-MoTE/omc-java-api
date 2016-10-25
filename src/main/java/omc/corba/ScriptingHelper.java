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

package omc.corba;

import omc.Global;
import omc.corba.parser.ListLexer;
import omc.corba.parser.ListParser;
import omc.corba.parser.ListParser.ListElementContext;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Helper for creating commands/expressions for the corba-interface.
 * 	This class contains convenient converters to generate Modelica-code.
 */
public final class ScriptingHelper {
	//regex voodoo; thumbs up for escaping the escape characters ;)
	private static String bckslash = "\\\\";

	//matches: ["this is a test"] AND [this is a test]
	private static Pattern quotePattern = Pattern.compile("^\"((?:.|\\n)+)\"$");
	//matches: [{}] AND [{bla, "blup", hans}]
	private static Pattern extractArrayPattern = Pattern.compile("^\\{((?:.|\\n)*)\\}$");

	private static String arraySplitRegex = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
	private static Pattern pathPattern = Pattern.compile("((?:\\w:)?(((?:\\/|\\\\)[\\w\\-\\.\\s]+)+))");

    private static final String withinRegex = "within\\s+([\\w\\._]+);";
    private static final String modelRegex =
			"(?:(?:model)|(?:class)|(?:package)|(?:function)|(?:connector))\\s+([\\w\\d_]+)";
    private static final Pattern withinPattern = Pattern.compile(withinRegex);
    public static final Pattern modelPattern = Pattern.compile(modelRegex);

	private ScriptingHelper() {
	}

    /** Turns the given object into a modelica string. */
	public static String asString(Object str) {
		return "\"" + str.toString() + "\"";
	}

    /** Turns the given collection into an modelica array. */
	public static String asArray(Collection<?> c) {
		return "{" + asParameterList(c) + "}";
	}

    /** Turns the given collection into a modelica parameterlist. */
	public static String asParameterList(Collection<?> c) {
		if(c.isEmpty()) return "";
		else {
			StringBuilder sb = new StringBuilder();
			c.forEach(x -> sb.append(x.toString() + ", "));
			return sb.substring(0, sb.length() - 2);
		}
	}

    /** Turns the given collection into an modelica array of strings. */
	public static String asStringArray(Collection<?> c) {
		List<?> xs = c.stream().map(ScriptingHelper::asString)
		    .collect(Collectors.toList());
		return asArray(xs);
	}

    /** Turns the given collection into a modelica parameterlist of
     *  strings using Object#toString(). */
	public static String asStringParameterList(Collection<?> c) {
		List<?> xs = c.stream().map(ScriptingHelper::asString)
		    .collect(Collectors.toList());
		return asParameterList(xs);
	}

    /** Removes trailing and leading quotes (&quot;) from `s`. */
	public static String killTrailingQuotes(String s) {
		Matcher matcher = quotePattern.matcher(s);
		if(s.equals("\"\"")) return "";
		else if(matcher.matches()) {
			return matcher.group(1).trim();
		} else {
			return s.trim();
		}
	}

    /** Turns the given modelica expression - which should be an array -
     *  into a List of Strings.
     */
	public static List<String> fromArray(String modelicaExpr) {
		List<String> list = new ArrayList<>();
		try {
			ListParser p = new ListParser(new CommonTokenStream(new ListLexer(new ANTLRInputStream(new ByteArrayInputStream(modelicaExpr.getBytes())))));
			return fromArray(p.list().listElement());
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Matcher matcher = extractArrayPattern.matcher(modelicaExpr);
//			matcher.find();
//			String extractedArray = matcher.group(1);
//			String[] subs = extractedArray.split(arraySplitRegex);
//			return (subs.length == 0 || extractedArray.isEmpty()) ? Collections.emptyList() :
//				Arrays.stream(subs).map(String::trim).collect(Collectors.toList());
		return list;
	}
	
	private static List<String> fromArray(List<ListElementContext> list) {
		List<String> l = new ArrayList<>();
		for(ListElementContext lec : list) {
			if(lec.list() != null) l.addAll(fromArray(lec.list().listElement()));
			else if(lec.bool() != null) l.add(lec.bool().getText());
			else if(lec.NUMBER() != null) l.add(lec.NUMBER().getText());
			else if(lec.STRING() != null) l.add(lec.STRING().getText());
			else if(lec.WORD() != null) l.add(lec.WORD().getText());
		}
		return l;
	}

    /** Returns the `name` of a model inside of `modelicaCode`.
     * <P>Note: If there are more than one models the result is the
     * first model. </P>
     */
  public static Optional<String> getModelName(String modelicaCode) {
    Matcher withinMatcher = withinPattern.matcher(modelicaCode);
    Matcher modelMatcher = modelPattern.matcher(modelicaCode);
    String packageName = "";
    if(withinMatcher.find()) {
      packageName = withinMatcher.group(1) + ".";
    }

    if(modelMatcher.find()) {
      String modelName = modelMatcher.group(1);
      return Optional.of(packageName + modelName);
    } else
      return Optional.empty();
  }

    /** Returns the `name` of a model in the `file`.
     *  @see ScriptingHelper#getModelName(String)
     */
  public static Optional<String> getModelName(Path file) throws IOException {
    return getModelName(new String(Files.readAllBytes(file), Global.encoding));
  }

    /** Extracts a path from the given string `str`. */
  public static Optional<String> extractPath(String str) {
    Matcher pathMatcher = pathPattern.matcher(str);
    if (pathMatcher.find()) {
      String path = pathMatcher.group(1);
      return Optional.of(path);
    } else
      return Optional.empty();
  }

	public static String convertPath(String path) {
		if(Global.isWindowsOS())
			return asString(path.replace("\\", "\\\\"));
		else return asString(path);
	}

	public static String convertPath(Path path) {
		return convertPath(path.toString());
	}
}
