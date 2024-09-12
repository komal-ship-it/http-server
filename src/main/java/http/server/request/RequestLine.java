package http.server.request;

import http.server.HTTPMethod;

public class RequestLine {
  public HTTPMethod httpMethod;
  public String     requestTarget;
  public String     httpVersion;
  public RequestLine(HTTPMethod method, String target, String httpVersion){
    this.httpMethod = method;
    this.requestTarget = target;
    this.httpVersion = httpVersion;
  }
}
