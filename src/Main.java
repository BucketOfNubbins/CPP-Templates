import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        try {
            for (int i = 0; i < 10; i++) {
                System.out.println(generate(26));
            }
        } catch (Exception e) {
            // Do nothing
        }


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
