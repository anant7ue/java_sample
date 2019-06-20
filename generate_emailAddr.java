 import java.util.*;
 import java.util.concurrent.*;

 class Solution {
     public String solution(String S, String C) {
         Scanner names = new Scanner(S);
         ConcurrentHashMap<String, Integer> nameFreq = new ConcurrentHashMap<String, Integer>();
         String emails = "";
         String entry = "";
         String entryName = "";
         String sfx = "@" + C.toLowerCase() + ".com";
         int count = 0;
         while(names.hasNext()) {
             count++;
             if(count != 1) {
                 emails += "; ";
             }
             String first = names.next().toLowerCase();
             String mid = names.next().toLowerCase();
             String last;

             /* As per description, last name can contain only english letters followed by single ; "
              *             Empty names not allowed as ";;" nor whitespaces following last name like "first last ;"*/

             if(mid.charAt(mid.length()-1) == ';') {
                 last = mid;
             } else {
                 last = names.next().toLowerCase();
             }
             if(last.contains("-")) {
                 int hyphen_index = last.indexOf('-');
                 String last_no_hyphen = last.substring(0, hyphen_index)+last.substring(hyphen_index+1);
                 last = last_no_hyphen;
             }
             int offset = last.length()-1;
             if(last.charAt(offset) != ';') {
                 offset += 1;
             }
             entryName = last.substring(0,offset) + "_" + first;
             int freq = 0;
             if(nameFreq.containsKey(entryName)) {
                 freq = nameFreq.get(entryName);
                 nameFreq.put(entryName, freq+1);

             } else {
                 nameFreq.put(entryName, 1);
             }
             if(freq != 0) {
                 entryName += Integer.toString(freq+1);
             }
             entry = entryName + sfx;
             emails += entry;
             System.out.println(entry);
         }
         return emails;
     }
 };
 /*
  * Example test:    ('John Doe; Peter Benjamin Parker; Mary Jane Watson-Parker; John Elvis Doe; John Evan Doe; Jane Doe; Peter Brian Parker', 'Example') 
  * Output:
  * doe_john@example.com; parker_peter@example.com; watsonparker_mary@example.com; doe_john2@example.com; doe_john3@example.com; doe_jane@example.com; parker_peter2@example.com
  *
  *
  *
  * Your test case:  ['John Doe; Peter Benjamin Parker; Mary Jane Watson-Parker-Doe; John Elvis Doe; John Evan Doe; Jane Doe; Peter Brian Parker', 'Example'] 
  * Output:
  * doe_john@example.com; parker_peter@example.com; watsonparkerdoe_mary@example.com; doe_john2@example.com; doe_john3@example.com; doe_jane@example.com; parker_peter2@example.com
  * Returned value: 'doe_john@example.com; parker_peter@example.com; watsonparkerdoe_mary@example.com; doe_john2@example.com; doe_john3@example.com; doe_jane@example.com; parker_peter2@example.com' 
  *
  * Your test case:  ['John Doe; John3 Doe2; John3 Doe; John1 Doe; Johnny Doe;', 'example'] 
  * Output:
  * doe_john@example.com; doe2_john3@example.com; doe_john3@example.com; doe_john1@example.com; doe_johnny@example.com
  * Returned value: 'doe_john@example.com; doe2_john3@example.com; doe_john3@example.com; doe_john1@example.com; doe_johnny@example.com' 
  *
  * Your test case:  ['', 'example'] 
  * Returned value: '' 
  *
  *
  * Your test case:  ['d j; d j; d j;', 'example'] 
  * Returned value: 'j_d@example.com; j_d2@example.com; j_d3@example.com' 
  *
  * */
