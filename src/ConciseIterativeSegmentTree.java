/**
 * A segment tree based on AI.Cash's implementation (<a href="https://codeforces.com/blog/entry/18051">...</a>)
 * <p>
 * Supports non-commutative operators.
 * <p>
 * Does not currently support lazy propagation, though it can be implemented.
 *
 * @see IterativeSegmentTree
 */
public class ConciseIterativeSegmentTree {
    private static final int IDENTITY = 0;

    private int combine(int a, int b) {
        return a + b;
    }

    int n;
    int[] tree;

    public ConciseIterativeSegmentTree(int n) {
        this.n = n;
        this.tree = new int[2 * n];
    }

    /**
     * Updates index i to x.
     * <p>
     * Can be modified to support other types of update, like increment, by changing the for loop initialization.
     *
     * @param i the index containing the element to be updated
     * @param x the value to update the element to
     */
    public void update(int i, int x) {
        for (tree[i += n] = x; i > 1; i >>= 1) {
            tree[i >> 1] = tree[i] + tree[i ^ 1];
        }
    }

    /**
     * Returns the combination of the range [l, r] (inclusive).
     *
     * @param l the left bound (inclusive)
     * @param r the right bound (inclusive)
     * @return the combination of the range [l, r] (inclusive)
     */
    public int query(int l, int r) {
        int resl = IDENTITY, resr = IDENTITY;
        for (l += n, r += n; l <= r; l >>= 1, r >>= 1) {
            if ((l & 1) == 1) { // if l is a right child
                resl = combine(resl, tree[l++]);
            }
            if ((r & 1) == 0) { // if r is a left child
                resr = combine(tree[r--], resr);
            }
        }
        return combine(resl, resr);
    }
}
