/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 */

package omc.corba;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.Arrays;
import java.util.Collections;

import omc.Global;

/** Helper for creating commands/expressions for the corba-interface.
 * 	This class contains convenient converters to generate Modelica-code.
 */
public final class ScriptingHelper {
	//regex voodoo; thumbs up for escaping the escape characters ;)
	private static String bckslash = "\\\\";
	//matches: \"Awesome test case\"
	private static Pattern quoteBackslashPattern = Pattern.compile("\n?"+bckslash+"\"((?:\n|.)*)"+bckslash+"\"\n?");
	//matches : "Awesome test case"
	private static Pattern quotePattern = Pattern.compile("^\n?\"((?:.|\n)*)\"\n?$");

	private static String arraySplitRegex = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
	private static Pattern pathPattern = Pattern.compile("((?:\\w:)?(((?:\\/|\\\\)[\\w\\-\\.\\s]+)+))");

    private static final String withinRegex = "within\\s+([\\w\\._]+);";
    private static final String modelRegex = "(?:(?:model)|(?:class)|(?:package)|(?:function))\\s+([\\w\\d_]+)";
    private static final Pattern withinPattern = Pattern.compile(withinRegex);
    private static final Pattern modelPattern = Pattern.compile(modelRegex);

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
		Matcher matcher = quoteBackslashPattern.matcher(s);
		if(matcher.matches()) {
			return (matcher.groupCount() >= 1) ? matcher.group(1).trim() : s;
		} else {
			Matcher matcher2 = quotePattern.matcher(s);
			if(matcher2.matches())
				return (matcher2.groupCount() >= 1) ? matcher2.group(1).trim() : s;
			else return s;
		}
	}

    /** Turns the given modelica expression - which should be an array -
     *  into a List of Strings.
     */
	public static List<String> fromArray(String modelicaExpr) {
		String[] subs = modelicaExpr.split(arraySplitRegex);
		if(subs.length == 1 && (subs[0].equals("{}") || subs[0].trim().isEmpty())) return Collections.emptyList();
		else {
			String tmp = subs[0];
			subs[0] = (tmp.startsWith("{")) ? tmp.substring(1) : tmp;
			tmp = subs[subs.length - 1];
			subs[subs.length - 1] = (tmp.endsWith("}")) ? tmp.substring(0, tmp.length() - 1) : tmp;
			return Arrays.stream(subs).map(String::trim).collect(Collectors.toList());
		}
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
