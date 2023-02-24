public class TravellingSalesPerson {
    static int[][] cache;

    /**
     * Gets the shortest path cost for a travelling sales person problem
     * @param graphMatrix [i][j] = cost for some node i to reach node j, must be full populated
     * @param s the start node
     * @return the min cost of a path that goes from the start, to every other node in the graph
     */
    static int tsp(int[][] graphMatrix, int s) {
        int fullSet = (1 << graphMatrix.length) - 1;
        cache = new int[1 << graphMatrix.length][graphMatrix.length];
        for (int i = 0; i < fullSet; i++) {
            for (int j = 0; j < graphMatrix.length; j++) {
                cache[i][j] = -1;
            }
        }
        for (int j = 0; j < graphMatrix.length; j++) {
            cache[fullSet][j] = graphMatrix[j][s];
        }
        return tsp(graphMatrix, 1 << s, s);
    }

    static int tsp(int[][] graphMatrix, int set, int node) {
        if (graphMatrix[set][node] == -1) {
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < graphMatrix.length; i++) {
                if ((set & (1 << i)) != 0) { // already visited some node i
                    continue;
                }
                int cost = graphMatrix[node][i] + tsp(graphMatrix, set | (1 << i), i);
                min = Math.min(min, cost);
            }
            graphMatrix[set][node] = min;
        }
        return graphMatrix[set][node];
    }
}
