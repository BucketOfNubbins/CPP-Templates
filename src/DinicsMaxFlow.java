import java.io.*;
import java.util.*;

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

        Node[] graph = new Node[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new Node(i);
        }
        Edge[] edges = new Edge[m];

        for (int i = 0; i < m; i++) {
            input = br.readLine().trim().split(" ");
            int u = Integer.parseInt(input[0]);
            int v = Integer.parseInt(input[1]);
            int c = Integer.parseInt(input[2]);
            edges[i] = new Edge(c, graph[u], graph[v]);
            graph[u].edges.add(edges[i]);
            graph[v].edges.add(edges[i]);
        }

        int f = Dinics(graph[s], graph[t]);
        int usedEdges = 0;
        StringBuilder sb = new StringBuilder();
        for (Edge e : edges) {
            if (e.used != 0) {
                usedEdges++;
                sb.append(e.a.id);
                sb.append(" ");
                sb.append(e.b.id);
                sb.append(" ");
                sb.append(e.used);
                sb.append("\n");
            }
        }
        bw.write(n + " " + f + " " + usedEdges + "\n");
        bw.write(sb.toString());

        br.close();
        bw.close();
    }

}
