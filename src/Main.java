import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        Main m = new Main();
        System.out.println(m.divisorGame(2));

        br.close();
        bw.close();
    }

    int[] cache = new int[1001];

    public boolean divisorGame(int n) {
        if (cache[n] == 0) {
            boolean win = false;
            for (int i = 1; i < n; i++) {
                if (n % i == 0 && !divisorGame(n - i)) {
                    win = true;
                    break;
                }
            }
            cache[n] = win ? 1 : -1;
        }
        return cache[n] == 1;
    }

}