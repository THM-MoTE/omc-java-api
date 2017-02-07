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

import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static omc.corba.ScriptingHelper.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ScriptingHelperTest {

	@Test()
	public void asStringTest() {
		assertEquals(asString("test"), "\"test\"");
		assertEquals(asString(6.4), "\"6.4\"");
		assertEquals(asString(Paths.get("tmp/nico")), "\"tmp/nico\"");
	}

	@Test()
	public void asParameterListTest() {
		List<String> xs = Arrays.asList("tmp", "pink", "cyan", "eclipse");
		assertEquals(asParameterList(xs), "tmp, pink, cyan, eclipse");

		List<Integer> numbers = Arrays.asList(6, 5, 8, 9, 10);
		assertEquals(asParameterList(numbers), "6, 5, 8, 9, 10");
	}

	@Test()
	public void asArrayTest() {
		List<String> xs = Arrays.asList("tmp", "pink", "cyan", "eclipse");
		assertEquals(asArray(xs), "{tmp, pink, cyan, eclipse}");

		List<Integer> numbers = Arrays.asList(6, 5, 8, 9, 10);
		assertEquals(asArray(numbers), "{6, 5, 8, 9, 10}");
	}

	@Test()
	public void asStringArrayTest() {
		List<String> xs = Arrays.asList("tmp", "pink", "cyan", "eclipse");
		assertEquals(asStringArray(xs), "{\"tmp\", \"pink\", \"cyan\", \"eclipse\"}");

		List<Integer> numbers = Arrays.asList(6, 5, 8, 9, 10);
		assertEquals(asStringArray(numbers), "{\"6\", \"5\", \"8\", \"9\", \"10\"}");
	}

	@Test()
	public void asStringParameterListTest() {
		List<String> xs = Arrays.asList("tmp", "pink", "cyan", "eclipse");
		assertEquals(asStringParameterList(xs), "\"tmp\", \"pink\", \"cyan\", \"eclipse\"");

		List<Integer> numbers = Arrays.asList(6, 5, 8, 9, 10);
		assertEquals(asStringParameterList(numbers), "\"6\", \"5\", \"8\", \"9\", \"10\"");
	}

	@Test()
	public void killTrailingQuotesTest() {
		String bckslsh = "\\";
		String s = "\"\neclipse is not pink! /tmp:4  \"";
		assertEquals(killTrailingQuotes(s), "eclipse is not pink! /tmp:4");

		String s2 = "\"\nAwesome test case\n\"";
		assertEquals(killTrailingQuotes(s2), "Awesome test case");

		String s3 = "\"Check of test completed successfully.\n"+
		    "Class test has 2 equation(s) and 1 variable(s).\n"+
		    "2 of these are trivial equation(s).\"";

		assertEquals(killTrailingQuotes(s3), "Check of test completed successfully.\n"+
        "Class test has 2 equation(s) and 1 variable(s).\n"+
        "2 of these are trivial equation(s).");
		assertEquals(killTrailingQuotes("\"\""), "");
		assertEquals(killTrailingQuotes(""), "");
		assertEquals(killTrailingQuotes(" "), "");
	}

	@Test()
	public void fromArrayTest() {
		String s = "{nico, model, jenny, derb}";
		String s2 = "{nico, model, jenny, derb}";
		List<String> exp = Arrays.asList("nico", "model", "jenny", "derb");
		assertEquals(fromArray(s), exp);
		assertEquals(fromArray(s2), exp);

		String s3 = "{}";
		assertEquals(fromArray(s3), Collections.emptyList());

		String s4 = "{\"model\",\"Ideal linear electrical resistor\",false,false,false,\"/usr/lib/omlibrary/Modelica 3.2.2/Electrical/Analog/Basic.mo\",true,53,3,113,15,{},false,false,\"\",\"\"}";
		List<String> exp2 = Arrays.asList(
				"\"model\"",
				"\"Ideal linear electrical resistor\"",
				"false","false","false",
				"\"/usr/lib/omlibrary/Modelica 3.2.2/Electrical/Analog/Basic.mo\"",
				"true","53","3","113","15", "{}","false","false","\"\"","\"\"");
		assertEquals(fromArray(s4), exp2);

		String s5 = "{{{nico, \"derb\"}}}";
		assertEquals(fromArray(s5), Arrays.asList("nico", "\"derb\""));
	}
  
  @Test()
  public void fromNestedArrayTest() {
    String s = "(nico, model, jenny, derb)";
    String s2 = "{nico, model, jenny, derb}";
    List<String> exp = Arrays.asList("nico", "model", "jenny", "derb");
    System.out.println(s);
    assertEquals(fromNestedArray(s), exp);
    System.out.println(s2);
    assertEquals(fromNestedArray(s2), exp);
    
    String s3 = "{}";
    System.out.println(s3);
    assertEquals(fromNestedArray(s3), Collections.emptyList());
    
    String s4 = "{\"model\",\"Ideal linear electrical resistor\",false,false,false,\"/usr/lib/omlibrary/Modelica 3.2.2/Electrical/Analog/Basic.mo\",true,53,3,113,15,{},false,false,\"\",\"\"}";
    List<Object> exp2 = Arrays.asList(
				"model",
				"Ideal linear electrical resistor",
				"false", "false", "false",
				"/usr/lib/omlibrary/Modelica 3.2.2/Electrical/Analog/Basic.mo",
				"true", "53", "3", "113", "15", Collections.EMPTY_LIST, "false", "false", "", "");
		System.out.println(s4);
    assertEquals(fromNestedArrayToNestedList(s4), exp2);
    
    String s5 = "{{{nico, \"derb\"}}}";
    System.out.println(s5);
    assertEquals(fromNestedArray(s5), Arrays.asList("nico", "\"derb\""));
  }

	@Test()
	public void getModelNameTest() {
	  String test = "model test\nReal x = 1;\nend test;";
	  assertEquals(getModelName(test), Optional.of("test"));

   String test2 = "this is a tst file";
   assertEquals(getModelName(test2), Optional.empty());

   String test3 = "within modelica.nico.test;\nmodel Baroreceptor \"a wonderfull comment\"\nend Baroreceptor;";
   assertEquals(getModelName(test3), Optional.of("modelica.nico.test.Baroreceptor"));
	}

	@Test()
	public void getModelNameFromPathTest() throws URISyntaxException, IOException {
	  Path file1 = Paths.get(getClass().getClassLoader().getResource("ResistorTest.mo").toURI());
	  Path file2 = Paths.get(getClass().getClassLoader().getResource("ResistorTest2.mo").toURI());

	  assertEquals(getModelName(file1), Optional.of("ResistorTest"));
	  assertEquals(getModelName(file2), Optional.of("nico.components.ResistorTest"));
	}

  @Test()
  public void extractPathTest() {
    String test = "(\"package\",\"\",false,false,false,\"/Users/nico/2014-modelica-kotani/SHM/package.mo\",false,2,1,6,8,{},false,false,\"\",\"\")";
		String winTest = "this is a test,C:\\Documents\\user\\test.mo,superstring";
		assertEquals(extractPath(test), Optional.of("/Users/nico/2014-modelica-kotani/SHM/package.mo"));
    assertEquals(extractPath("/home/nico/blup.txt"), Optional.of("/home/nico/blup.txt"));
    assertEquals(extractPath("truefalse,true\"/home/nico/blup.txt\"cksiqichökajs"), Optional.of("/home/nico/blup.txt"));
    assertEquals(extractPath(winTest), Optional.of("C:\\Documents\\user\\test.mo"));
  }

	@Test()
	public void asPathTest() {
		System.setProperty("os.name", "Linux");
		assertTrue(System.getProperty("os.name") == "Linux", "`os.name` couldn't set to `Linux`");

		String path1 = "/home/user/Dokumente/Docs";
		String winPath = "C:\\Users\\chris\\Documents\\year";
		assertEquals(convertPath(path1), "\""+path1+"\"");
		assertEquals(convertPath(winPath), "\""+winPath+"\"");

		System.setProperty("os.name", "Windows");
		assertTrue(System.getProperty("os.name") == "Windows", "`os.name` couldn't set to `Windows`");

		assertEquals(convertPath(winPath), "\"C:\\\\Users\\\\chris\\\\Documents\\\\year\"");
		assertEquals(convertPath(path1), "\""+path1+"\"");
	}
}
