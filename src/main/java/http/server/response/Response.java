package http.server.response;

import http.server.Header;
import http.server.request.RequestLine;

import java.util.List;
import java.util.Map;

public class Response {
  public ResponseLine responseLine;
  public List<Header> headers;
  public byte[]       body;

  public Response(ResponseLine rs, List<Header> headers, byte[] body ){
    this.responseLine = rs;
    this.body = body;
    this.headers = headers;
  }

}


