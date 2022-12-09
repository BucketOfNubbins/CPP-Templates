import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String output = "";
        for (int i = 0; i < 25; i++) {
            output += chars.charAt((int) (Math.random() * chars.length()));
        }
        System.out.println(output);

        br.close();
        bw.close();
    }



}
