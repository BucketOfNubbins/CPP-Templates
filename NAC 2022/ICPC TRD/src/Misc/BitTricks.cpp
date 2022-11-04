// Gosper's Hack: Iterates all size k subsets (belonging to set of n elems).
int mask = (1 << k) - 1, x, y;
while (mask <= (1 << n) - (1 << (n-k))) {
    // Code here
    x = mask&-mask, y = mask+x, mask = y | (((y^mask)>>2)/x);
}

// Get LSB of X
x & -x

// Submask Emumeration: Loops over all subset masks of m (except m itself).
for (int x = m; x; ) { --x &= m; ... } 

// Uppercase to lowercase: ch |= ' ';
// Lowercase to uppercase: ch &= '_';

// Int bitset operations. Alternative is bitset (#include <bitset>) which does 
// not have subtraction (thus has an uglier implementation for submask iteration)
#define iBitset unsigned __int128
void add(iBitset& set, int elem) { set |= (iBitset)1<<elem; }
void remove(iBitset& set, int elem) { set &= ~((iBitset)1<<elem); }
bool isIn(iBitset set, int elem) { return (((iBitset)1<<elem) & set); }
iBitset bSetUnion(iBitset s1, iBitset s2) { return s1 | s2; }
iBitset bSetIntersect(iBitset s1, iBitset s2) { return s1 & s2; }
iBitset bSetComplement(iBitset set) 
    { return ~set & (((iBitset)1<<128) - 1); } // Throws -Wall warning
void printBits(iBitset bSet) {
    for (int i = 127; i >= 0; i--) 
        cout << ((bSet & ((iBitset)1<<i)) ? '1' : '0');
    cout << endl; 
}
vector<int> getElemsInBitset(iBitset bSet) {
    vector<int> elems;
    for (int i = 0; i < 128; i++)
        if (isIn(bSet, i)) elems.push_back(i);
    return elems;
}