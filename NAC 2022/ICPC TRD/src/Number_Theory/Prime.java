import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Prime {
    /**
     * Fast way to get all the primes up to some limit
     * @param n prime numbers to generate up to
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
    public static int[] generatePhi(int n) {
        int[] res = IntStream.range(0, n + 1).toArray();
        for (int i = 1; i <= n; i++)
            for (int j = i + i; j <= n; j += i) res[j] -= res[i];
        return res;
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

}
