package http.server.request;

import http.server.HTTPMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Decoder {

  public RequestLine parseRequest(InputStream inputStream){
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

      return new RequestLine(method, target, httpVersion);
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
