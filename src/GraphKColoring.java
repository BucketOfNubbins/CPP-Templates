public class GraphKColoring {
    private static int[] solveKColoring(int n, int[] adjLists, int limit) {
        int[] optimalSet = new int[1 << n];
        int[] subsetK = new int[1 << n];
        // Overall -> O(n * 2^n)
        for (int i = 0; i < (1 << n); i++) { // O(2^n)
            if (isValid(i, limit, adjLists)) {
                optimalSet[i] = i;
                subsetK[i] = i == 0? 0 : 1;
            } else {
                int bestK = Integer.MAX_VALUE;
                int bestSet = 0;
                int ci = i;
                while (ci != 0) { // O(set bits)
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

    private static boolean isValid(int set, int limit, int[] adjLists) {
        int setCopy = set;
        while (setCopy != 0) {
            int b = Integer.lowestOneBit(setCopy);
            setCopy ^= b;
            if ((set & adjLists[Integer.numberOfTrailingZeros(b)]) != 0) {
                return false;
            }
        }
        return Integer.bitCount(set) <= limit;
    }
}
