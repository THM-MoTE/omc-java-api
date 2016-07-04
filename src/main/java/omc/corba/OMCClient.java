package omc.corba;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.omg.CORBA.ORB;

import omc.Global;
import omc.corba.idl.OmcCommunication;
import omc.corba.idl.OmcCommunicationHelper;

public class OMCClient implements OMCInterface {
	private OmcCommunication omc;
	private boolean isConnected;

	public OMCClient() {
		
	}
	
	@Override
  public Result sendExpression(String expression) {
		if (!isConnected)
			throw new IllegalStateException("Client not connected! Connect first!");

		String erg = omc.sendExpression(expression);
	  List<String> error = getErrors();
	  return new Result(erg, error);
  }

	@Override
  public void connect() throws IOException {
		Path refPath = getObjectReferencePath();
		String stringifiedRef = readObjectReference(refPath);
		omc = convertToObject(stringifiedRef);
		isConnected = true;
  }
	
	public List<String> getErrors() {
		String erg = omc.sendExpression(GET_ERRORS);
    if(erg.isEmpty()) return Collections.emptyList();
    else return Arrays.asList(erg);
	}
	OmcCommunication convertToObject(String stringifiedObject) {
		String args[] = new String[1];
		ORB orb = ORB.init(args, null);
		return OmcCommunicationHelper.narrow(orb.string_to_object(stringifiedObject));
	}
	
	String readObjectReference(Path pathToObjRef) throws IOException {
		//read only 1 line, ignoring linebreak
		return Files.readAllLines(pathToObjRef).get(0);
	}
	
	Path getObjectReferencePath() {
		final String osType = System.getProperty("os.name");
		final String username = System.getProperty("user.name");
		final Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
		Path resultingPath;
		if(osType.contains("Linux") || osType.contains("Mac")) {
			//objRef in <tmp>/openmodelica.<username>.objid
			resultingPath = tmpDir.resolve("openmodelica."+username+".objid");
		} else {
		//objRef in <tmp>/openmodelica.objid
			resultingPath = tmpDir.resolve("openmodelica.objid");
		}
		return resultingPath;
	}
}
