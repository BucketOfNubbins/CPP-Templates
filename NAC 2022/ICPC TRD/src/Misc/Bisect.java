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

public static int bisectLeft(int[] A, int k, int s) {
    int lo = 0;
    int hi = s;
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
    