public class GraphKColoring {
    private static int[] solveKColoring(int n, int[] adjLists, int limit) {
        int[] optimalSet = new int[1 << n];
        int[] subsetK = new int[1 << n];
        // Overall -> O(n * 2^n)
        for (int i = 0; i < (1 << n); i++) { // O(2^n)
            if (isValid(n, i, limit, adjLists)) {
                optimalSet[i] = i;
                subsetK[i] = 1;
            } else {
                int bestK = Integer.MAX_VALUE;
                int bestSet = 0;
                int ci = i;
                while (ci != 0) { // O(set bits)
                    int b = ci & -ci;
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

    private static boolean isValid(int n, int set, int limit, int[] adjLists) {
        for (int i = 0; i < n; i++) {
            if (((1 << i) & set) != 0 && (set & adjLists[i]) != 0) {
                return false;
            }
        }
        return Integer.bitCount(set) <= limit;
    }
}
