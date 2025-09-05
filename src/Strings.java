
// In comments, I replace the .equals() function with == for legibility

public class Strings {
    // pi[i] = length of the longest proper prefix of the substring s[0...i] which is also a suffix of this substring.
    // s.substring(0, pi[i]) == s.substring(i + 1 - pi[i], i + 1)
    // remember that in {s.substring(start, end)} start is inclusive and end is exclusive
    private static void prefixFunc(String s, int[] pi) {
        int n = s.length();
        for (int i = 1; i < n; i++) {
            int j = pi[i - 1];
            while (j > 0 && s.charAt(i) != s.charAt(j))
                j = pi[j - 1];
            if (s.charAt(i) == s.charAt(j))
                j++;
            pi[i] = j;
        }
    }

    // z[i] is the length of the longest string, starting from index i, that matches the start of s.
    // s.substring(0, z[i]) == s.substring(i, i + z[i])
    private static void zFunc(String s, int[] z) {
        int n = s.length();
        for (int i = 1, l = 0, r = 0; i < n; ++i) {
            if (i <= r)
                z[i] = Math.min(r - i + 1, z[i - l]);
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i]))
                ++z[i];
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
    }

    //  p[0][i] = half length of longest even palindrome around pos i, p[1][i] = longest odd (half rounded down).
    private static int[][] manacher(String s) {
        int n = s.length();
        int[][] p = new int[2][n + 1];
        for (int z = 0; z < 2; z++) {
            for (int i = 0, l = 0, r = 0; i < n; i++) {
                int t = r - i + (z ^ 1);
                if (i < r) {
                    p[z][i] = Math.min(t, p[z][l + t]);
                }
                int L = i - p[z][i], R = i + p[z][i] - (z ^ 1);
                while (L >= 1 && R + 1 < n && s.charAt(L - 1) == s.charAt(R + 1)) {
                    p[z][i]++;
                    L--;
                    R++;
                }
                if (R > r) {
                    l = L;
                    r = R;
                }
            }
        }
        return p;
    }

    private static String leastRotation(String s) {
        s += s;
        int[] f = new int[s.length()];
        int k = 0;
        for (int j = 1; j < s.length(); j++) {
            int i = f[j - k - 1];
            while (i != -1 && s.charAt(j) != s.charAt(k + i + 1)) {
                if (s.charAt(j) < s.charAt(k + i + 1)) k = j - i - 1;
                i = f[i];
            }

            if (s.charAt(j) != s.charAt(k + i + 1)) {
                if (s.charAt(j) < s.charAt(k)) k = j;
                f[j - k] = -1;
            } else f[j - k] = i + 1;
        }
        return s.substring(k, k + s.length() / 2);
    }

    // String hashing
    // Rolling hash function is used, see the following
    // https://cp-algorithms.com/string/string-hashing.html
    // https://en.wikipedia.org/wiki/Rolling_hash
    /*
        Rolling hashes can be a very powerful way to check for matching sub strings
        For example, the following problem requires the use of hashing
        https://open.kattis.com/problems/isomorphicinversion
     */

    /*
        Several function from NumberTheory.java are used here, see that file for those implementations.
     */

    /*  When choosing primes for your hash, I recommend
            {a} be larger than the alphabet
            {m} be as large as possible, but less than the square root of Long.MAX_VALUE.


        Some choices of primes for {a}
            31
            37
            41
            53
            59
            71
            73
        (Note:(long) Char.MAXVALUE = 65535)
            65537
            65539
            65837
            65881
            66041

        Some choice of primes for {m}
            1e9 + 7
            1e9 + 9
            1e9 + 21
            1e9 + 87
        (Note: floor(sqrt(Long.MAX_VALUE)) = 3_037_000_499)
        (Integer.MAX_VALUE = 2_147_483_647)
            2124749677
            2131131137
            2147483647 (Yes, Integer.MAX_VALUE is prime)


     */

    /*
        I provided so many options for {a} and {m} above since changing those values yields different hash functions
        which can be used to guard against collisions in one of the hash functions. I recommend mainly changing the
        value of {a} for guarding against collisions as small strings might not be affected by the modulus.
        If you need more primes, see https://t5k.org/curios/ or Prime.java to generate your own.
     */

    /*
    The implementation below assumes nothing about the alphabet besides that it will fit inside a Java Character.
    So the max value I assume is 65535.

    The implementation can easily be changed for various specific alphabets
    digits '0' : '9'
        c = c - '0'

    lowercase letters 'a' : 'z'
        c = c - 'a'

    uppercase letters 'A' : 'Z'
        c = c - 'A'
     */

    /*
        hash(S, a, m) = sum{i=0}{|S|-1} S_i * a^i     % m
        |S| = size of S
        S_i is the ith character of S, zero indexed
    */
    private static long hash(String s, long a, long m) {
        long hash = 0; //
        long ai = 1; // a^i
        for (int i = 0; i < s.length(); i++) {
            long cc = s.charAt(i); // can replace with other function if alphabet is known
            hash += (cc * ai) % m;
            hash %= m;
            ai *= a;
            ai %= m;
        }
        return hash;
    }

    /*
        Precomputing the powers, and their modular inverse, of {a} can speed up computation.
     */

    // TODO: Test all of the below functions
    private static long extendRight(long previousHash, int previousLength, char c, long a, long m) {
        long ap = NumberTheory.modPow(a, previousLength, m);
        long hash = ap * c;
        hash %= m;
        hash += previousHash;
        hash %= m;
        return hash;
    }

    private static long extendLeft(long previousHash, int previousLength, char c, long a, long m) {
        long hash = previousHash * a;
        hash %= m;
        hash += c;
        hash %= m;
        return hash;
    }

    private static long retractRight(long previousHash, int previousLength, char c, long a, long m) {
        // assert previousLength >= 1
        long lastPow = NumberTheory.modPow(a, previousLength - 1, m);
        long lastTerm = (c * lastPow) % m;
        long hash = previousHash - lastTerm;
        hash += m;
        hash %= m;
        return hash;
    }

    private static long retractLeft(long previousHash, int previousLength, char c, long a, long m) {
        long hash = previousHash - c;
        hash += m;
        hash %= m;
        hash *= NumberTheory.modularInverse(a, m);
        hash %= m;
        return hash;
    }

    private static long shiftRight(long previousHash, int length, char l, char r, long a, long m) {
        long hash = retractLeft(previousHash, length, l, a, m);
        hash = extendRight(hash, length - 1, r, a, m);
        return hash;
    }

    private static long shiftLeft(long previousHash, int length, char l, char r, long a, long m) {
        long hash = retractRight(previousHash, length, r, a, m);
        hash = extendLeft(hash, length - 1, l, a, m);
        return hash;
    }


}
