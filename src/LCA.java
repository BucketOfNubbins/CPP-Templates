import java.util.ArrayList;
import java.util.List;

public class LCA {
    // LCA
    int[] height;
    List<Integer> euler;
    int[] first;
    int[] segTree;
    boolean[] visited;
    int n;

    LCA(List<Integer>[] adj, int root) {
        n = adj.length;
        height = new int[n];
        first = new int[n];
        euler = new ArrayList<>(2 * n);
        visited = new boolean[n];
        dfs(adj, root, 0);
        int m = euler.size();
        segTree = new int[m * 4];
        build(1, 0, m - 1);
    }

    void dfs(List<Integer>[] adj, int node, int h) {
        visited[node] = true;
        height[node] = h;
        first[node] = euler.size();
        euler.add(node);
        for (int to : adj[node]) {
            if (!visited[to]) {
                dfs(adj, to, h + 1);
                euler.add(node);
            }
        }
    }

    void build(int node, int b, int e) {
        if (b == e) {
            segTree[node] = euler.get(b);
        } else {
            int mid = (b + e) / 2;
            build(node << 1, b, mid);
            build(node << 1 | 1, mid + 1, e);
            int l = segTree[node << 1], r = segTree[node << 1 | 1];
            segTree[node] = (height[l] < height[r]) ? l : r;
        }
    }

    int query(int node, int b, int e, int L, int R) {
        if (b > R || e < L)
            return -1;
        if (b >= L && e <= R)
            return segTree[node];
        int mid = (b + e) >> 1;

        int left = query(node << 1, b, mid, L, R);
        int right = query(node << 1 | 1, mid + 1, e, L, R);
        if (left == -1) return right;
        if (right == -1) return left;
        return height[left] < height[right] ? left : right;
    }

    int lca(int u, int v) {
        int left = first[u], right = first[v];
        if (left > right) {
            int t = left;
            left = right;
            right = t;
        }
        return query(1, 0, euler.size() - 1, left, right);
    }
}