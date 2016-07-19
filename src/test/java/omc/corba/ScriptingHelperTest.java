/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 */

package omc.corba;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
	public void killTrailingQuotesTest() {
		String bckslsh = "\\";
		String s = "\n\"eclipse is not pink! /tmp:4  \"";
		assertEquals("eclipse is not pink! /tmp:4", killTrailingQuotes(s));

		String s2 = "\n"+bckslsh+"\"Awesome test case"+bckslsh+"\"\n";
		assertEquals("Awesome test case", killTrailingQuotes(s2));

		String s3 = "\"Check of test completed successfully.\n"+
		    "Class test has 2 equation(s) and 1 variable(s).\n"+
		    "2 of these are trivial equation(s).\"";

		assertEquals("Check of test completed successfully.\n"+
        "Class test has 2 equation(s) and 1 variable(s).\n"+
        "2 of these are trivial equation(s).", killTrailingQuotes(s3));
	}

	@Test
	public void fromArrayTest() {
		String s = "{nico, model, jenny, derb}";
		String s2 = "{nico, model, jenny, derb}";
		List<String> exp = Arrays.asList("nico", "model", "jenny", "derb");
		assertEquals(exp, fromArray(s));
		assertEquals(exp, fromArray(s2));

		String s3 = "{}";
		assertEquals(Collections.emptyList(), fromArray(s3));

		String s4 = "{\"model\",\"Ideal linear electrical resistor\",false,false,false,\"/usr/lib/omlibrary/Modelica 3.2.2/Electrical/Analog/Basic.mo\",true,53,3,113,15,{},false,false,\"\",\"\"}";
		List<String> exp2 = Arrays.asList(
				"\"model\"",
				"\"Ideal linear electrical resistor\"",
				"false","false","false",
				"\"/usr/lib/omlibrary/Modelica 3.2.2/Electrical/Analog/Basic.mo\"",
				"true","53","3","113","15","{}","false","false","\"\"","\"\"");
		assertEquals(exp2, fromArray(s4));
	}

	@Test
	public void getModelNameTest() {
	  String test = "model test\nReal x = 1;\nend test;";
	  assertEquals(Optional.of("test"), getModelName(test));

   String test2 = "this is a tst file";
   assertEquals(Optional.empty(), getModelName(test2));

   String test3 = "within modelica.nico.test;\nmodel Baroreceptor \"a wonderfull comment\"\nend Baroreceptor;";
   assertEquals(Optional.of("modelica.nico.test.Baroreceptor"), getModelName(test3));
	}

	@Test
	public void getModelNameFromPathTest() throws URISyntaxException, IOException {
	  Path file1 = Paths.get(getClass().getClassLoader().getResource("ResistorTest.mo").toURI());
	  Path file2 = Paths.get(getClass().getClassLoader().getResource("ResistorTest2.mo").toURI());

	  assertEquals(Optional.of("ResistorTest"), getModelName(file1));
	  assertEquals(Optional.of("nico.components.ResistorTest"), getModelName(file2));
	}

  @Test
  public void extractPathTest() {
    String test = "(\"package\",\"\",false,false,false,\"/Users/nico/2014-modelica-kotani/SHM/package.mo\",false,2,1,6,8,{},false,false,\"\",\"\")";
    assertEquals(Optional.of("/Users/nico/2014-modelica-kotani/SHM/package.mo"),extractPath(test));
    assertEquals(Optional.of("/home/nico/blup.txt"),extractPath("/home/nico/blup.txt"));
    assertEquals(Optional.of("/home/nico/blup.txt"),extractPath("truefalse,true\"/home/nico/blup.txt\"cksiqichökajs"));
  }
}
