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
            for (int j = 0; j < i; j++) {
                if (A[i] >= A[j] && maxLength[i] < maxLength[j] + 1) {
                    parent[i] = j;
                    maxLength[i] = maxLength[j] + 1;
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
}
