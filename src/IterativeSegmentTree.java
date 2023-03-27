/**
 * A segment tree based on AI.Cash's implementation (<a href="https://codeforces.com/blog/entry/18051">...</a>)
 * <p>
 * Supports non-commutative operators.
 * <p>
 * Does not currently support lazy propagation, though it can be implemented.
 *
 * @see ConciseIterativeSegmentTree
 */
public class IterativeSegmentTree {
    private static final int IDENTITY = 0;

    private int combine(int a, int b) {
        return a + b;
    }

    int n;
    int[] tree;

    public IterativeSegmentTree(int n) {
        this.n = n;
        this.tree = new int[2 * n];
    }

    private int parent(int n) {
        return n / 2;
    }

    private int left(int n) {
        return 2 * n;
    }

    private int right(int n) {
        return 2 * n + 1;
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
        // set leaf
        i += n;
        tree[i] = x;

        i = parent(i);

        while (i > 0) { // while i is not null (segment tree is 1-indexed)
            tree[i] = combine(tree[left(i)], tree[right(i)]);
            i = parent(i);
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
        // start by considering leaves
        l += n;
        r += n;

        int resl = IDENTITY, resr = IDENTITY;
        while (l <= r) { // while query interval still larger than segment
            if ((l & 1) == 1) { // if l is a right child
                resl = combine(resl, tree[l]);
                ++l;
            }
            if ((r & 1) == 0) { // if r is a left child
                resr = combine(tree[r], resr);
                --r;
            }

            l = parent(l);
            r = parent(r);
        }

        return combine(resl, resr);
    }
}
