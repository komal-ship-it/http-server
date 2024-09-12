package http.server;

public class Header {
  String key;
  String val;

  public Header(String k, String v){
    this.key = k;
    this.val = v;
  }

  public String toString(){
    return key + ":" + val + Main.CLRF;
  }
}
