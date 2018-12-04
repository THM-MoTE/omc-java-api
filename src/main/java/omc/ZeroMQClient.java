package omc;

import omc.corba.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import omc.ior.IORNameProvider;
import omc.ior.ZMQPortFileProvider;
import org.zeromq.*;
import org.zeromq.ZMQ.*;

public class ZeroMQClient extends OMCInterface {

  private final IORNameProvider portFileProvider;
  private final int ioThreadCnt = 1;
  private  final OmcExecuter omcExecutor;
  private Socket socket;
  private Context context;
  private boolean isConnected = false;

  private final Lock socketLock;

  public ZeroMQClient(String omcExec) {
    this(omcExec, findLocale());
  }

  public ZeroMQClient(String omcExec, String locale) {
    super();
    portFileProvider = new ZMQPortFileProvider();
    this.omcExecutor = new OmcExecuter(omcExec, locale);
    this.socketLock = new ReentrantLock();
  }

  @Override
  public Result sendExpression(String expression) {
    if(!isConnected) {
      throw new IllegalStateException("ZMQ-Client not connected!");
    } else {
        socketLock.lock();
        String response;
        try {
            boolean success = socket.send(expression);
            response = socket.recvStr().trim();
        } finally { socketLock.unlock(); }
        log.debug("sendExpression: {} returned {}", expression, response);
        return new Result(response, getError());
    }
  }

  private Optional<String> getError() {
      socketLock.lock();
      String erg;
      try {
          boolean success = socket.send(GET_ERRORS);
          erg = socket.recvStr().trim();
      } finally { socketLock.unlock(); }
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
    socket.setLinger(0); // Dismisses pending messages if closed
    String portFilecontent = readPortFile();
    log.debug("port file content: {}", portFilecontent);
    boolean con = socket.connect(portFilecontent);
    log.debug("connecting to socket: {}", con);
    isConnected = true;
    call("loadModel", "Modelica");
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
