import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Prime {
    /**
     * Fast way to get all the primes up to some limit
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

    // Euler's totient function
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
        long s = Long.numberOfTrailingZeros(n-1);
        long d = n >> s;
        for (long a : A) {
            long p = NumberTheory.modPow(a % n, d, n);
            long i = s;
            while (p != 1 && p != n-1 && a % n != 0 && i-- != 0)
                p = p * p % n;
            if (p != n-1 && i != s)
                return false;
        }
        return true;
    }

    /**
     * Very slow in general, but is useful to know in case a few very large primes are required for some reason.
     * Can technically give non-prime numbers, but it is rare enough to not worry about it for most applications.
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

}
