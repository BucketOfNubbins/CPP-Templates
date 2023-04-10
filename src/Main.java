import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Main {


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        try {
            System.out.println(generate(32));
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
