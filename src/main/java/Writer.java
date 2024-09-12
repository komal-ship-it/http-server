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
}
