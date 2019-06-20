import java.util.*;

class Pt {
        int x;
            int y;
                double dist;
                    Pt(int a, int b) {
                                x = a; y = b;
                                        dist = a*a + b*b;
                                            }
};

class NearSolution {
    public static int mark(int num, TreeMap<Double, Pt> tm) {
        Random randUtil = new Random();
        for(int i = 0; i < num+5; i++) {
            Pt a = new Pt(randUtil.nextInt(100), randUtil.nextInt(100));
            tm.put(a.dist, a);
            if(i >= num) {
                tm.remove(tm.lastKey());
            }
        }
        return 1;
    }

    public static void main(String[] args) {
        TreeMap<Double, Pt> tNear = new TreeMap<Double, Pt>();
        int maxSize = 10;
        Pt a4 = new Pt(3,2);
        tNear.put(a4.dist, a4);
        mark(maxSize, tNear);

        if(tNear.size() > maxSize) {
            tNear.remove(tNear.lastKey());
        }
        int count = 0;
        for(Map.Entry<Double, Pt> entry: tNear.entrySet()) {
            System.out.println(count++ + " x="+ entry.getValue().x + " y=" + entry.getValue().y);
        }
    }
};

