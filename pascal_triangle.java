import java.util.*;

class Pascal {
    public static List<List<Integer>> generate(int numRows) {
        List<List<Integer>> x = new Vector<List<Integer>>();
        System.out.println("lets pascal it");
        Vector<Integer> row1 = new Vector<Integer>();
        Vector<Integer> row2 = new Vector<Integer>();
        row1.add(1);
        row2.add(1);
        x.add(row1);
        for(int i = 0; i < numRows; i++) {
            int prev = 0;
            row1 = new Vector<Integer>(row2);
            row2.clear();
            for(int j = 0; j < row1.size(); j++) {
                row2.add(prev+row1.get(j));
                prev = row1.get(j);
            }
            row2.add(row1.get(row1.size()-1));
            System.out.println( row1.toString());
            x.add(row2);
        }
        return x;
    }
    public static void main(String[] args) {
        generate(5);
    }
};

