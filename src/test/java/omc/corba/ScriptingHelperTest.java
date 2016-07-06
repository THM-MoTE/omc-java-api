/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 */

package omc.corba;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import org.junit.Test;

import static org.junit.Assert.*;
import static omc.corba.ScriptingHelper.*;

public class ScriptingHelperTest {

	@Test
	public void asStringTest() {
		assertEquals("\"test\"", asString("test"));
		assertEquals("\"6.4\"", asString(6.4));
		assertEquals("\"tmp/nico\"", asString(Paths.get("tmp/nico")));
	}

	@Test
	public void asParameterListTest() {
		List<String> xs = Arrays.asList("tmp", "pink", "cyan", "eclipse");
		assertEquals("tmp, pink, cyan, eclipse", asParameterList(xs));

		List<Integer> numbers = Arrays.asList(6, 5, 8, 9, 10);
		assertEquals("6, 5, 8, 9, 10", asParameterList(numbers));
	}

	@Test
	public void asArrayTest() {
		List<String> xs = Arrays.asList("tmp", "pink", "cyan", "eclipse");
		assertEquals("{tmp, pink, cyan, eclipse}", asArray(xs));

		List<Integer> numbers = Arrays.asList(6, 5, 8, 9, 10);
		assertEquals("{6, 5, 8, 9, 10}", asArray(numbers));
	}

	@Test
	public void asStringArrayTest() {
		List<String> xs = Arrays.asList("tmp", "pink", "cyan", "eclipse");
		assertEquals("{\"tmp\", \"pink\", \"cyan\", \"eclipse\"}",
		    asStringArray(xs));

		List<Integer> numbers = Arrays.asList(6, 5, 8, 9, 10);
		assertEquals("{\"6\", \"5\", \"8\", \"9\", \"10\"}", asStringArray(numbers));
	}

	@Test
	public void asStringParameterListTest() {
		List<String> xs = Arrays.asList("tmp", "pink", "cyan", "eclipse");
		assertEquals("\"tmp\", \"pink\", \"cyan\", \"eclipse\"",
		    asStringParameterList(xs));

		List<Integer> numbers = Arrays.asList(6, 5, 8, 9, 10);
		assertEquals("\"6\", \"5\", \"8\", \"9\", \"10\"",
		    asStringParameterList(numbers));
	}

	@Test
	public void killTrailingHyphensTest() {
		String bckslsh = "\\";
		String s = "\n\"eclipse is not pink! /tmp:4  \"";
		assertEquals("eclipse is not pink! /tmp:4", killTrailingHyphens(s));

		String s2 = "\n"+bckslsh+"\"Awesome test case"+bckslsh+"\"\n";
		assertEquals("Awesome test case", killTrailingHyphens(s2));
	}

	@Test
	public void fromArrayTest() {
		String s = "{nico, model, jenny, derb}";
		String s2 = "\"{nico, model, jenny, derb}\"";
		List<String> exp = Arrays.asList("nico", "model", "jenny", "derb");
		assertEquals(exp, fromArray(s));
		assertEquals(exp, fromArray(s2));

		String s3 = "\"{}\"";
		assertEquals(Collections.emptyList(), fromArray(s3));
	}
}
