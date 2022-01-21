public class Edge {
    Node a;
    Node b;
    int id;

    public Edge(Node u, Node v, int i, boolean directed) {
        a = u;
        b = v;
        a.adj.add(this);
        if (!directed) {
            b.adj.add(this);
        }
        id = i;
    }

    // Undirected
    Node other(Node o) {
        return o == a ? b : a;
    }

    // Directed
    Node destination() {
        return b;
    }
}
