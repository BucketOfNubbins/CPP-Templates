
public class DinicsMaxFlow {
    private static class Node {
        int level;
        List<Edge> edges;
        int id;

        public Node(int i) {
            id = i;
            level = -1;
            edges = new ArrayList<>();
        }
    }

    private static class Edge {
        int capacity;
        int used;
        // Flows from a to b, if directed
        Node a;
        Node b;

        public Edge(int c, Node a, Node b) {
            capacity = c;
            used = 0;
            this.a = a;
            this.b = b;
        }

        public Node getOther(Node t) {
            return t == a ? b : a;
        }

        public boolean isNodeSource(Node t) {
            return t == a;
        }
    }

    private static int Dinics(Node source, Node sink) {
        int total = 0;
        while (createLevels(source, sink)) {
            int flow = getFlow(Integer.MAX_VALUE, source, sink);
            while (flow != 0) {
                total += flow;
                flow = getFlow(Integer.MAX_VALUE, source, sink);
            }
        }
        return total;
    }

    private static int getFlow(int max, Node source, Node sink) {
        if (source == sink) {
            return max;
        } else {
            for (Edge e : source.edges) {
                Node other = e.getOther(source);
                int flow;
                if (other.level - source.level == 1) {
                    if (canFlow(source, e)) {
                        flow = getFlow(Math.min(max, e.capacity - e.used), other, sink);
                        if (flow != 0) {
                            e.used += flow;
                            return flow;
                        }
                    } else if (canUnFlow(source, e)) {
                        flow = getFlow(Math.min(max, e.used), other, sink);
                        if (flow != 0) {
                            e.used -= flow;
                            return flow;
                        }
                    }
                }
            }
            return 0;
        }
    }

    private static boolean createLevels(Node source, Node sink) {
        HashSet<Node> visited = new HashSet<>();
        visited.add(source);
        source.level = 0;
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(source);
        while (!queue.isEmpty()) {
            Node s = queue.remove();
            for (Edge e : s.edges) {
                Node o = e.getOther(s);
                if (!visited.contains(o) && (canFlow(s, e) || canUnFlow(s, e))) {
                    o.level = s.level + 1;
                    visited.add(o);
                    queue.add(o);
                }
            }
        }
        return visited.contains(sink);
    }

    private static boolean canFlow(Node s, Edge e) {
        return e.isNodeSource(s) && e.capacity > e.used;
    }

    private static boolean canUnFlow(Node s, Edge e) {
        return !e.isNodeSource(s) && e.used > 0;
    }
}
