import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class SCComponents {
    int[] indexes;
    int[] lowlink;
    boolean[] onStack;
    int index;
    Stack<Node> s;
    List<List<Node>> SCCs; // List of Strongly Connected Components

    // Tarjan's Algorithm, which has the additional benefit of reverse topological sorting the SCCs
    public List<List<Node>> findComponents(Node[] nodes) {
        indexes = new int[nodes.length];
        lowlink = new int[nodes.length];
        onStack = new boolean[nodes.length];
        SCCs = new ArrayList<>();

        Arrays.fill(indexes, -1);
        Arrays.fill(lowlink, -1);
        Arrays.fill(onStack, false);

        index = 0;
        s = new Stack<>();
        for (Node n : nodes) {
            if (indexes[n.id] == -1) {
                strongConnect(n);
            }
        }
        return SCCs;
    }

    private void strongConnect(Node v) {
        indexes[v.id] = index;
        lowlink[v.id] = index++;
        onStack[v.id] = true;
        s.push(v);
        for (Edge e : v.adj) {
            Node w = e.destination();
            if (indexes[w.id] == -1) {
                strongConnect(w);
                lowlink[v.id] = Math.min(lowlink[v.id], lowlink[w.id]);
            } else if (onStack[w.id]) {
                lowlink[v.id] = Math.min(lowlink[v.id], indexes[w.id]);
            }
        }
        if (lowlink[v.id] == indexes[v.id]) {
            List<Node> SCC = new ArrayList<>();
            Node w;
            do {
                w = s.pop();
                onStack[w.id] = false;
                SCC.add(w);
            } while (w != v);
            SCCs.add(SCC);
        }
    }
}
