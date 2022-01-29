import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class EdmondKarpMaxFlow {

    List<Edge>[] adjList;

    private static class Edge {
        int to;
        int from;
        int capacity;
        int flow;
        Edge reverse;

        public Edge(int u, int v, int c) {
            from = u;
            to = v;
            capacity = c;
            flow = 0;
            reverse = null;
        }
    }

    public EdmondKarpMaxFlow(int n) {
        adjList = new List[n];
        for (int i = 0; i < n; i++) {
            adjList[i] = new ArrayList<>();
        }
    }

    public void addEdge(int u, int v, int c) {
        Edge[] edges = createEdge(u, v, c);
        adjList[u].add(edges[0]); // u to v
        adjList[v].add(edges[1]); // v to u
    }

    private Edge[] createEdge(int u, int v, int c) {
        Edge to = new Edge(u, v, c);
        Edge reverse = new Edge(v, u, 0);
        to.reverse = reverse;
        reverse.reverse = to;
        return new Edge[]{to, reverse};
    }

    private List<Edge> getPath(int s, int t) {
        boolean[] visited = new boolean[adjList.length];
        Edge[] usedEdge = new Edge[adjList.length];
        Queue<Integer> queue = new ArrayDeque<>();
        Queue<Edge> edgeQueue = new ArrayDeque<>();
        queue.add(s);
        edgeQueue.add(new Edge(-1, -1, -1));
        while (!queue.isEmpty()) {
            int cur = queue.remove();
            Edge used = edgeQueue.remove();
            if (visited[cur]) {
                continue;
            }
            usedEdge[cur] = used.from == -1 ? null : used;
            visited[cur] = true;
            for (Edge e : adjList[cur]) {
                if (e.capacity - e.flow > 0) {
                    queue.add(e.to);
                    edgeQueue.add(e);
                }
            }
        }
        List<Edge> path = new ArrayList<>();
        Edge cur = usedEdge[t];
        while (cur != null) {
            path.add(cur);
            cur = usedEdge[cur.from];
        }
        return path.size() == 0 ? null : path;
    }

    private int maxFlow(int s, int t) {
        int sum = 0;
        List<Edge> path;
        while ((path = getPath(s, t)) != null) {
            int bottleneck = Integer.MAX_VALUE;
            for (Edge e : path) {
                bottleneck = Math.min(bottleneck, e.capacity - e.flow);
            }
            for (Edge e : path) {
                e.flow += bottleneck;
                e.reverse.flow -= bottleneck;
            }
            sum += bottleneck;
        }
        return sum;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        EdmondKarpMaxFlow g = new EdmondKarpMaxFlow(6);
        g.addEdge(0, 1, 5);
        g.addEdge(1, 2, 6);
        g.addEdge(2, 3, 3);
        g.addEdge(2, 4, 4);
        g.addEdge(0, 4, 3);
        g.addEdge(4, 5, 6);
        g.addEdge(5, 3, 4);
        int flow = g.maxFlow(0, 3);
        bw.write(flow + "\n");

        br.close();
        bw.close();
    }

}

