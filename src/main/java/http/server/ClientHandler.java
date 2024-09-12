package http.server;

import http.server.request.Decoder;
import http.server.request.Request;
import http.server.request.RequestLine;
import http.server.response.Encoder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

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
    }

    else if(requestLine.requestTarget.contains("/echo")){
      String echoedElem = requestLine.requestTarget.split("/")[2];
      // writing OK to client stream
      encoder.writeOkResponse(echoedElem, Encoder.PLAIN_CONTENT_TYPE_KEY);
    }

    else if(requestLine.requestTarget.contains("/user-agent")){
      if(!request.headers.headerMap.containsKey(Headers.USER_AGENT_HEADER)){
        System.out.println("Could not find user-agent header in request");
      }

      encoder.writeOkResponse(request.headers.headerMap.get(Headers.USER_AGENT_HEADER), Encoder.PLAIN_CONTENT_TYPE_KEY);
    }

    else if(requestLine.requestTarget.contains("/files")){
      String fileName = requestLine.requestTarget.split("/")[2];
      File file = new File(HttpServer.directoryName, fileName);
      if(file.exists()){
        String fileContent = Files.readString(file.toPath());
        encoder.writeOkResponse(fileContent, Encoder.OCTET_STREAM_CONTENT_TYPE_KEY);
      }else {
        encoder.write("404", "Not Found");
      }
    }

    else {
      // writing Not_found to client stream
      encoder.write("404", "Not Found");
    }
  }
}
