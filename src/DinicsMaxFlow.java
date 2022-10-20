import java.io.*;
import java.util.*;

public class DinicsMaxFlow {

    private static class FlowGraph {
        Node[] nodes;

        private class Node {
            int level;
            List<Edge> edges;
            int id;

            public Node(int i) {
                id = i;
                level = -1;
                edges = new ArrayList<>();
            }
        }

        private class Edge {
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

        public FlowGraph(int n) {
            nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node(i);
            }
        }

        public void addEdge(int u, int v, int c) {
            Edge e = new Edge(c, nodes[u], nodes[v]);
            nodes[u].edges.add(e);
            nodes[v].edges.add(e);
        }

        public int[][] getUsedEdges() {
            List<int[]> edges = new ArrayList<>();
            for (Node n : nodes) {
                for (Edge e : n.edges) {
                    if (e.isNodeSource(n) && e.used > 0) {
                        edges.add(new int[]{e.a.id, e.b.id, e.used});
                    }
                }
            }
            return edges.toArray(new int[edges.size()][]);
        }

        public int Dinics(int source, int sink) {
            return Dinics(nodes[source], nodes[sink]);
        }

        private int Dinics(Node source, Node sink) {
            int total = 0;
            while (createLevels(source, sink)) {
                int[] indices = new int[nodes.length];
                int flow;
                while ((flow = getFlow(Integer.MAX_VALUE, source, sink, indices)) != 0) {
                    total += flow;
                }
            }
            return total;
        }

        private int getFlow(int max, Node source, Node sink, int[] indices) {
            if (source == sink) {
                return max;
            } else {
                for (; indices[source.id] < source.edges.size(); indices[source.id]++) {
                    Edge e = source.edges.get(indices[source.id]);
                    Node other = e.getOther(source);
                    int flow;
                    if (other.level - source.level == 1) {
                        if (canFlow(source, e)) {
                            flow = getFlow(Math.min(max, e.capacity - e.used), other, sink, indices);
                            if (flow != 0) {
                                e.used += flow;
                                return flow;
                            }
                        } else if (canUnFlow(source, e)) {
                            flow = getFlow(Math.min(max, e.used), other, sink, indices);
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

        private boolean createLevels(Node source, Node sink) {
            HashSet<Node> visited = new HashSet<>();
            visited.add(source);
            source.level = 0;
            Queue<Node> queue = new ArrayDeque<>(nodes.length);
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

        private boolean canFlow(Node s, Edge e) {
            return e.isNodeSource(s) && e.capacity > e.used;
        }

        private boolean canUnFlow(Node s, Edge e) {
            return !e.isNodeSource(s) && e.used > 0;
        }
    }

    /**
     * Example use
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        String[] input = br.readLine().trim().split(" ");
        int n = Integer.parseInt(input[0]);
        int m = Integer.parseInt(input[1]);
        int s = Integer.parseInt(input[2]);
        int t = Integer.parseInt(input[3]);

        FlowGraph fg = new FlowGraph(n);

        for (int i = 0; i < m; i++) {
            input = br.readLine().trim().split(" ");
            int u = Integer.parseInt(input[0]);
            int v = Integer.parseInt(input[1]);
            int c = Integer.parseInt(input[2]);
            fg.addEdge(u, v, c);
        }
        int f = fg.Dinics(s, t);
        StringBuilder sb = new StringBuilder();
        int[][] usedEdges = fg.getUsedEdges();
        for (int[] edge : usedEdges) {
            sb.append(edge[0]).append(" ").append(edge[1]).append(" ").append(edge[2]).append("\n");
        }
        bw.write(n + " " + f + " " + usedEdges.length + "\n");
        bw.write(sb.toString());

        br.close();
        bw.close();
    }

}
