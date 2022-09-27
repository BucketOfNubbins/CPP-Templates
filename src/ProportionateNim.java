import java.util.*;

public class ProportionateNim {

    private static HashMap<List<Integer>, Integer> grundy = new HashMap<>();
    private static HashMap<List<Integer>, HashSet<List<Integer>>> reachableStates = new HashMap<>();

    public static void main(String[] args) {
        grundy.put(makeState(0, 0), 0);
        HashSet<Integer> set = new HashSet<>();
        int x = 1;
//        for (int i = 0; x <= 100; i++) {
//            int v = function(i);
//            if (v != fastF(i)) {
//                System.out.println("Error: " + i);
//            }
//            if (!set.contains(v)) {
//                set.add(v);
//                System.out.printf("%2d: %4d\n", x++, function(i));
//
//            }
//            System.out.printf("%2d: %4d\n", i, function(i));
//        }
        System.out.println(fastF(50000));

    }

    private static long fastF(int x) {
        boolean[][] table = new boolean[x + 1][x + 1];
        for (int i = 0; i < x + 1; i++) {
            Arrays.fill(table[i], true);
        }
        long sum = 0;
        for (int s = 0; s <= x; s++) {
            for (int n = 0; n <= s >> 1; n++) {
                int m = s - n;
                if (table[n][m]) {
                    System.out.printf("[%4d, %4d]\n", n, m);
                    sum += n + m;
                    for (int k = 0; n + k <= x; k++) {
                        int i, j;
                        i = n + k;
                        j = m;
                        table[Math.min(i, j)][Math.max(i, j)] = false;
                        if (m + k <= x) {
                            i = n;
                            j = m + k;
                            table[Math.min(i, j)][Math.max(i, j)] = false;
                            i = n + k;
                            j = m + k;
                            table[Math.min(i, j)][Math.max(i, j)] = false;
                        }
                        if (2 * k + n <= x && m + k <= x) {
                            i = 2 * k + n;
                            j = k + m;
                            table[Math.min(i, j)][Math.max(i, j)] = false;
                        }
                        if (n + k <= x && m + 2 * k <= x) {
                            i = k + n;
                            j = 2 * k + m;
                            table[Math.min(i, j)][Math.max(i, j)] = false;
                        }
                    }
                }
            }
        }
        return sum;
    }

    private static int function(int x) {
        int sum = 0;
        for (int m = 0; m <= x; m++) {
            for (int n = 0; n <= m && n + m <= x; n++) {
                if (getGrundy(makeState(n, m)) == 0) {
                    sum += n + m;
//                    System.out.println("[" + n + ", " + m + "]");
                }
            }
        }
        return sum;
    }

    private static List<Integer> makeState(int a, int b) {
        List<Integer> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        Collections.sort(list);
        return list;
    }

    private static int getGrundy(List<Integer> state) {
        if (!grundy.containsKey(state)) {
            TreeSet<Integer> reachableGrundy = new TreeSet<>();
            HashSet<List<Integer>> reachableStates = getStates(state);
            for (List<Integer> s : reachableStates) {
                reachableGrundy.add(getGrundy(s));
            }
            for (int i = 0; ; i++) {
                if (!reachableGrundy.contains(i)) {
                    grundy.put(state, i);
                    break;
                }
            }
        }
        return grundy.get(state);
    }

    private static HashSet<List<Integer>> getStates(List<Integer> state) {
        if (!reachableStates.containsKey(state)) {
            reachableStates.put(state, new HashSet<>());
            // subtract from first pile
            for (int n = 1; n <= state.get(0); n++) {
                reachableStates.get(state).add(makeState(state.get(0) - n, state.get(1)));
            }
            // subtract from second pile
            for (int n = 1; n <= state.get(1); n++) {
                List<Integer> arr = makeState(state.get(0), state.get(1) - n);
                reachableStates.get(state).add(arr);
            }
            // subtract from both
            for (int n = 1; n <= state.get(0); n++) {
                reachableStates.get(state).add(makeState(state.get(0) - n, state.get(1) - n));
            }
            // subtract n from first, and 2n from second
            for (int n = 1; n <= state.get(0) && 2 * n <= state.get(1); n++) {
                List<Integer> arr = makeState(state.get(0) - n, state.get(1) - 2 * n);
                reachableStates.get(state).add(arr);
            }
            // subtract 2n from first, and n from second
            for (int n = 1; 2 * n <= state.get(0) && n <= state.get(1); n++) {
                reachableStates.get(state).add(makeState(state.get(0) - 2 * n, state.get(1) - n));
            }
        }
        return reachableStates.get(state);
    }


}
