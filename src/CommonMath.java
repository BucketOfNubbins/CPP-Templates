import java.io.*;
import java.util.Arrays;

public class CommonMath {

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 10; i++) {
            System.out.println(Arrays.toString(eEuclid(fib(i + 1), fib(i))));
            System.out.println(fib(i + 1) + " " + fib(i));
        }
    }

    private static int fib(int n) {
        if (n <= 1) {
            return 1;
        } else {
            return fib(n-1) + fib(n-2);
        }
    }

    private static int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    // Euclid's algorithm to find the gcd of two numbers, written in Java
    private static int gcd(int a, int b) {
        while (a != 0) {
            int temp = a;
            a = b % a;
            b = temp;
        }
        return b;
    }

    // Bonus Extended Euclidean algorithm, returns and array for [g, x, y] where g is the gcd, and x, y are the coefficients such that ax + by = g.
    private static int[] eEuclid(int a, int b) {
        int x = 1, y = 0,  x1 = 0, y1 = 1, a1 = a, b1 = b;
        int t;
        while (b1 != 0) {
            int q = a1 / b1;
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
        return new int[]{a1, x, y};
    }

    private static int rGcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    private static int[] rEEuclid(int a, int b) {
        if (b == 0) {
            return new int[]{a, 1, 0};
        }
        int[] arr = rEEuclid(b, a % b);
        return new int[]{arr[0], arr[2], arr[1] - arr[2] * (a / b)};
    }

}
