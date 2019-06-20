import java.util.*;

class MeetRoom {
    public static void fillSlots(TreeMap<Integer, Integer> timeline, int num) {
        Random rTimer = new Random();
        int t1 = rTimer.nextInt(50);
        int t2 = t1 + rTimer.nextInt(50);
        for(int i = 0; i < num; i++) {
            t1 = rTimer.nextInt(50);
            t2 = t1 + rTimer.nextInt(50);
            int val = 0;
            if(timeline.containsKey(t1)) {
                val = timeline.get(t1);
            }
            timeline.put(t1, 1 + val);
            val = 0;
            if(timeline.containsKey(t2)) {
                val = timeline.get(t2);
            }
            timeline.put(t2, -1 + val);
            System.out.println("count min num meeting rooms b/w t1,t2 e.g."+ t1 + " " +t2);
        }
    }
    public static void main(String[] args) {
        TreeMap<Integer, Integer> linear = new TreeMap<Integer, Integer>();
        fillSlots( linear, 10);
        int sum, max = 0;
        sum = 0;
        int t0 = 0;
        for(Map.Entry<Integer, Integer> entry : linear.entrySet()) {
            sum += entry.getValue();
            if(sum > max) {
                max = sum;
                t0 = entry.getKey();
            }
        }
        System.out.println("count min num meeting rooms " + max + " @t= " +t0);
    }
};


