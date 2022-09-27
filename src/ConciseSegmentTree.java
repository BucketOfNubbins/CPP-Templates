/**
 * Implements a segment tree for range min queries
 */
public class ConciseSegmentTree {
    int[] tree;
    Integer[] updates;
    int size;

    // CHANGE THIS DEPENDING ON GOAL OF SEGMENT TREE (Min, Max, Sum, Product, gcd, dp, etc.)
    int identity = Integer.MAX_VALUE;

    // CHANGE THIS DEPENDING ON GOAL OF SEGMENT TREE
    private int func(int leftValue, int rightValue) {
        return Math.min(leftValue, rightValue);
    }

    // CHANGE THIS DEPENDING ON GOAL OF SEGMENT TREE
    private int updateRangeFunc(int updateValue, int previousValue, int leftBound, int rightBound) {
        return updateValue;
    }

    public ConciseSegmentTree(int[] array) {
        tree = new int[array.length * 2 - 1];
        updates = new Integer[array.length * 2 - 1];
        size = array.length;
        build(array, 0, 0, array.length - 1);
    }

    private int build(int[] array, int index, int left, int right) {
        if (left == right) {
            tree[index] = array[left];
        } else {
            int m = (left + right) / 2;
            int leftChild = build(array, getLeft(index, left, m), left, m);
            int rightChild = build(array, getRight(index, left, m), m + 1, right);
            tree[index] = func(leftChild, rightChild);
        }
        return tree[index];
    }

    public void update(int updateValue, int updateLeft, int updateRight) {
        update(updateValue, 0, 0, size - 1, updateLeft, updateRight);
    }

    private void update(int updateValue, int index, int treeLeft, int treeRight, int updateLeft, int updateRight) {
        if (treeLeft == updateLeft && treeRight == updateRight) {
            tree[index] = updateRangeFunc(updateValue, tree[index], treeLeft, treeRight);
            updates[index] = updateValue;
        } else if (updateLeft > updateRight) {
            return;
        } else {
            int middle = (treeLeft + treeRight) / 2;
            update(updates[index], getLeft(index, treeLeft, middle), treeLeft, middle, updateLeft, Math.min(updateRight, middle));
            update(updates[index], getRight(index, treeLeft, middle), middle + 1, treeRight, Math.max(updateLeft, middle + 1), updateRight);
        }
    }

    // Lazy Propagation - push update 1 layer down
    private void push(int index, int left, int right) {
        if (updates[index] != null) {
            int middle = (left + right) / 2;
            update(updates[index], getLeft(index, left, middle), left, middle, left, middle);
            update(updates[index], getRight(index, left, middle), middle + 1, right, middle + 1, right);
            updates[index] = null;
        }
    }

    public int getQuery(int queryLeft, int queryRight) {
        return getQuery(0, 0, size - 1, queryLeft, queryRight);
    }

    private int getQuery(int index, int treeLeft, int treeRight, int queryLeft, int queryRight) {
        if (treeLeft == queryLeft && treeRight == queryRight) {
            return tree[index];
        } else if (queryLeft > queryRight) {
            return identity;
        } else {
            push(index, treeLeft, treeRight);
            int middle = (treeLeft + treeRight) / 2;
            int leftChild = getQuery(getLeft(index, treeLeft, middle), treeLeft, middle, queryLeft, Math.min(queryRight, middle));
            int rightChild = getQuery(getRight(index, treeLeft, middle), middle + 1, treeRight, Math.max(queryLeft, middle + 1), queryRight);
            return func(leftChild, rightChild);
        }
    }

    private int getLeft(int index, int left, int middle) {
        return index + 1;
    }

    private int getRight(int index, int left, int middle) {
        return index + 2 * (middle - left + 1);
    }
}
