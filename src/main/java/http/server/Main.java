package http.server;

import http.server.request.Decoder;
import http.server.request.RequestLine;

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

       Socket client = serverSocket.accept(); // Wait for connection from client.

       InputStream  clientInputStream  = client.getInputStream();
       Decoder decoder = new Decoder();
       OutputStream clientOutputStream = client.getOutputStream();
       Writer       writer             = new Writer(clientOutputStream);

       if(client.isClosed()) return;
       RequestLine requestLine = decoder.parseRequest(clientInputStream);
       if(requestLine == null) return;
       if(requestLine.requestTarget.equals("/")){
         // writing OK to client stream
         writer.write("200", "OK");
       } else {
         // writing Not_found to client stream
         writer.write("404", "Not Found");
       }

       System.out.println("accepted new connection");
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
