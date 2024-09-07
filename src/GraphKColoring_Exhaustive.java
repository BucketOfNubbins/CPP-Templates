import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphKColoring_Exhaustive {
    private static int nextBinary(int x) { // Could be useful
        int k = Integer.bitCount(x);
        x += (x & -x);
        int fillCount = k - Integer.bitCount(x);
        x |= (1 << fillCount) - 1;
        return x;
    }

    /*
    Graph Coloring can be reduced to finding the shortest path in a DAG, however the size of the DAG is 2^n nodes.
    Calculating the edges of the DAG is also tricky.
     */
    private static int[] solveKColoring(int n, int[] adjLists) {
        int[] optimalSet = new int[1 << n];
        int[] subsetK = new int[1 << n];
        List<Integer> independentSets = new ArrayList<>();
        for (int size = 1; size <= n; size++) {
            int i = (1 << size) - 1;
            while (i < (1 << n)) {
                if (isValid(i, adjLists)) {
                    optimalSet[i] = i;
                    subsetK[i] = i == 0 ? 0 : 1;
                    if (i != 0) {
                        independentSets.add(i);
                    }
                } else {
                    int bestK = Integer.MAX_VALUE;
                    int bestSet = 0;
                    for (int candidate : independentSets) {
                        if (!isSubset(i, candidate)) {
                            continue;
                        }
                        int k = 1 + subsetK[i - candidate];
                        if (k < bestK) {
                            bestK = k;
                            bestSet = candidate;
                        }
                    }
                    optimalSet[i] = bestSet;
                    subsetK[i] = bestK;
                }
                i = nextBinary(i);
            }

        }
        return optimalSet;
    }

    private static boolean isSubset(int large, int small) {
        return (~large & small) == 0;
    }

    private static boolean isValid(int set, int[] adjLists) {
        int setCopy = set;
        while (setCopy != 0) { // O(n) iterations
            int b = Integer.lowestOneBit(setCopy);
            setCopy ^= b; // O(n), but is effectively O(1) as long as n <= max bits in an integer
            if ((set & adjLists[Integer.numberOfTrailingZeros(b)]) != 0) { // O(n), but effectively O(1)
                return false;
            }
        }
        return Integer.bitCount(set) <= capacity;
    }

    private static int capacity;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        String[] input = br.readLine().split(" ");

        int n = Integer.parseInt(input[0]);
        int e = Integer.parseInt(input[1]);
        capacity = Integer.parseInt(input[2]);

        String[] names = new String[n];
        HashMap<String, Integer> nameMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            names[i] = br.readLine();
            nameMap.put(names[i], i);
        }

        int[] adjLists = new int[n];
        for (int i = 0; i < e; i++) {
            input = br.readLine().split(" ");
            int u = nameMap.get(input[0]);
            int v = nameMap.get(input[1]);
            adjLists[u] |= 1 << v;
            adjLists[v] |= 1 << u;
        }

        int[] optimalSet = solveKColoring(n, adjLists);

        int fullSet = (1 << n) - 1;
        int setCopy = fullSet;

        int k = 0;

        StringBuilder sets = new StringBuilder();
        while (setCopy != 0) {
            int os = optimalSet[setCopy];
            setCopy ^= os;
            while (os != 0) {
                int b = os & -os;
                os ^= b;
                sets.append(names[Integer.numberOfTrailingZeros(b)]);
                if (os != 0) {
                    sets.append(" ");
                }
            }
            sets.append("\n");
            k++;
        }
        System.out.println(k);
        System.out.print(sets);

        br.close();
        bw.close();
    }
}
