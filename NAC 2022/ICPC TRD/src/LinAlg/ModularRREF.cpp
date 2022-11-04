// Mod arithmetic for small modulos only. Resolves negative issue of % (e.g. -1 % 2 = -1 instead of 1).
vector<int> mod_inv;
int mod(int a, int m) {
    if (a < -m || a >= m) a = a % m;
    if (a < 0) a += m;
    return a;
}
void initModInv(int m) {
    mod_inv.resize(m, 0); mod_inv[1] = 1;
    for (int i = 2; i < m; ++i)
        mod_inv[i] = m - (m/i) * mod_inv[m%i] % m;
}
int modMult(int a, int b, int m) { return mod(a*b, m); }
int modAdd(int a, int b, int m) { return mod(a+b,m); }
int modSub(int a, int b, int m) { return mod(a-b,m); }
int modDiv(int a, int b, int m) { assert(b!=0); return mod(a*mod_inv[b], m); }

// Modular rref. Returns rank of matrix.
int rrefMod(vector<vector<int>> &a, int mod) {
    int n = a.size();
    int m = a[0].size();
    int r = 0;
    for (int c = 0; c < m && r < n; c++) {
        int j = r;
        for (int i = r + 1; i < n; i++)
            if (abs(a[i][c]) > abs(a[j][c]))
                j = i;
        if (a[j][c] == 0)
            continue;
        swap(a[j], a[r]);

        int s = mod_inv[a[r][c]];
        for (int j = 0; j < m; j++)
            a[r][j] = modMult(a[r][j], s, mod);
        for (int i = 0; i < n; i++)
            if (i != r) {
                int t = a[i][c];
                for (int j = 0; j < m; j++)
                    a[i][j] = modSub(a[i][j], modMult(t, a[r][j], mod), mod);
            }
        r++;
    }
    return r;
}

void testSystem(vector<vector<int>> linEqs) {
    vector<vector<int>> modEqs(100, vector<int>(100, 0));
    for (int i = 0; i < 100; i++)
        for (int j = 0; j < 100; j++)
            modEqs[i][j] = mod(linEqs[i][j], p);
    initModInv(p);
    rrefMod(modEqs, p);
}
