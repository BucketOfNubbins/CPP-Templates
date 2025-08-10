import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;

public class NumberTheory {

    public static void main(String[] args) {
        long start = System.nanoTime();
        int n = 20; // has been tested with n = 30 and b = 15, no issues found.
        int b = 10;
        long p = (1 << b) - 1;
        for (int i = 0; p < (1 << n); i++) {
            long ti = bitPatternToIndex(p);
            long tp = indexToBitPattern(n, b, i);
            if (i != ti || p != tp) {
                System.out.println("ERROR!");
            }
            p = nextBinary(p);
        }
        long end = System.nanoTime();
        long elapsedTime = end - start;
        System.out.println(elapsedTime * 1e-9 + " seconds elapsed.");
    }

    static long crt(long a, long m, long b, long n) {
        if (n > m) return crt(b, n, a, m);
        long[] out = eEuclid(m, n);
        long g = out[0];
        long x = out[1];
        long y = out[2];
        System.out.println(Arrays.toString(out));
        if ((a - b) % g != 0) {
            throw new IllegalArgumentException("No Solution");
        }
        assert ((a - b) % g == 0); // else no solution
        x = (b - a) % n * x % n / g * m + a;
        return x < 0 ? x + m * n / g : x;
    }

    static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    // Euclid's algorithm to find the gcd of two numbers, written in Java
    static long gcd(long a, long b) {
        while (a != 0) {
            long temp = a;
            a = b % a;
            b = temp;
        }
        return b;
    }

    // Extended Euclidean algorithm, returns an array for [g, x, y],
    // where g is the gcd and (x, y) are the coefficients such that ax + by = g.
    // ax = g  mod b
    // by = g  mod a
    static long[] eEuclid(long a, long b) {
        long x = 1, y = 0, x1 = 0, y1 = 1, a1 = a, b1 = b;
        long t;
        while (b1 != 0) {
            long q = a1 / b1;
            t = x1;
            x1 = x - q * x1;
            x = t;
            t = y1;
            y1 = y - q * y1;
            y = t;
            t = b1;
            b1 = a1 - q * b1;
            a1 = t;
        }
        return new long[]{a1, (x + b) % b, y};
    }

    // calculates the greatest common divisor of a and b
    static long rGcd(long a, long b) {
        return b == 0 ? a : rGcd(b, a % b);
    }

    // Extended Euclidean algorithm, returns an array for [g, x, y],
    // where g is the gcd and (x, y) are the coefficients such that ax + by = g.
    // ax = g  mod b
    // by = g  mod a
    static long[] rEEuclid(long a, long b) {
        if (b == 0) {
            return new long[]{a, 1, 0};
        }
        long[] arr = rEEuclid(b, a % b);
        return new long[]{arr[0], arr[2], arr[1] - arr[2] * (a / b)};
    }

