// Needs bisect right
private static int[] LIS(int[] A) {
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