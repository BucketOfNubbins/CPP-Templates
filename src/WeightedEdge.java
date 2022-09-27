public class WeightedEdge extends Edge {
    int weight;

    public WeightedEdge(Node u, Node v, int w, int i, boolean directed) {
        super(u, v, i, directed);
        weight = w;
    }
}
