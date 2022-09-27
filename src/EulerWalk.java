import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class EulerWalk {


    // gr is adjacency List, with pair (destination, global edge index), output is reverse path
    private static List<Integer> eulerWalk(List<List<int[]>> gr, int nEdges, int src) {
        int n = gr.size();
        int[] D = new int[n];
        int[] its = new int[n];
        int[] eu = new int[n];
        List<Integer> ret = new ArrayList<>();
        Stack<Integer> s = new Stack<>();
        s.push(src);
        D[src]++; // to allow Euler paths, not just cycles
        while (!s.empty()) {
            int x = s.peek();
            int end = gr.get(x).size();
            if (its[x] == end) {
                ret.add(x);
                s.pop();
                continue;
            }
            int y = gr.get(x).get(its[x])[0];
            int e = gr.get(x).get(its[x])[1];
            its[x]++;
            if (eu[e] == 0) {
                D[x]--;
                D[y]++;
                eu[e] = 1;
                s.push(y);
            }
        }
        for (int x : D) {
            if (x < 0 || ret.size() != nEdges + 1) return null;
        }
        return ret;
    }


    // Example Execution
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        String[] input = br.readLine().split(" ");
        int V = Integer.parseInt(input[0]);
        int E = Integer.parseInt(input[1]);
        while (V != 0 && E != 0) {
            List<List<int[]>> gr = new ArrayList<>(V);
            for (int i = 0; i < V; i++) {
                gr.add(new ArrayList<>());
            }
            for (int i = 0; i < E; i++) {
                input = br.readLine().split(" ");
                int u = Integer.parseInt(input[0]);
                int v = Integer.parseInt(input[1]);
                gr.get(u).add(new int[]{v, i});
            }
            int src = 0;
            for (int i = 0; i < V; i++) {
                if (gr.get(i).size() % 2 == 1) {
                    src = i;
                    break;
                }
            }
            List<Integer> path = eulerWalk(gr, E, src);
            if (path != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(" ");
                for (int v : path) {
                    sb.append(v);
                    sb.append(" ");
                }
                System.out.println(sb.reverse().toString().trim());
            } else {
                System.out.println("Impossible");
            }

            input = br.readLine().split(" ");
            V = Integer.parseInt(input[0]);
            E = Integer.parseInt(input[1]);
        }

        br.close();
        bw.close();
    }

}
