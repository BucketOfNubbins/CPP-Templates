
public class FFT {

    // compute the FFT of x[], assuming its length is a power of 2
    public static class FFTItrator {
        int n, m;
        //Lookup tables. Only need to recompute when size of FFT changes.
        double[] cos;
        double[] sin;
        double[] window;

        public FFTItrator(int n) {
            this.n = n;
            this.m = (int) (Math.log(n) / Math.log(2));
            //Make sure n is a power of 2
            if (n != (1 << m))
                throw new RuntimeException("FFT length must be power of 2");
            //precompute tables
            cos = new double[n / 2];
            sin = new double[n / 2];
            for (int i = 0; i < n / 2; i++) {
                cos[i] = Math.cos(-2 * Math.PI * i / n);
                sin[i] = Math.sin(-2 * Math.PI * i / n);
            }
            makeWindow();
        }

        protected void makeWindow() {
            //Make a blackman window:
            //w(n)=0.42-0.5cos{(2*PI*n)/(N-1)}+0.08cos{(4*PI*n)/(N-1)};
            window = new double[n];
            for (int i = 0; i < window.length; i++)
                window[i] = 0.42 - 0.5 * Math.cos(2 * Math.PI * i / (n - 1)) + 0.08 * Math.cos(4 * Math.PI * i / (n - 1));
        }

        public double[] getWindow() {
            return window;
        }

        /************************************************* **************
         * fft.c
         * Douglas L. Jones
         * University of Illinois at Urbana-Champaign
         * January 19, 1992
         * http://cnx.rice.edu/content/m12016/latest/
         *
         * fft: in-place radix-2 DIT DFT of a complex input
         *
         * input:
         * n: length of FFT: must be a power of two
         * m: n = 2**m
         * input/output
         * x: double array of length n with real part of data
         * y: double array of length n with imag part of data
         *
         * Permission to copy and use this program is granted
         * as long as this header is included.
         ************************************************** **************/

        public void fft(double[] x, double[] y) {
            int i, j, k, n1, n2, a;
            double c, s, e, t1, t2;

            //Bit-reverse binary bit flip replacement
            j = 0;
            n2 = n / 2;
            for (i = 1; i < n - 1; i++) {
                n1 = n2;
                while (j >= n1) {
                    j = j - n1;
                    n1 = n1 / 2;
                }
                j = j + n1;
                if (i < j) {
                    t1 = x[i];
                    x[i] = x[j];
                    x[j] = t1;
                    t1 = y[i];
                    y[i] = y[j];
                    y[j] = t1;
                }
            }

            //FFT
            n1 = 0;
            n2 = 1;
            for (i = 0; i < m; i++) {
                n1 = n2;
                n2 = n2 + n2;
                a = 0;

                for (j = 0; j < n1; j++) {
                    c = cos[a];
                    s = sin[a];
                    a += 1 << (m - 1);

                    for (k = j; k < n; k = k + n2) {
                        t1 = c * x[k + n1] - s * y[k + n1];
                        t2 = s * x[k + n1] + c * y[k + n1];
                        x[k + n1] = x[k] - t1;
                        y[k + n1] = y[k] - t2;
                        x[k] = x[k] + t1;
                        y[k] = y[k] + t2;
                    }
                }
            }
        }
    }

    public static void ifft(double[] x, double[] y) {
        int n = x.length;
        // take conjugate
        for (int i = 0; i < n; i++) {
            y[i] = -y[i];
        }
        FFTItrator fft = new FFTItrator(n);
        // compute forward FFT
        fft.fft(x, y);

        // take conjugate again
        for (int i = 0; i < n; i++) {
            y[i] = -y[i];
        }

        // divide by n
        double inv = 1.0 / n;
        for (int i = 0; i < n; i++) {
            x[i] *= inv;
            y[i] *= inv;
        }
    }

    // compute the circular convolution of x and y
    // x and y have the same length of 2^k
    public static void cconvolve(double[] xx, double[] xy, double[] yx, double[] yy) {

        int n = xx.length;
        FFTItrator fftItrator = new FFTItrator(n);

        // compute FFT of each sequence
        fftItrator.fft(xx, xy);
        fftItrator.fft(yx, yy);

        for (int i = 0; i < n; i++) {
            double x = xx[i] * yx[i] - xy[i] * yy[i];
            double y = xx[i] * yy[i] + xy[i] * yx[i];
            xx[i] = x;
            xy[i] = y;
        }

        // compute inverse FFT
        ifft(xx, xy);
    }


    // compute the linear convolution of x and y
    public static double[][] convolve(double[] xx, double[] xy, double[] yx, double[] yy) {
        double[] ax = new double[2 * xx.length];
        double[] ay = new double[2 * xx.length];
        double[] bx = new double[2 * yx.length];
        double[] by = new double[2 * yx.length];
        for (int i = 0; i < xx.length; i++) {
            ax[i] = xx[i];
            ay[i] = xy[i];
            bx[i] = yx[i];
            by[i] = yy[i];
        }
        for (int i = xx.length; i < 2 * xx.length; i++) {
            ax[i] = 0;
            ay[i] = 0;
            bx[i] = 0;
            by[i] = 0;
        }
        cconvolve(ax, ay, bx, by);
        return new double[][]{ax, ay};
    }


}
