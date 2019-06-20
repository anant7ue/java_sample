import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

class FileTypeSize {
    public static void initType(ConcurrentHashMap<String, String> chm) {
        chm.put("mp3", "Music");
        chm.put("wav", "Music");
        chm.put("avi", "Video");
        chm.put("mp4", "Video");
        chm.put("zip", "File");
        chm.put("txt", "Other");
    }
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> nameType = new ConcurrentHashMap<String, String>();
        TreeMap<String, Integer> typeSize = new TreeMap<String, Integer>();
        initType(nameType);
        for(Map.Entry<String, String> entry : nameType.entrySet()) {
            if(!typeSize.containsKey(entry.getValue())) {
                typeSize.put(entry.getValue(), 0);
            }
        }
        String fileList="mymuisc.mp3 11kb myfile.zip 1111b myvideo.mp4 124b myvideo2.avi 450b mytextss.txt 23b";
        Scanner parse = new Scanner(fileList);
        while(parse.hasNext()) {
            String tokn = parse.next();
            Scanner suffix = new Scanner(tokn).useDelimiter("\\.");
            String sfx = suffix.next();
            sfx = suffix.next();
            String typ = nameType.get(sfx);
            System.out.println("tokn= " + tokn + " sfx= " +sfx);

            String toksz = parse.next().toLowerCase();
            int sz = 0;
            for(int i = 0; i < toksz.length(); i++) {
                if(Character.isDigit(toksz.charAt(i))) {
                    sz = sz*10 + Character.digit(toksz.charAt(i), 10);
                } else {
                    if(toksz.contains("kb")) {
                        sz *= 1000;
                    }
                    if(toksz.contains("mb")) {
                        sz *= 1000000;
                    }
                    break;
                }
            }
            int oldsz = typeSize.get(typ);
            typeSize.put(typ, sz+oldsz);

            System.out.println("tokn= " + tokn + " sz= " +sz);
        }

        for(Map.Entry<String, Integer> entry: typeSize.entrySet()) {
            System.out.println("type= " + entry.getKey() + "  sz=" + entry.getValue());
        }
        System.out.println("");
    }
};

