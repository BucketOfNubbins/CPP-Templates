typedef struct MyDS {
    MyDS() {}
    MyDS operator+(const MyDS& right) {}
} SegTreeType;

struct SegmentTree {
    vector<SegTreeType> tree;
    int arrSize;
    SegTreeType ST_ZERO = SegTreeType();
    SegmentTree(vector<SegTreeType>& arr, int size): 
        tree(size*2, ST_ZERO), arrSize(size) {
        // Builds segment tree
        for (int i = 0; i < arrSize; i++)
            tree[arrSize + i] = arr[i];
        for (int i = arrSize - 1; i > 0; i--)
            tree[i] = tree[i << 1] + tree[i << 1 | 1];
    }
    // Update array position idx with val.
    void update(int idx, SegTreeType val) {
        for (tree[idx += arrSize] = val; idx /= 2;)
            tree[idx] = tree[idx << 1] + tree[idx << 1 | 1];
    }
    // Query the tree to find the answer for a range of [l, r] (inclusive)
    SegTreeType query(int l, int r) {
        SegTreeType lAns = ST_ZERO, rAns = ST_ZERO;
        for (l += arrSize, r += arrSize + 1; l < r; l/=2, r/=2) {
            if (l % 2) lAns = lAns + tree[l++];
            if (r % 2) rAns = tree[--r] + rAns;
        }
        return lAns + rAns;
    }
};