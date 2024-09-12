package http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Writer {

  OutputStream os;
  public Writer(OutputStream os){
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
}
