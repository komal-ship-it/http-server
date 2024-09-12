package http.server.response;

import http.server.HTTPMethod;

public class ResponseLine {
  public String     httpVersion;

  public String responseCode;

  public String responseReason;
  public ResponseLine(String httpVersion, String responseCode, String responseReason){
    this.httpVersion = httpVersion;
    this.responseCode = responseCode;
    this.responseReason = responseReason;
  }

  public String toString(){
    return httpVersion + " " + responseCode + " " + responseReason;
  }
}
