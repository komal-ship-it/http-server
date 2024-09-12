package http.server.response;

import http.server.Headers;
import http.server.request.RequestLine;

import java.util.List;
import java.util.Map;

public class Response {
  public ResponseLine responseLine;
  public Headers      headers;
  public byte[]       body;

  public Response(ResponseLine rs, Headers headers, byte[] body ){
    this.responseLine = rs;
    this.body = body;
    this.headers = headers;
  }

}


