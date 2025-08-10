import java.io.*;
import java.util.HashMap;

public class GraphKColoringAlt {
    // Alt Graph Coloring takes advantage of the following facts.
    /*
    If, and only if, a graph G has at least 3 nodes and all induced subgraphs of G are one colorable, then G is one
    colorable.
    This can be proven easily as it entirely relies on the presence of any edge in G. If there is an edge E in G, then G
     is not one colorable and there is an induced subgraph consisting of E and its two corresponding nodes.
     The above fact can be further leveraged to prove that a graph G, with more than 2 nodes, is one colorable if and
     only if all of its induced subgraphs with exactly one less node are also one colorable.
     */
    /*
    All of that means there is a marginal improvement in performance, though only by a constant factor. (More testing
     needed to see if it is actually an improvement.)
     */

    private static int[] solveKColoring(int n, int[] adjLists) {
        int[] optimalSet = new int[1 << n];
        int[] subsetK = new int[1 << n];
        // Overall -> O(n^2 * 2^n)
        for (int i = 0; i < (1 << n); i++) { // O(2^n) iterations
            switch (Integer.bitCount(i)) {
                case 0:
                    optimalSet[i] = 0;
                    subsetK[i] = 0;
                    break;
                case 1:
                    optimalSet[i] = i;
                    subsetK[i] = 1;
                    break;
                case 2:
                    int u = Integer.lowestOneBit(i);
                    int v = i ^ u;
                    int aIndex = Integer.numberOfTrailingZeros(u);
                    if ((adjLists[aIndex] & v) != 0 || capacity == 1) { // u and v are adjacent
                        optimalSet[i] = u;
                        subsetK[i] = 2;
                    } else { // a and b are not adjacent
                        optimalSet[i] = i;
                        subsetK[i] = 1;
                    }
                    break;
                default:
                    int countOneColorable = 0;
                    int bestK = Integer.MAX_VALUE;
                    int bestSet = 0;
                    int ic = i;
                    while (ic != 0) { // O(n)
                        int b = Integer.lowestOneBit(ic);
                        ic ^= b;
                        int ib = i ^ b;
                        int set = optimalSet[ib];
                        int k = subsetK[set] + subsetK[i ^ set];
                        if (subsetK[ib] == 1) {
                            countOneColorable++;
                        }
                        if (k < bestK) {
                            bestK = k;
                            bestSet = set;
                        }
                    }
                    if (countOneColorable == Integer.bitCount(i) && Integer.bitCount(i) <= capacity) {
                        optimalSet[i] = i;
                        subsetK[i] = 1;
                    } else {
                        optimalSet[i] = bestSet;
                        subsetK[i] = bestK;
                    }
            }
        }
        return optimalSet;
    }

    private static int capacity;

    // Solves the kattis problem 'Bus Planning'
    // https://open.kattis.com/problems/busplanning
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
