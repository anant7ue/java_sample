import java.util.*;

class Spiral {

    public static int assign(int[][] arr, int d1, int d2) {
        int count = 0;
        int max = d1 * d2;
        int i, j = -1;
        int dir = 1;
        for(i = 0; count < max; count++) {
            if(dir == 1) {
                if(((j+1) < d2)&&(arr[i][j+1] == 0)) {
                    arr[i][j+1] = count+1;
                    j++;
                    continue;
                } else {
                    dir = 2;
                }
            }
            if(dir == 2) {
                if(((i+1) < d1)&&(arr[i+1][j] == 0)) {
                    arr[i+1][j] = count+1;
                    i++;
                    continue;
                } else {
                    dir = 3;
                }
            }
            if(dir == 3) {
                if(((j-1) >= 0)&&(arr[i][j-1] == 0)) {
                    arr[i][j-1] = count+1;
                    j--;
                    continue;
                } else {
                    dir = 4;
                }
            }
            if(dir == 4) {
                if(((i-1) >= 0)&&(arr[i-1][j] == 0)) {
                    arr[i-1][j] = count+1;
                    i--;
                    continue;
                } else {
                    count -= 1;
                    dir = 1;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int dim1 = 10;
        int dim2 = 10;
        int[][] fill = new int[dim1][dim2];
        for(int i = 0; i < dim1; i++) {
            for(int j = 0; j < dim2; j++) {
                fill[i][j] = 0;
            }
        }
        System.out.println("lets count spiral in a 2dimensional array\n");
        assign(fill, dim1, dim2);
        for(int i = 0; i < dim1; i++) {
            for(int j = 0; j < dim2; j++) {
                System.out.print( fill[i][j] +" ");
            }
            System.out.println(" ");
        }
    }
};

