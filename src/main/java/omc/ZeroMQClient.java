package omc;

import omc.corba.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import omc.ior.IORNameProvider;
import omc.ior.ZMQPortFileProvider;
import org.zeromq.*;
import org.zeromq.ZMQ.*;

public class ZeroMQClient extends OMCInterface {

  private final IORNameProvider portFileProvider;
  private final int ioThreadCnt = 1;
  private final String omcLocale;
  private final String omc;
  private Socket socket;
  private Context context;
  private boolean isConnected = false;

  public ZeroMQClient(String omcExec) {
    this(omcExec, findLocale());
  }

  public ZeroMQClient(String omcExec, String locale) {
    super();
    this.omcLocale = locale;
    this.omc = omcExec;
    portFileProvider = new ZMQPortFileProvider();
  }

  @Override
  public Result sendExpression(String expression) {
    if(!isConnected) {
      throw new IllegalStateException("ZMQ-Client not connected!");
    } else {
      boolean success = socket.send(expression);
      String response = socket.recvStr().trim();
      log.debug("sendExpression: {} returned {}", expression, response);
      return new Result(response, getError());
    }
  }

  private Optional<String> getError() {
    boolean success = socket.send(GET_ERRORS);
    String erg = socket.recvStr().trim();
    log.debug("receiving errors returned: {}", erg);

    if(erg.isEmpty() || erg.equals("\"\""))
      return Optional.empty();
    else if(erg.startsWith("\"")) {
      //kill dangling hyphens
      erg = erg.substring(1);
      if(erg.endsWith("\""))
        erg = erg.substring(0, erg.length()-1);
    }
    return Optional.of(erg.trim());
  }


  @Override
  public void connect() throws IOException {
    context = ZMQ.context(ioThreadCnt);
    socket = context.socket(ZMQ.REQ);
    String portFilecontent = readPortFile();
    log.debug("port file content: {}", portFilecontent);
    boolean con = socket.connect(portFilecontent);
    log.debug("connecting to socket: {}", con);
    isConnected = true;
  }

  @Override
  public void disconnect() throws IOException {
    socket.close();
    context.close();
    isConnected = false;
  }

  private String readPortFile() throws IOException {
    Path path = portFileProvider.getPath();
    log.debug("reading port file at: {}", path);
    List<String> lines = Files.readAllLines(path, Global.encoding);
    return lines.get(0);
  }
}
