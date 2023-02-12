// Adapted from cp-algorithms.

int numVertices = 0, logDepth = 0, timer = 0;
vector<vector<int>> adjList, up;
vector<int> timeIn, timeOut;

void dfs(int v, int p) {
    timeIn[v] = ++timer;
    up[v][0] = p;
    for (int i = 1; i <= logDepth; ++i)
        up[v][i] = up[up[v][i-1]][i-1];

    for (int u : adjList[v])
        if (u != p)
            dfs(u, v);

    timeOut[v] = ++timer;
}

// Answers if vertex u is an ancestor of vertex v.
bool is_ancestor(int u, int v) { 
    return timeIn[u] <= timeIn[v] && timeOut[u] >= timeOut[v]; 
}

void preprocess(int root) {
    timeIn.resize(numVertices), timeOut.resize(numVertices);
    logDepth = ceil(log2(numVertices)); // <cmath> header
    up.resize(numVertices, vector<int>(logDepth + 1));

    dfs(root, root);
}

long long int lca(int u, int v) {
    if (is_ancestor(u,v)) return u;
    if (is_ancestor(v,u)) return v;
    for (int i = logDepth; i >= 0; --i)
        if (!is_ancestor(up[u][i], v))
            u = up[u][i];
    return up[u][0];
}