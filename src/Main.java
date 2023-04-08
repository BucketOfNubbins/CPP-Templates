import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class Main {

    static final Integer identity = 0;
    static final BinaryOperator<Integer> combine = Integer::sum;
    static final BiFunction<Integer, int[], Integer> rangeCombine = (I, A) -> (A[1] - A[0] + 1) * I;

    static final int size = 5000000;
    static final int maxValue = Integer.MAX_VALUE / size;
    static final int operations = 1000;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));


        Integer[] arr = new Integer[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * maxValue);
        }
        long timeA = 0;
        long timeB = 0;
        timeA -= System.nanoTime();
        SegmentTreeGeneric<Integer> a = new SegmentTreeGeneric<>(arr, identity, combine, rangeCombine);
        timeA += System.nanoTime();
        timeB -= System.nanoTime();
        ConciseIterativeSegmentTree b = new ConciseIterativeSegmentTree(arr.length);
        timeB += System.nanoTime();
        for (int i = 0; i < arr.length; i++) {
            timeB -= System.nanoTime();
            b.update(i, arr[i]);
            timeB += System.nanoTime();
        }
        for (int i = 0; i < operations; i++) {
            int l = (int) (Math.random() * arr.length);
            int r = l + (int) (Math.random() * (arr.length - l));
            if (Math.random() < .6) {
                timeA -= System.nanoTime();
                int x = a.query(l, r);
                timeA += System.nanoTime();
                timeB -= System.nanoTime();
                int y = b.query(l, r);
                timeB += System.nanoTime();
                if (x != y) {
                    System.out.println(Arrays.toString(arr));
                    System.out.println(l + ", " + r);
                    System.out.println(x);
                    System.out.println(y);
                }
            } else {
                int v = (int) (Math.random() * maxValue);
                timeA -= System.nanoTime();
                a.update(l, r, v);
                timeA += System.nanoTime();
                timeB -= System.nanoTime();
                for (int j = l; j <= r; j++) {
                    b.update(j, v);
                }
                timeB += System.nanoTime();
            }
        }
        System.out.println(timeA * 1e-9);
        System.out.println(timeB * 1e-9);


//        try {
//            System.out.println(generate(32));
//        } catch (Exception e) {
//            // Do nothing
//        }


        br.close();
        bw.close();
    }

    private static final char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ`1234567890-=~!@#$%^&*()_+[]\\{}|;':\",./<>?".toCharArray();
    private static final char[] greekLower = "αβγδεζηθικλμνξοπρςστυφχψω".toCharArray();
    private static final char[] greekUpper = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡ_ΣΤΥΦΧΨΩ".toCharArray();


    private static String generate(int l) throws NoSuchAlgorithmException {
        SecureRandom r = SecureRandom.getInstanceStrong();
        char[] string = new char[l];
        for (int i = 0; i < l; i++) {
            string[i] = chars[r.nextInt(chars.length)];
        }
        return new String(string);
    }


}
