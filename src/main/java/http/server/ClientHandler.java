package http.server;

import http.server.request.Decoder;
import http.server.request.Request;
import http.server.request.RequestLine;
import http.server.response.Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler {

  public static void handleClient(Socket client) throws IOException {

    InputStream  clientInputStream  = client.getInputStream();
    Decoder      decoder            = new Decoder();
    OutputStream clientOutputStream = client.getOutputStream();
    Encoder      encoder            = new Encoder(clientOutputStream);

    if(client.isClosed()) return;
    Request request = decoder.parseRequest(clientInputStream);
    if(request == null) return;

    RequestLine requestLine = request.requestLine;
    if(requestLine.requestTarget.equals("/")){
      // writing OK to client stream
      encoder.write("200", "OK");
    } else if(requestLine.requestTarget.contains("/echo")){
      String echoedElem = requestLine.requestTarget.split("/")[2];
      // writing OK to client stream
      encoder.writeOkResponse(echoedElem);
    } else if(requestLine.requestTarget.contains("/user-agent")){
      if(!request.headers.headerMap.containsKey(Headers.USER_AGENT_HEADER)){
        System.out.println("Could not find user-agent header in request");
      }

      encoder.writeOkResponse(request.headers.headerMap.get(Headers.USER_AGENT_HEADER));
    } else {
      // writing Not_found to client stream
      encoder.write("404", "Not Found");
    }
  }
}
