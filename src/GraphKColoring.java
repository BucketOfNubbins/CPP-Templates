import java.io.*;
import java.util.HashMap;

public class GraphKColoring {
    // nextBinary, indexToBitPattern, and bitPatternToIndex in NumberTheory can be useful here

    private static int[] solveKColoring(int n, int[] adjLists) {
        int[] optimalSet = new int[1 << n];
        int[] subsetK = new int[1 << n];
        // Overall -> O(n^2 * 2^n)
        for (int i = 0; i < (1 << n); i++) { // O(2^n) iterations
            if (isValid(i, adjLists)) { // O(n^2), though effectively O(n)
                optimalSet[i] = i;
                subsetK[i] = i == 0 ? 0 : 1;
            } else {
                int bestK = Integer.MAX_VALUE;
                int bestSet = 0;
                int ci = i;
                while (ci != 0) { // O(n)
                    int b = Integer.lowestOneBit(ci);
                    ci ^= b;
                    int set = optimalSet[i ^ b];
                    int k = subsetK[set] + subsetK[i ^ set];
                    if (k < bestK) {
                        bestK = k;
                        bestSet = set;
                    }
                }
                optimalSet[i] = bestSet;
                subsetK[i] = bestK;
            }
        }
        return optimalSet;
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
