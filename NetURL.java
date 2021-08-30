
import java.net.*;
import java.io.*;

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
        Pattern p6 = Pattern.compile( ipV6Pattern );
        Matcher m6 = p6.matcher( ip );
        Pattern p4 = Pattern.compile( ipV4Pattern );
        Matcher m4 = p4.matcher( ip );
        
        if( m4.matches() || m6.matches()) {
        }

    }
}
