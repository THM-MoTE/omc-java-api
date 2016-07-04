package omc.corba;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	}
}
