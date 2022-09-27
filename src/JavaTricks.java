import java.util.*;

public class JavaTricks {

    private static void sort(int[][] list) {
        Arrays.sort(list, Comparator.comparingInt(o -> o[0]));
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o->o[0]));


    }


}
