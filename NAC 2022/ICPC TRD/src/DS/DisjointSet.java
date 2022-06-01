public class DisjointSet {
    int[] parent;
    int[] rank;
    int components;

    public DisjointSet(int size) {
        parent = new int[size];
        rank = new int[size];
        components = size;
        for (int i = 0; i < size; i++) {
            parent[i] = i;
        }
    }

    public int find(int i) {
        return parent[i] == i ? i : (parent[i] = find(parent[i]));
    }

    public void union(int x, int y) {
        int px = find(x);
        int py = find(y);
        if (px == py) {
            return;
        }
        components--;
        if (rank[px] < rank[py]) {
            parent[px] = py;
        } else if (rank[px] > rank[py]) {
            parent[py] = px;
        } else {
            parent[px] = py;
            rank[px]++;
        }
    }
}
