import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
