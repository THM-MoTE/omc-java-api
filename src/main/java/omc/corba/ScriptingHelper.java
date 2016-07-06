package omc.corba;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.Arrays;
import java.util.Collections;

/** Helper for creating commands/expressions for the corba-interface.
 * 	This class contains convenient converters to generate Modelica-code.
 */
public final class ScriptingHelper {
	//regex voodoo; thumbs up for escaping the escape characters ;)
	private static String bckslash = "\\\\";
	//matches: \"Awesome test case\"
	private static Pattern hyphenBackslashPattern = Pattern.compile("\n?"+bckslash+"\"(.*)"+bckslash+"\"\n?");
	//matches : "Awesome test case"
	private static Pattern hyphenPattern = Pattern.compile("\n?\"(.*)\"\n?");

	private static Pattern arrayPattern = Pattern.compile(bckslash+"?\"?\\{(.*)\\}"+bckslash+"?\"?");

	private ScriptingHelper() {
	}

	public static String asString(Object str) {
		return "\"" + str.toString() + "\"";
	}

	public static String asArray(Collection<?> c) {
		return "{" + asParameterList(c) + "}";
	}

	public static String asParameterList(Collection<?> c) {
		StringBuilder sb = new StringBuilder();
		c.forEach(x -> sb.append(x.toString() + ", "));
		return sb.substring(0, sb.length() - 2);
	}

	public static String asStringArray(Collection<?> c) {
		List<?> xs = c.stream().map(ScriptingHelper::asString)
		    .collect(Collectors.toList());
		return asArray(xs);
	}

	public static String asStringParameterList(Collection<?> c) {
		List<?> xs = c.stream().map(ScriptingHelper::asString)
		    .collect(Collectors.toList());
		return asParameterList(xs);
	}

	public static String killTrailingHyphens(String s) {
		Matcher matcher = hyphenBackslashPattern.matcher(s);
		if(matcher.matches()) {
			return (matcher.groupCount() >= 1) ? matcher.group(1).trim() : s;
		} else {
			Matcher matcher2 = hyphenPattern.matcher(s);
			if(matcher2.matches())
				return (matcher2.groupCount() >= 1) ? matcher2.group(1).trim() : s;
			else return s;
		}
	}

	public static List<String> fromArray(String modelicaExpr) {
		Matcher matcher = arrayPattern.matcher(modelicaExpr);
		System.out.println(matcher.matches());
		System.out.println(matcher.pattern());
		if(matcher.matches()) {
			String group = matcher.group(1).trim();
			return Arrays.stream(group.split(",")).map(String::trim).collect(Collectors.toList());
		} else
			return Collections.emptyList();
	}
}
