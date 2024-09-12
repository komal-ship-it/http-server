package http.server.request;

import http.server.Headers;
import http.server.response.ResponseLine;

import java.util.List;
import java.util.Map;

public class Request {
  public RequestLine requestLine;
  public Headers     headers;
  public byte[]      body;

  public Request(RequestLine rs, Headers headers, byte[] body ){
    this.requestLine = rs;
    this.body = body;
    this.headers = headers;
  }
}


