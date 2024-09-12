package http.server;

import java.util.HashMap;
import java.util.Map;

public class Headers {
  public static String USER_AGENT_HEADER = "User-Agent";
  HashMap<String, String > headerMap;

  public Headers(HashMap<String, String > headerMap){
    this.headerMap = headerMap;
  }

  public String toString(){
    StringBuilder str = new StringBuilder();
    for(Map.Entry<String, String> entry: headerMap.entrySet()){
      str.append(entry.getKey() + ":" + entry.getValue() + Main.CLRF);
    }

    return str.toString() + Main.CLRF;
  }
}
