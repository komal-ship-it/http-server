package http.server.request;

import java.util.Map;

public class Request {
  RequestLine         requestLine;
  Map<String, String> headers;
  byte[]              body;
}


