package http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
  private final int             port;
  private final ExecutorService executorService;

  public static String directoryName = "";

  private final String DIRECTORY_ARGUMENT = "--directory";

  public HttpServer(int port, int concurrencyLevel, String[] args){
    this.port = port;
    this.executorService = Executors.newFixedThreadPool(concurrencyLevel);
    handleArguments(args);
  }

  private void handleArguments(String[] args){
    List<String> argList = Arrays.stream(args).toList();

    if(argList.contains(DIRECTORY_ARGUMENT)){
      this.directoryName = argList.get(argList.indexOf(DIRECTORY_ARGUMENT) + 1);
    }
  }

  public void run(){
    try {
      ServerSocket serverSocket = new ServerSocket(port);

      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);

      while (true){
        Socket client = serverSocket.accept(); // Wait for connection from client.
        System.out.println("accepted new connection");

        executorService.submit(() -> {
          try {
            ClientHandler.handleClient(client);
          } catch (IOException ex) {
            System.out.println("Exception while handling client" + ex.getMessage());
            throw new RuntimeException(ex);
          }
        });
      }

    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
