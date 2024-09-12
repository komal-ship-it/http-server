package http.server;

import http.server.request.Decoder;
import http.server.request.Request;
import http.server.request.RequestLine;
import http.server.response.Encoder;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.zip.GZIPOutputStream;

import static http.server.Headers.ACCEPT_ENCODING_HEADER;

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
    Headers headers = request.headers;
    Boolean isCompressed = false;
    if(headers.headerMap.containsKey(ACCEPT_ENCODING_HEADER) &&
            headers.headerMap.get(ACCEPT_ENCODING_HEADER).contains(Encoder.GZIP_CONTENT_ENCODING_VALUE)){
      isCompressed = true;
    }
    if(requestLine.requestTarget.equals("/")){
      // writing OK to client stream
      encoder.write("200", "OK");
    }

    else if(requestLine.requestTarget.contains("/echo")){
      String echoedElem = requestLine.requestTarget.split("/")[2];

      if(isCompressed){
        byte[] compressedData = compress(echoedElem);
        String GZIP_BASE = "HTTP/1.1 200 OK\r\nContent-Encoding: gzip\r\nContent-Type: text/plain\r\nContent-Length: ";
        byte[] base =
                (GZIP_BASE + compressedData.length + "\r\n\r\n").getBytes();
        clientOutputStream.write(base);
        clientOutputStream.write(compressedData);
        clientOutputStream.flush();


      } else {
        encoder.writeOkResponse(echoedElem, Encoder.PLAIN_CONTENT_TYPE_KEY, false);
      }
    }

    else if(requestLine.requestTarget.contains("/user-agent")){
      if(!request.headers.headerMap.containsKey(Headers.USER_AGENT_HEADER)){
        System.out.println("Could not find user-agent header in request");
      }

      encoder.writeOkResponse(request.headers.headerMap.get(Headers.USER_AGENT_HEADER), Encoder.PLAIN_CONTENT_TYPE_KEY, isCompressed);
    }

    else if(requestLine.requestTarget.contains("/files")){
      String fileName = requestLine.requestTarget.split("/")[2];
      File file = new File(HttpServer.directoryName, fileName);

      if(requestLine.httpMethod == HTTPMethod.POST){
        Boolean isCreated = file.createNewFile();
        System.out.println("New file created: " + isCreated);
        Files.write(file.toPath(), request.body);

        encoder.write("201", "Created");
      } else {

        if(file.exists()){
          String fileContent = Files.readString(file.toPath());
          encoder.writeOkResponse(fileContent, Encoder.OCTET_STREAM_CONTENT_TYPE_KEY, isCompressed);
        }else {
          encoder.write("404", "Not Found");
        }
      }

    }

    else {
      // writing Not_found to client stream
      encoder.write("404", "Not Found");
    }
  }

  private static byte[] compress(final String str) throws IOException {
    if ((str == null) || (str.length() == 0)) {
      return null;
    }
    ByteArrayOutputStream obj = new ByteArrayOutputStream();
    GZIPOutputStream gzip = new GZIPOutputStream(obj);
    gzip.write(str.getBytes("UTF-8"));
    gzip.flush();
    gzip.close();
    return obj.toByteArray();
  }
}
