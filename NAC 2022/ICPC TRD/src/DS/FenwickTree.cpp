/* FenwickTree(int numElems): Fenwick/Binary Indexed Tree. 0-indexed inputs to functions. */
struct FenwickTree {
    vector<ll> data;
    int size;
    FenwickTree(int n) : data(n+1, 0), size(n+1) {}
    ll getSum(int currIdx) const {
        ll sum = 0; ++currIdx;
        while (currIdx > 0) {
            sum += data[currIdx];
            currIdx -= currIdx & (-currIdx);
        }
        return sum;
    }
    void update(int currIdx, ll delta) {
        ++currIdx;
        while (currIdx < size) {
            data[currIdx] += delta;
            currIdx += currIdx & (-currIdx);
        }
    }
};