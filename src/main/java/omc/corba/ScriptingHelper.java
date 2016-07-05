package omc.corba;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/** Helper for creating commands/expressions for the corba-interface.
 * 	This class contains convenient converters to generate Modelica-code.
 */
public final class ScriptingHelper {
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
}
