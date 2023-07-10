import java.io.*;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * Implements a generic segment tree
 */
public class SegmentTreeGeneric<E> {

    // Combining with identity should do nothing
    private final E identity;

    // How to combine two segments
    // E combine(E left, E right) {}
    private final BinaryOperator<E> combine;

    // If the entire range {l, r} had the same value, what value should that segment have?
    // E rangeCombine(E value, int[] range) {}
    private final BiFunction<E, int[], E> rangeCombine;

    int size;
    Object[] values;
    Object[] updates;

    public SegmentTreeGeneric(int n, E i, BinaryOperator<E> c, BiFunction<E, int[], E> rc) {
        identity = i;
        combine = c;
        rangeCombine = rc;
        size = n;
        values = new Object[2 * n];
        Arrays.fill(values, identity);
        updates = new Object[2 * n];
//        Arrays.fill(updates, null); Default value is null
    }

    public SegmentTreeGeneric(E[] array, E i, BinaryOperator<E> c, BiFunction<E, int[], E> rc) {
        identity = i;
        combine = c;
        rangeCombine = rc;
        size = array.length;
        values = new Object[array.length * 2];
        updates = new Object[array.length * 2];
//        Arrays.fill(updates, null); Default value is null
        build(0, 0, array.length - 1, array);
    }

    private E build(int v, int l, int r, E[] array) {
        if (l == r) {
            values[v] = array[l];
        } else {
            values[v] = combine.apply(build(getLeft(v, l, r), l, mid(l, r), array),
                    build(getRight(v, l, r), mid(l, r) + 1, r, array));
        }
        return (E) values[v];
    }

    public E query(int l, int r) {
        return query(0, 0, size - 1, l, r);
    }

    private E query(int v, int l, int r, int ql, int qr) {
        if (ql > qr) {
            return identity;
        }
        if (l == ql && r == qr) {
            return (E) values[v];
        }
        push(v, l, r);
        int m = mid(l, r);
        E left = query(getLeft(v, l, r), l, m, ql, Math.min(m, qr));
        E right = query(getRight(v, l, r), m + 1, r, Math.max(ql, m + 1), qr);
        return combine.apply(left, right);
    }

    public void update(int l, int r, E value) {
        update(0, 0, size - 1, l, r, value);
    }

    private E update(int v, int l, int r, int ul, int ur, E value) {
        push(v, l, r);
        if (ul > ur) {
            return (E) values[v];
        } else if (l == r && ul == ur && l == ul) {
            return (E) (values[v] = value);
        } else if (l == ul && r == ur) {
            values[v] = rangeCombine.apply(value, new int[]{l, r});
            updates[v] = value;
            return (E) values[v];
        } else {
            int m = mid(l, r);
            E left = update(getLeft(v, l, r), l, m, ul, Math.min(ur, m), value);
            E right = update(getRight(v, l, r), m + 1, r, Math.max(m + 1, ul), ur, value);
            return (E) (values[v] = combine.apply(left, right));
        }
    }

    private void push(int v, int l, int r) {
        if (updates[v] != null) {
            E u = (E) updates[v];
            updates[v] = null;
            int m = mid(l, r);
            update(getLeft(v, l, r), l, m, l, Math.min(r, m), u);
            update(getRight(v, l, r), m + 1, r, Math.max(m + 1, l), r, u);
        }
    }

    private int mid(int l, int r) {
        return (l + r) / 2;
    }

    private int numNodesLeft(int l, int r) {
        return 2 * (mid(l, r) - l + 1) - 1;
    }

    private int getLeft(int v, int l, int r) {
        return v + 1;
    }

    private int getRight(int v, int l, int r) {
        return v + numNodesLeft(l, r) + 1;
    }


    /**
     * Example Use
     */
    static final Integer Identity = 0;
    static final BinaryOperator<Integer> Combine = (a, b) -> a + b;
    static final BiFunction<Integer, int[], Integer> RangeCombine = (I, A) -> (A[1] - A[0] + 1) * I;

    static final int Size = 20;
    static final int MaxValue = 20;
    static final int Operations = 10;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        Integer[] arr = new Integer[Size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * MaxValue);
        }
        SegmentTreeGeneric<Integer> a = new SegmentTreeGeneric<>(arr, Identity, Combine, RangeCombine);
        for (int i = 0; i < Operations; i++) {
            // randomly choose left and right bounds
            int l = (int) (Math.random() * arr.length);
            int r = l + (int) (Math.random() * (arr.length - l));
            if (Math.random() < .6) { // query or update
                int x = a.query(l, r);
                System.out.println(x);
            } else {
                int v = (int) (Math.random() * MaxValue);
                a.update(l, r, v);
            }
        }

        br.close();
        bw.close();
    }


}
