package omc.corba;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import omc.Global;
import omc.corba.idl.OmcCommunication;

import org.junit.*;

import static org.junit.Assert.*;

public class OMCClientTest {
	@Test
	public void getObjectReferencePathTest() {
		OMCClient client = new OMCClient();
		Path path = Paths.get(System.getProperty("java.io.tmpdir"));
		System.setProperty("os.name", "Linux");
		String username = System.getProperty("user.name");
		assertEquals(path.resolve("openmodelica."+username+".objid"), client.getObjectReferencePath());
		
		System.setProperty("os.name", "Mac");
		assertEquals(path.resolve("openmodelica."+username+".objid"), client.getObjectReferencePath());
		
		System.setProperty("os.name", "Windows");
		assertEquals(path.resolve("openmodelica.objid"), client.getObjectReferencePath());
	}
	
	@Test
	public void readObjectReferenceTest() throws IOException {
		OMCClient client = new OMCClient();
		System.setProperty("os.name", "Linux");
		Path path = client.getObjectReferencePath();
		String content = client.readObjectReference(path);
		assertTrue(content.contains("IOR"));
	}
	
	@Test
	public void convertToObjectTest() throws IOException {
		OMCClient client = new OMCClient();
		System.setProperty("os.name", "Linux");
		Path path = client.getObjectReferencePath();
		String content = client.readObjectReference(path);
		OmcCommunication obj = client.convertToObject(content);
		assertNotNull(obj);
		String erg = obj.sendExpression("model test end test2;");
		assertTrue(erg.contains("The identifier at start and end are different"));
	}

	@Test
	public void connectTest() throws IOException {
		OMCClient client = new OMCClient();
		client.connect();
		Result successResult = client.sendExpression("model test end test;");
		assertTrue(successResult.result.length() > 1);
		System.out.println(successResult.errors);
		assertTrue(successResult.errors.isEmpty());

		Result errorResult = client.sendExpression("model test ed test;");
		assertEquals(errorResult.result.length(), 0);
		assertTrue(errorResult.errors.size() > 0);
	}

	@Test(expected = IllegalStateException.class)
	public void sendWithoutConnectTest() throws IOException {
		OMCClient client = new OMCClient();
		client.sendExpression("model t end t;");
	}
}
