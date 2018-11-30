package omc;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import omc.corba.OMCInterface;
import omc.corba.Result;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Optional;

import omc.ZeroMQClient;

public class ZMQClientTest {

  @Test
  public void connectTest() throws IOException {
    OMCInterface client = new ZeroMQClient("omc");
    client.connect();
  }

  @Test
  public void simpleExpressionTest() throws IOException {
    ZeroMQClient client = new ZeroMQClient("omc", OMCInterface.fallbackLocale);
    client.connect();
    Result successResult = client.sendExpression("model test end test;");
    assertTrue(successResult.result.length() > 1);
    assertFalse(successResult.error.isPresent());

    Result errorResult = client.sendExpression("model test ed test;");
    assertTrue(errorResult.result.length() > 0);
    assertFalse(errorResult.error.isPresent());
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void sendWithoutConnectTest() throws IOException {
    ZeroMQClient client = new ZeroMQClient("omc", OMCInterface.fallbackLocale);
    client.sendExpression("model t end t;");
  }

  @Test
  public void testErrors() throws IOException {
    ZeroMQClient client = new ZeroMQClient("omc", OMCInterface.fallbackLocale);
    client.connect();
    Result res = client.sendExpression("loadFle(\"testbla\")");
    assertTrue(res.error.isPresent());
    assertTrue(res.error.get().contains("Class loadFle not found in scope"));
    Result res2 = client.sendExpression("model tst end tst2;");
    assertTrue(res2.result.contains("The identifier at start and end are different"));
  }

  @Test
  public void testExpressions() throws IOException {
    ZeroMQClient client = new ZeroMQClient("omc", "en_US.UTF-8");
    client.connect();
    Result r = client.sendExpression("model test Real x = 0.0; end test;");
    assertEquals(r, new Result("{test}", Optional.empty()));

    String resString = "\"Check of test completed successfully.\n" +
      "Class test has 1 equation(s) and 1 variable(s).\n" +
      "1 of these are trivial equation(s).\"";
    Result r2 = client.sendExpression("checkModel(test)");
    assertEquals(r2, new Result(resString, Optional.empty()));
  }

  @Test
  public void testCall() throws IOException {
    ZeroMQClient client = new ZeroMQClient("omc");
    client.connect();
    Result res = client.call("loadFile", "\"testi\"");
    assertEquals(res.result, "false");
  }
}
