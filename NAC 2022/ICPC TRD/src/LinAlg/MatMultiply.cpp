typedef double T;

vector<vector<T>> MatMultiply(vector<vector<T>>& a, vector<vector<T>>& b) {
  const int n = a.size();
  const int m = b[0].size();
  const int z = a[0].size();
  vector<vector<T>> c(n, vector<T>(m, 0));
  if(b.size() != z){ cout << "matrices not the right size" << endl; exit(0);}
  for(int i = 0; i < n; i++)
    for(int j = 0; j < m; j++)
      for(int k = 0; k < z; k++)
        c[i][j] += a[i][k]*b[k][j];
  return c;
}