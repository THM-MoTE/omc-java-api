package omc.corba;

import java.io.IOException;

public interface OMCInterface {
	public Result sendExpression(String expression);
	public void connect() throws IOException;
}