    static BigInteger[] BigEEuclid(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return new BigInteger[]{a, BigInteger.ONE, BigInteger.ZERO};
        }
        BigInteger[] arr = BigEEuclid(b, a.mod(b));
        return new BigInteger[]{arr[0], arr[2], arr[1].subtract(arr[2].multiply(a.divide(b)))};
    }

    // Smallest x S.T a^x = b (mod m)
    static long modLog(long a, long b, long m) {
        long n = (long) Math.sqrt(m) + 1;
        long e = 1, f = 1, j = 1;
        HashMap<Long, Long> A = new HashMap<>();
        while (j <= n && (e = f = e * a % m) != b % m)
            A.put(e * b % m, j++);
        if (e == b % m)
            return j;
        if (gcd(m, e) == gcd(m, b)) {
            for (int i = 2; i < n + 2; i++) {
                if (A.containsKey(e = e * f % m)) {
                    return n * i - A.get(e);
                }
            }
        }
        return -1;
    }

    // smallest x S.T x^2 = a (mod p), p must be prime
    static long sqrt(long a, long p) {
        a %= p;
        if (a < 0)
            a += p;
        if (a == 0)
            return 0;
        if (modPow(a, (p - 1) / 2, p) != 1)
            throw new IllegalArgumentException("no solution");
        assert (modPow(a, (p - 1) / 2, p) == 1); // else no solution
        if (p % 4 == 3)
            return modPow(a, (p + 1) / 4, p);
        long s = p - 1, n = 2;
        int r = 0, m;
        while (s % 2 == 0) {
            ++r;
            s /= 2;
        }
        while (modPow(n, (p - 1) / 2, p) != p - 1)
            ++n;
        long x = modPow(a, (s + 1) / 2, p);
        long b = modPow(a, s, p), g = modPow(n, s, p);
        for (; ; r = m) {
            long t = b;
            for (m = 0; m < r && t != 1; ++m)
                t = t * t % p;
            if (m == 0)
                return x;
            long gs = modPow(g, 1L << (r - m - 1), p);
            g = gs * gs % p;
            x = x * gs % p;
            b = b * g % p;
        }
    }


    // evaluates a^b (mod p)
    static long modPow(long a, long b, long p) {
        long res = 1;
        while (b != 0) {
            if ((b & 1) == 1) {
                res *= a;
                res %= p;
            }
            a *= a;
            a %= p;
            b >>= 1;
        }
        return res;
    }

    // evaluates a^b (mod p)
    static long modPowRec(long a, long b, long p) {
        if (b == 0) {
            return 1;
        }
        long res = (b & 1) == 1 ? a : 1;
        // (a * a)^(b/2) == a^(b/2) * a^(b/2)
        return (res * modPowRec((a * a) % p, b >> 1, p)) % p;
    }

    // n choose k (mod m)
    static long choose(long n, long k, long m) {
        if (k > n) {
            return 0L;
        }
        if (n - k < k) {
            return choose(n, n - k, m);
        }
        long numerator = 1;
        long denominator = 1;
        for (int i = 1; i <= k; i++) {
            numerator *= n - (k - i);
            numerator %= m;
            denominator *= i;
            denominator %= m;
        }
        long ans = numerator * modularInverse(denominator, m) % m;
        return (ans + m) % m;
    }

    /*
    TODO: better factorial / choose algorithms. See https://doi.org/10.1016/0196-6774(85)90006-9
     */


    // Appears to be the best of the choose functions.
    // It is the fastest, and might be the least prone to overflow issues.
    static long chooseWithoutMod(long n, long k) {
        if (k > n) {
            return 0L;
        }
        if (n - k < k) {
            return chooseWithoutMod(n, n - k);
        }
        long numerator = 1;
        long denominator = 1;
        for (int i = 1; i <= k; i++) {
            if (Long.MAX_VALUE / (n - (k - i)) <= numerator - 1 || Long.MAX_VALUE / i <= denominator - 1) {
                long g = gcd(numerator, denominator);
                numerator /= g;
                denominator /= g;
                if (g == 1) {
                    System.out.println("Error! Could not simplify the fraction!");
                    return -1;
                }
            }
            numerator *= n - (k - i);
            denominator *= i;
        }
        return numerator / denominator; // should be an exact integer
    }

    static long simpleChoose(long n, long k, long m) {
        long ans = factorial(n, m);
        ans *= eEuclid(factorial(n - k, m), m)[1];
        ans %= m;
        ans *= eEuclid(factorial(k, m), m)[1];
        ans %= m;
        return (ans + m) % m;
    }

    static long factorial(long n, long m) {
        long ans = 1;
        for (long i = 2; i <= n; i++) {
            ans *= i;
            ans %= m;
        }
        return ans;
    }

    // find (x, y) such that a*x + b*y = c or return false if it's not possible
    // [x + k*b/gcd(a, b), y - k*a/gcd(a, b)] are also solutions
    static long[] diof(long a, long b, long c) {
        long[] euclid = eEuclid(Math.abs(a), Math.abs(b));
        long g = Math.abs(euclid[0]);
        if (c % g != 0) return null;
        long x = euclid[1];
        long y = euclid[2];
        x *= c / g;
        y *= c / g;
        if (a < 0) x = -x;
        if (b < 0) y = -y;
        return new long[]{x, y};
    }

    // a * modularInverse(a, m) == 1 (mod m)
    static long modularInverse(long a, long m) {
        long[] euclid = eEuclid(a, m);
        if (euclid[0] != 1) {
            System.out.println("No Inverse!");
            return -1;
        } else {
            return ((euclid[1] % m) + m) % m;
        }
    }

    // 1011 -> 1101
    // calculates the next smallest binary number with the same number of set '1' bits.
    private static long nextBinary(long x) {
        int k = Long.bitCount(x);
        x += (x & -x);
        int fillCount = k - Long.bitCount(x);
        x |= (1L << fillCount) - 1;
        return x;
    }

    static long LARGE_PRIME = 2147483647L; // ~ sqrt(2^63)
    // 1000000007 also works in many cases
    // The chosen prime should be about the square root of the maximum possible value. (A bit less for wiggle room is good.)
    // for 32-bit integers, 46021 is a good choice.
    // Note that the chosen prime is also the maximum value returned.


    // See https://cs.stackexchange.com/questions/152121/generating-the-n-th-number-with-k-bits-set-is-it-possible
    // https://isl.stanford.edu/people/cover/papers/transIT/0073cove.pdf

    // generates the ith smallest binary number with bitCount bits set to 1
    static long indexToBitPattern(int maxBitPosition, int bitCount, long index) {
        long outputPattern = 0;
        for (int bitPosition = maxBitPosition; bitCount > 0; bitPosition--) {
            long c = chooseWithoutMod(bitPosition, bitCount);
            if (c <= index) {
                outputPattern |= 1L << bitPosition;
                index -= c;
                bitCount--;
            }
        }
        return outputPattern;
    }

    // calculates the index of the given bit pattern, related to the above function
    static long bitPatternToIndex(long bitPattern) {
        long index = 0;
        int bitIndex = 1;
        while (bitPattern != 0) {
            long bit = bitPattern & -bitPattern;
            bitPattern ^= bit;
            int bitPosition = Long.numberOfTrailingZeros(bit);
            index += chooseWithoutMod(bitPosition, bitIndex);
            bitIndex++;
        }
        return index;
    }

}
