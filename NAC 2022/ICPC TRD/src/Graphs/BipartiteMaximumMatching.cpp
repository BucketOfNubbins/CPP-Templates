// Bipartite Maximum Matching
// adjList has left elems only, which are adj to right elems.
// leftMatch[i] is matching right element, rightMatch[j] is matching left element.
// Assumes left elems are 0, ... , lSz-1 and right elems are 0, ... , rSz-1.

vector<vector<int>> adjList;
int leftSize, rightSize;
vector<bool> visited;
vector<int> leftMatch, rightMatch;

int dfs(int currLElem) {
    if (currLElem < 0) return 1;
    if (visited[currLElem]) return 0;
    visited[currLElem] = true;

    for (auto currRElem : adjList[currLElem]) {
        if (dfs(rightMatch[currRElem])) {
            leftMatch[currLElem] = currRElem;
            rightMatch[currRElem] = currLElem;
            return 1;
        }
    }
    return 0; //No matches found
}

int maxBipartiteMatching() {
    int max = 0;
    fill(leftMatch.begin(), leftMatch.end(), -1);
    fill(rightMatch.begin(), rightMatch.end(), -1);
    for (int currLElem = 0; currLElem < leftSize; currLElem++) {
        if (leftMatch[currLElem] < 0) {
            fill(visited.begin(), visited.end(), false);
            max += dfs(currLElem);
        }
    }
    return max;
}