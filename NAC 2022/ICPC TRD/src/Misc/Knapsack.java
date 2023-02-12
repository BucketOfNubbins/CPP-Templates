// c - capacity of knapsack; n - number of items
private static List<Integer> knapSack(int[] values, int[] weights, int c, int n) {
    int[][] dp = new int[n][c + 1];
    boolean[][] used = new boolean[n][c + 1];
    for (int i = 0; i < weights[n - 1] && i <= c; i++) {
        dp[n - 1][i] = 0;
    }
    for (int i = weights[n - 1]; i <= c; i++) {
        used[n - 1][i] = true;
        dp[n - 1][i] = values[n - 1];
    }
    for (int i = n - 2; i >= 0; i--) {
        for (int j = 0; j < weights[i] && j <= c; j++) {
            dp[i][j] = dp[i + 1][j];
        }
        for (int j = weights[i]; j <= c; j++) {
            int a = values[i] + dp[i + 1][j - weights[i]];
            int b = dp[i + 1][j];
            dp[i][j] = Math.max(a, b);
            used[i][j] = a > b;
        }
    }
    List<Integer> items = new ArrayList<>();
    for (int i = 0; i < n && c > 0; i++) {
        if (used[i][c]) {
            items.add(i);
            c -= weights[i];
        }
    }
    return items;
}
  