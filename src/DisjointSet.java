public class DisjointSet {
    int[] parent;
    int[] rank;
    int[] sizes;
    int components;

    public DisjointSet(int size) {
        parent = new int[size];
        rank = new int[size];
        sizes = new int[size];
        components = size;
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            sizes[i] = 1;
        }
    }

    public int find(int i) {
        return parent[i] == i ? i : (parent[i] = find(parent[i]));
    }

    public int getSize(int i) {
        return sizes[find(i)];
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
            sizes[py] += sizes[px];
        } else if (rank[px] > rank[py]) {
            parent[py] = px;
            sizes[px] += sizes[py];
        } else {
            parent[px] = py;
            sizes[py] += sizes[px];
            rank[px]++;
        }
    }
}
