
import java.net.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetUrl {
    
    public static void main(String[] args) throws MalformedURLException {
        System.out.println("hello from java net url");
        String i4 = "https://80.45.9.1:443";
        String i6 = "https://[fe80:a4b5::1]:443";
        URL url4 = new URL(i4);
        URL url6 = new URL(i6);
        System.out.println("java net port4 =" + url4.getPort());
        System.out.println("java net host4 = " + url4.getHost());
        System.out.println("java net port6 = " + url6.getPort());
        System.out.println("java net host6 = " + url6.getHost());
        
        String ipV6Pattern = "\\[([a-zA-Z0-9:]+)\\]:(\\d+)";
        String ipV4Pattern = "([a-zA-Z0-9.]+)\\:(\\d+)";
        List<String> ipList = new ArrayList<>();
        Pattern p6 = Pattern.compile( ipV6Pattern );
        Matcher m6 = p6.matcher( ip );
        Pattern p4 = Pattern.compile( ipV4Pattern );
        Matcher m4 = p4.matcher( ip );
        
        if( m4.matches() ) { 
            System.out.println("IPV4");
        } else if (m6.matches()) {
            System.out.println("IPV6");        
        }

        String uStr = "https://api-v3.mbta.com/predictions?filter[stop]=place-pktrm&filter[direction_id]=0&include=stop";
        URL url = new URL(uStr);
           // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           // conn.setRequestMethod("GET");
           // BufferedReader br = new BufferedReader(new InputStreamReader(
//                    (conn.getInputStream())));

  //          String jsonResponse;
    //        System.out.println("Output from Server .... \n");
     //       if ((jsonResponse = br.readLine()) != null) {
      //          System.out.println(jsonResponse);
        //    }

          //  JSONParser jsonParser = new JSONParser();
           // JSONObject obj = (JSONObject) jsonParser.parse(jsonResponse);
           // JSONArray dataObj = (JSONArray) obj.get("data");
            // System.out.println("\n\nd-attr = " + dataObj.size());

    }
}
