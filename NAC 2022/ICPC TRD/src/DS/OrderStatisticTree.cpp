#include <ext/pb_ds/assoc_container.hpp>
#include <ext/pb_ds/tree_policy.hpp>
using namespace __gnu_pbds;

// Use pair<int,int> if dup elems can exist -- first val, second index.
typedef tree<int, null_type, less<int>, rb_tree_tag,
			tree_order_statistics_node_update>
	ordered_set;
ordered_set X;
X.insert(1);
X.insert(2);
X.insert(4);

cout<<*X.find_by_order(1)<<endl; // 2
cout<<*X.find_by_order(2)<<endl; // 4
cout<<(end(X)==X.find_by_order(4))<<endl; // true

cout<<X.order_of_key(-5)<<endl;  // 0
cout<<X.order_of_key(1)<<endl;   // 0
cout<<X.order_of_key(3)<<endl;   // 2
cout<<X.order_of_key(4)<<endl;   // 2
cout<<X.order_of_key(400)<<endl; // 2