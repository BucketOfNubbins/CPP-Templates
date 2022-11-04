
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
