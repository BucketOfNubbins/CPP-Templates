// (Stanford) Example for using stringstreams and next_permutation
int main(void){
    vector<int> v = {1,2,3,4};
    
    // Expected output: 1 2 3 4
    //                  1 2 4 3
    //                  ...
    //                  4 3 2 1  
    do {
        ostringstream oss;
        oss << v[0] << " " << v[1] << " " << v[2] << " " << v[3];
        
        // for input from a string s,
        //   istringstream iss(s);
        //   iss >> variable;
        
        cout << oss.str() << endl;
    } while (next_permutation (v.begin(), v.end()));
    
    v.clear();
    
    v.push_back(1); v.push_back(2); v.push_back(1); v.push_back(3);
    
    // To use unique, first sort numbers.  Then call
    // unique to place all the unique elements at the beginning
    // of the vector, and then use erase to remove the duplicate
    // elements.
    
    sort(v.begin(), v.end());
    v.erase(unique(v.begin(), v.end()), v.end());
    
    // Expected output: 1 2 3
    for (size_t i = 0; i < v.size(); i++)
    cout << v[i] << " ";
    cout << endl; 
}

// Use of mt19937 for generating random numbers in range (0, MAX):
random_device device;
mt19937 generator(device());
uniform_int_distribution<int> unifDist(0,MAX);
int randNum = unifDist(generator)

// Use of chrono for getting program runtime:
#include <chrono>
auto start = chrono::high_resolution_clock::now();
auto duration = chrono::duration_cast<chrono::milliseconds>
                    (chrono::high_resolution_clock::now() - start);
if (duration.count() > 1000) { cout << "fail\n"; return 0; } // 1 second

// Bitsets:
#include <bitset>
_Find_first(i);
_Find_next(i);
// This code prints all set bits of BS:
for (int i = BS._Find_first(); i < BS.size(); i = BS._Find_next(i)) 
    cout << i << endl;
    
// Custom Sort Function:
struct less_than_key {
    inline bool operator() (const MyDS& ds1, const MyDS& struct2) {
        return (struct1.key < struct2.key);
    }
};
vector <MyDS> vec;
sort(vec.begin(), vec.end(), less_than_key());

// Lambdas:
sort(x, x + n,
        [](float a, float b) {return (std::abs(a) < std::abs(b));}
    );
// Capture clause: [x] captures x by value, [&x] captures by reference