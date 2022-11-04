public class Strings {
    private static void prefixFunc(String s, int[] pi) {
        int n = s.length();
        for (int i = 1; i < n; i++) {
            int j = pi[i - 1];
            while (j > 0 && s.charAt(i) != s.charAt(j))
                j = pi[j - 1];
            if (s.charAt(i) == s.charAt(j))
                j++;
            pi[i] = j;
        }
    }

    private static void zFunc(String s, int[] z) {
        int n = s.length();
        for (int i = 1, l = 0, r = 0; i < n; ++i) {
            if (i <= r)
                z[i] = Math.min(r - i + 1, z[i - l]);
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i]))
                ++z[i];
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
    }

    //  p[0][i] = half length of longest even palindrome around pos i, p[1][i] = longest odd (half rounded down).
    private static int[][] manacher(String s) {
        int n = s.length();
        int[][] p = new int[2][n + 1];
        for (int z = 0; z < 2; z++) {
            for (int i = 0, l = 0, r = 0; i < n; i++) {
                int t = r - i + 1 - z;
                if (i < r) {
                    p[z][i] = Math.min(t, p[z][l + t]);
                }
                int L = i - p[z][i], R = i + p[z][i] + 1 - z;
                while (L >= 1 && R + 1 < n && s.charAt(L - 1) == s.charAt(R + 1)) {
                    p[z][i]++;
                    L--;
                    R++;
                }
                if (R > r) {
                    l = L;
                    r = R;
                }
            }
        }
        return p;
    }
    
    // Booth's
    private static String leastRotation(String s) {
        s += s;
        int[] f = new int[s.length()];
        int k = 0;
        for (int j = 1; j < s.length(); j++) {
            int i = f[j - k - 1];
            while (i != -1 && s.charAt(j) != s.charAt(k + i + 1)) {
                if (s.charAt(j) < s.charAt(k + i + 1)) k = j - i - 1;
                i = f[i];
            }

            if (s.charAt(j) != s.charAt(k + i + 1)) {
                if (s.charAt(j) < s.charAt(k)) k = j;
                f[j - k] = -1;
            } else f[j - k] = i + 1;
        }
        return s.substring(k, k + s.length() / 2);
    }
}
