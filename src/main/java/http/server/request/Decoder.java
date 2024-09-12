package http.server.request;

import http.server.HTTPMethod;
import http.server.Headers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Decoder {

  public Request parseRequest(InputStream inputStream){
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    try {
      String input  = reader.readLine();
      if(input==null || input.isEmpty() || input.isBlank()){
        return null;
      }
      String[] requestLine = input.split(" ");
      if(requestLine.length < 3){
        System.out.println("Incorrect RequestLine parameters: " + requestLine);
        throw new RuntimeException();
      }
      HTTPMethod method = getMethod(requestLine[0]);
      String target = requestLine[1];
      String httpVersion = requestLine[2];


      String       header;
      HashMap<String, String> headerMap = new HashMap<>();
      Headers headers = new Headers(headerMap);

      while (!(header = reader.readLine()).isEmpty()){
        String[] headerContent = header.split(":");
        headerMap.put(headerContent[0], headerContent[1].trim());
      }

//      String message= "";
//      String temp;
//      if((temp = reader.readLine())!=null) message = temp;

//      BufferedReader bufferedReader =
//              new BufferedReader(new InputStreamReader(inputStream));
//
//      StringBuffer bodyBuffer = new StringBuffer();
//      while (bufferedReader.ready()) {
//        bodyBuffer.append((char)bufferedReader.read());
//      }
//      String body = bodyBuffer.toString();

      int contentLength =
              Integer.parseInt(headerMap.getOrDefault("Content-Length", "0"));
      char[] body = new char[contentLength];
      reader.read(body, 0, contentLength);
      String bodyContent = new String(body);
      System.out.println("body read " + bodyContent);

      return new Request(
              new RequestLine(method, target, httpVersion),
              headers,
              bodyContent.getBytes(StandardCharsets.UTF_8)
      );
    } catch (IOException e) {
      System.out.println("Exception while parsing request: " + e);
      throw new RuntimeException(e);
    }
  }

  public HTTPMethod getMethod(String requestCode) {
    if(requestCode.equalsIgnoreCase("get")) return HTTPMethod.GET;
    else if(requestCode.equalsIgnoreCase("post")) return HTTPMethod.POST;
    else throw new RuntimeException("Unsupported http method");
  }
}
