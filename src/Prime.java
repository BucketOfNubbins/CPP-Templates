import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.LongStream;

public class Prime {
    /*
    A couple random large prime numbers:
    - int:
        307722227
        763949873
        834545111
        667269971
        235974839
    - long:
        518463576809201L
        551133728342474639L
        248883903731404283L
        378340956398423323L
        530129765484895267L
        280763777358021569L
     */

    /**
     * Fast way to get all the primes up to some limit
     *
     * @param n indicates that we should generate the prime numbers up to n
     * @return A list of prime numbers
     */
    public static List<Integer> getPrimes(int n) {
        List<Integer> primes = new ArrayList<>();
        boolean[] isPrime = new boolean[n];
        Arrays.fill(isPrime, true);

        for (int i = 2; i < n; i++) {
            if (isPrime[i]) {
                primes.add(i);
                for (long j = (long) i * i; j < n; j += i) {
                    isPrime[(int) j] = false;
                }
            }
        }
        return primes;
    }

    // Euler's totient function
    // Counts the positive integers up to a given integer n that are relatively prime to n
    // Useful when combined with Fermat-Euler theorem.
    // {
    //      if a and n are coprime (gcd == 1)
    //      a^phi(n) == 1 mod n
    // }
    public static int phi(int n) {
        int res = n;
        for (int i = 2; i * i <= n; i++)
            if (n % i == 0) {
                while (n % i == 0) n /= i;
                res -= res / i;
            }
        if (n > 1)
            res -= res / n;
        return res;
    }

    /*
    phi(n) = n * product_{p | n} (1 - p_n)
    or
    phi(n) = product_{p, k} p^(k-1) * (p-1)
    where p is a prime divisor of n, and k is the exponent of that divisor.

    CODE BELOW IS UNTESTED, AND LIKELY HAS OVERFLOW ISSUES
     */
    public static int phi(int n, int[][] factors) {
        int res = 1;
        for (int[] pair : factors) {
            int p = pair[0];
            int k = pair[1];
            res *= NumberTheory.modPow(p, k - 1, n) * (p - 1);
        }
        return res;
    }

    // Euler's totient function
    // If you need many (relatively small) values of the totient function
    private static long[] generatePhi(int length) {
        long[] arr = LongStream.range(0, length).toArray();
        for (int i = 2; i < length; i++) {
            if (arr[i] == i) {
                for (int j = i; j < length; j += i) {
                    arr[j] *= i - 1;
                    arr[j] /= i;
                }
            }
        }
        return arr;
    }

    boolean isPrime(long n) {
        if (n < 2 || n % 6 % 4 != 1) return (n | 1) == 3;
        long[] A = {2, 325, 9375, 28178, 450775, 9780504, 1795265022};
        long s = Long.numberOfTrailingZeros(n - 1);
        long d = n >> s;
        for (long a : A) {
            long p = NumberTheory.modPow(a % n, d, n);
            long i = s;
            while (p != 1 && p != n - 1 && a % n != 0 && i-- != 0)
                p = p * p % n;
            if (p != n - 1 && i != s)
                return false;
        }
        return true;
    }

    /**
     * Very slow in general, but is useful to know in case a few very large primes are required for some reason.
     * Can technically give non-prime numbers, but it is rare enough to not worry about it for most applications.
     *
     * @param n prime numbers to generate up to
     * @return A list of prime numbers
     */
    public static List<Long> altPrimes(long n) {
        List<Long> primes = new ArrayList<>();
        BigInteger limit = BigInteger.valueOf(n);
        BigInteger num = BigInteger.valueOf(2);
        while (limit.compareTo(num) > 0) {
            primes.add(num.longValue());
            num = num.nextProbablePrime();
        }
        return primes;
    }

    /**
     * Returns the prime factorization of the specified value `n`, or null if n is negative.
     * <p>
     * The prime factorization is represented as a `List` of size 2 `List`s. Each size 2 sublist contains a prime as
     * the first argument and the exponent of the prime in the prime factorization of `n` as the second argument.
     * <p>
     * Runs in O(pi(sqrt(n))) = O(sqrt(n) / lg n) time.
     *
     * @param n                     The long to take the prime factorization of.
     * @param sortedPrimesUpToSqrtN The primes up to and including sqrt(n) in sorted order. The method will run just as
     *                              efficiently if more primes are included. It is just required that all primes up to
     *                              and including sqrt(n) are included for the method to work properly.
     * @return The prime factorization of the specified value `n`, or an empty list if `n <= 1`.
     * @see "getPrimes"
     */
    public static List<long[]> primeFactorize(long n, List<Integer> sortedPrimesUpToSqrtN) {
        if (n <= 0) {
            return null;
        }
        List<long[]> res = new ArrayList<>();
        for (long p : sortedPrimesUpToSqrtN) {
            if (p * p > n || n == 1) {
                break;
            }

            if (n % p == 0) {
                long[] primeFactor = new long[]{p, 1};
                n /= p;
                while (n % p == 0) {
                    primeFactor[1]++;
                    n /= p;
                }
                res.add(primeFactor);
            }
        }
        if (n != 1) {
            res.add(new long[]{n, 1});
        }
        return res;
    }

    /**
     * Returns the positive factors of the specified value `n`, or null if `n` is 0 or `Long.MIN_VALUE`.
     * <p>
     * Runs in O(sqrt(n) / lg n) time (due to prime factorization).
     *
     * @param n                     The long whose factors are to be returned.
     * @param sortedPrimesUpToSqrtN The primes up to and including sqrt(n) in sorted order. The method will run just as
     *                              efficiently if more primes are included. It is just required that all primes up to
     *                              and including sqrt(n) are included for the method to work properly.
     * @return The factors of the specified value `n`.
     * @see "primeFactorize"
     */
    public static List<Long> getFactors(long n, List<Integer> sortedPrimesUpToSqrtN) {
        if (n == 0 || n == Long.MIN_VALUE) {
            return null;
        }

        List<Long> res = new ArrayList<>();
        res.add(1L);

        n = Math.abs(n);

        for (long[] primeFactor : Objects.requireNonNull(primeFactorize(n, sortedPrimesUpToSqrtN))) {
            int resSize = res.size();
            for (int resIndex = 0; resIndex < resSize; ++resIndex) {
                long multiplier = primeFactor[0];
                for (long i = 1; i <= primeFactor[1]; ++i) {
                    res.add(res.get(resIndex) * multiplier);
                    multiplier *= primeFactor[0];
                }
            }
        }

        return res;
    }
}
