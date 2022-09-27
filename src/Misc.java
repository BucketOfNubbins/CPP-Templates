import java.math.BigInteger;
import java.util.*;

public class Misc {
    public static void main(String[] args) {
        int[] A = new int[]{3, 2, 6, 9, 1, 4, 5, 20, 3};
//        System.out.println(bisectRight(A, 5, A.length));
//        System.out.println(bisectLeft(A, 5, A.length));
        System.out.println(Arrays.toString(LIS(A)));
        System.out.println(Arrays.toString(fasterLIS(A)));
    }

    /**
     * O(n^2) implementation for longest monotonically increasing subsequence.
     *
     * @param A the array to process
     * @return an array containing the optimal sequence
     */
    private static int[] LIS(int[] A) {
        if (A.length == 0) {
            return new int[]{};
        }
        int[] maxLength = new int[A.length];
        int[] parent = new int[A.length];
        int maxIndex = 0;
        for (int i = 0; i < A.length; i++) {
            maxLength[i] = 1;
            parent[i] = -1;
            for (int j = i - 1; j >= 0; j--) {
                if (A[i] >= A[j] && maxLength[i] < maxLength[j] + 1) {
                    parent[i] = j;
                    maxLength[i] = maxLength[j] + 1;
                }
                if (maxLength[i] > j) {
                    break;
                }
            }
            if (maxLength[maxIndex] < maxLength[i]) {
                maxIndex = i;
            }
        }
        int[] sequence = new int[maxLength[maxIndex]];
        int cur = maxIndex;
        for (int i = maxLength[maxIndex] - 1; i >= 0; i--) {
            sequence[i] = A[cur];
            cur = parent[cur];
        }
        return sequence;
    }

    /**
     * O(n lg n) implementation for longest monotonically increasing subsequence.
     *
     * @param A the array to process
     * @return an array containing the optimal sequence
     */
    private static int[] fasterLIS(int[] A) {
        int[] partialSequence = new int[A.length]; // An optimal partial sequence, may not exist in A
        int[] indexes = new int[A.length]; // Index mapping of the partialSequence
        int[] parent = new int[A.length]; // parent pointer used to reconstruct sequence at the end
        int length = 0;
        for (int n = 0; n < A.length; n++) {
            int i = bisectRight(partialSequence, A[n], length); // Find optimal placement
            partialSequence[i] = A[n]; // place
            indexes[i] = n;
            if (i != 0) {
                parent[n] = indexes[i - 1]; // set parent to be previous in sequence
            }
            if (i == length) {
                length++; // increment length if increased
            }
        }
        int[] sequence = new int[length]; // recover sequence
        int cur = indexes[length - 1];
        for (int i = length - 1; i >= 0; i--) {
            sequence[i] = A[cur];
            cur = parent[cur];
        }
        return sequence;
    }

    /**
     * Binary search to find the highest index i which when k is inserted into A[i], A is still sorted
     *
     * @param A the array to insert k into
     * @param k k
     * @param s the size of A, since A might not be full
     * @return the index for insertion
     */
    private static int bisectRight(int[] A, int k, int s) {
        int lo = 0;
        int hi = s;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (A[mid] <= k) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Binary search to find the lowest index i which when k is inserted into A[i], A is still sorted
     *
     * @param A the array to insert k into
     * @param k k
     * @param s the size of A, since A might not be full
     * @return the index for insertion
     */
    public static int bisectLeft(int[] A, int k, int s) {
        int lo = 0;
        int hi = s; // for this problem, inclusive hi is beneficial
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (A[mid] < k) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    private static int ternarySearch(int[] A) {
        int lo = 0;
        int hi = A.length;
        while (lo < hi) {
            int lm = (hi - lo) / 3;
            int hm = lo + 2 * lm;
            lm += lo;
            int lv = A[lm];
            int hv = A[hm];
            if (lv < hv) {
                hi = hm;
            } else {
                lo = lm + 1;
            }
        }
        return lo;
    }

    public static BigInteger fib(int n) {
        BigInteger[][] matrix = new BigInteger[][]{{BigInteger.ONE, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ZERO}};
//        long[][] matrix = new long[][]{{1, 1}, {1, 0}};
        matrix = fastExpo(matrix, n);
        return matrix[1][0];
    }

    public static BigInteger[][] fastExpo(BigInteger[][] matrix, long n) {
        BigInteger[][] res = new BigInteger[matrix.length][matrix.length];
        for (int i = 0; i < res.length; i++) {
            Arrays.fill(res[i], BigInteger.ZERO);
            res[i][i] = BigInteger.ONE;
        }
        while (n != 0) {
            if ((n & 1) == 1) {
                res = multiply(res, matrix);
            }
            n >>= 1;
            matrix = multiply(matrix, matrix);
        }
        return res;
    }

    public static BigInteger[][] multiply(BigInteger[][] a, BigInteger[][] b) {
        BigInteger[][] x = new BigInteger[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int k = 0; k < b[0].length; k++) {
                x[i][k] = BigInteger.ZERO;
                for (int j = 0; j < a.length; j++) {
                    x[i][k] = x[i][k].add(a[i][j].multiply(b[j][k]));
                }
            }
        }
        return x;
    }

    public static int[][] multiply(int[][] a, int[][] b) {
        int[][] x = new int[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int k = 0; k < b[0].length; k++) {
                x[i][k] = 0;
                for (int j = 0; j < a.length; j++) {
                    x[i][k] += a[i][j] * b[j][k];
                }
            }
        }
        return x;
    }

    /**
     * Multiplies a range of matrices together from A based on the ordering provided by the matrix s.
     *
     * @param A the list of matrices
     * @param s the order of multiplications to perform
     * @param i the index of the first element in the range
     * @param j the index of the last element in the range
     * @return the resulting matrix
     */
    private static int[][] MatrixChainMultiply(int[][][] A, int[][] s, int i, int j) {
        if (i == j) {
            return A[i];
        }
        return multiply(MatrixChainMultiply(A, s, i, s[i][j]),
                MatrixChainMultiply(A, s, s[i][j] + 1, j));
    }


}
