package http.server;

import http.server.request.Decoder;
import http.server.request.Request;
import http.server.request.RequestLine;
import http.server.response.Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static String HTTP_VERSION = "HTTP/1.1";
  public static String CLRF = "\r\n";

  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage

     try {
       ServerSocket serverSocket = new ServerSocket(4221);

       // Since the tester restarts your program quite often, setting SO_REUSEADDR
       // ensures that we don't run into 'Address already in use' errors
       serverSocket.setReuseAddress(true);

       while (true){
         Socket client = serverSocket.accept(); // Wait for connection from client.
         System.out.println("accepted new connection");

         new Thread(() -> {
           try {
             ClientHandler.handleClient(client);
           } catch (Exception ex){
             System.out.println("Exception while handling client" + ex.getMessage());
             throw new RuntimeException(ex);
           }
         }).start();
       }

     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
