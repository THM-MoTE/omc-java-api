package omc.corba;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OmcAPITest {
  private OMCInterface omc;
  @Before
  public void initClient() throws IOException {
    omc = new OMCClient("/usr/local/bin/omc");
    omc.connect();
  }
  @After
  public void stopClient() throws IOException {
    omc.disconnect();
  }

  @Test
  public void is_test() throws ConnectException, IOException {
    omc.sendExpression("class test end test;");
    assertTrue(omc.is_("Class", "test"));
    assertFalse(omc.is_("Class", "test2"));
    omc.sendExpression("model nico end nico;");
    assertTrue(omc.is_("Model", "nico"));
  }

  @Test
  public void getListTest() throws ConnectException, IOException {
    omc.call("clear");
    omc.sendExpression("model test end test;");
    assertEquals(Arrays.asList("test"), omc.getList("getClassNames"));
    assertEquals(Collections.emptyList(), omc.getList("getPackages"));
  }

  @Test
  public void checkModelTest() throws ConnectException, IOException {
    omc.call("clear");
    omc.sendExpression("model test Intger x; equation x = 0.5; end test;");
    String exp = "[<interactive>:1:12-1:19:writable] Error: Klasse Intger konnte nicht im Geltungsbereich von test gefunden werden.\nError: Error occurred while flattening model test";
    assertEquals(exp, omc.checkModel("test"));
    omc.sendExpression("model test2 Integer x; equation x = 1; end test2;");
    String exp2 = "\"Check of test2 completed successfully.\nClass test2 has 1 equation(s) and 1 variable(s).\n1 of these are trivial equation(s).\"";
    assertEquals(exp2, omc.checkModel("test2"));
  }
}
