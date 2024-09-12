package http.server.response;

import http.server.Headers;
import http.server.Main;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Encoder {

  public static String CONTENT_TYPE_KEY = "Content-Type";
  public static String CONTENT_LENGTH_KEY = "Content-Length";

  public static String CONTENT_ENCODING_KEY = "Content-Encoding";

  public static String PLAIN_CONTENT_TYPE_KEY = "text/plain";

  public static String OCTET_STREAM_CONTENT_TYPE_KEY = "application/octet-stream";

  public static String GZIP_CONTENT_ENCODING_VALUE = "gzip";


  OutputStream os;
  public Encoder(OutputStream os){
    this.os = os;
  }

  public void write(String str){
    try {
      os.write(str.getBytes(StandardCharsets.UTF_8));
      os.flush();
    } catch (IOException e) {
      System.out.println(String.format("Exception while writing %s: %s", str, e));
      throw new RuntimeException(e);
    }
  }

  public void write(String responseCode, String reason){
    try {
      String responseLine = Main.HTTP_VERSION + " " + responseCode + " " +  reason + Main.CLRF;
      String headers = Main.CLRF;
      String finalMsgToWrite = responseLine + headers;
      os.write(finalMsgToWrite.getBytes(StandardCharsets.UTF_8));
      os.flush();
    } catch (IOException e) {
      System.out.printf("Exception while writing responseCode: %s and reason: %s with exception %s%n", responseCode, reason, e);
      throw new RuntimeException(e);
    }
  }

  public void writeOkResponse(String msg, String contentType, Boolean isCompressed){
    ResponseLine responseLine= new ResponseLine(Main.HTTP_VERSION, "200", "OK");

    try {
      HashMap<String, String> headerMap = new HashMap<>();
      headerMap.put(CONTENT_TYPE_KEY, contentType);
      headerMap.put(CONTENT_LENGTH_KEY, String.valueOf(msg.getBytes(StandardCharsets.UTF_8).length));
      if(isCompressed){
        headerMap.put(CONTENT_ENCODING_KEY, GZIP_CONTENT_ENCODING_VALUE);
      }
      Headers headers = new Headers(headerMap);

      String responseMsg  = responseLine + Main.CLRF +
              headers +
              msg;
      os.write(responseMsg.getBytes(StandardCharsets.UTF_8));
      os.flush();
    } catch (IOException e) {
      System.out.printf("Exception while writing msg: %s with exception %s%n", msg, e);
      throw new RuntimeException(e);
    }

  }
}
