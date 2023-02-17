import java.util.Arrays;
import java.util.HashMap;

public class NumberTheory {

    public static void main(String[] args) {
        System.out.println(sqrt(1, 5));
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
//            System.out.printf("%d & %d & %d & %d & %d & %d \\\\\n", x, y, x1, y1, a1, b1);
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
//        System.out.printf("%d & %d & %d & %d & %d & %d \n", x, y, x1, y1, a1, b1);
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
        long ans = (numerator * eEuclid(denominator, m)[1]) % m;
        return (ans + m) % m;
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
    long[] diof(long a, long b, long c) {
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
}
