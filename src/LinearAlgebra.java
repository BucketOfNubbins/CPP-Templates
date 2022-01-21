import java.io.*;

public class LinearAlgebra {

    public static void main(String[] args) throws IOException {
        // Do stuff
    }

    static class Matrix {
        int m;
        int n;
        double[][] matrix;

        public Matrix(int mm, int nn) {
            m = mm;
            n = nn;
            matrix = new double[m][n];
        }

        public Matrix(int mm, int nn, double diag, double fill) {
            m = mm;
            n = nn;
            matrix = new double[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) {
                        matrix[i][j] = diag;
                    } else {
                        matrix[i][j] = fill;
                    }
                }
            }
        }

        public void multiply(double scalar) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] *= scalar;
                }
            }
        }

        public void add(Matrix a) {
            // this and a must be same size
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] += a.matrix[i][j];
                }
            }
        }

        public Matrix multiply(Matrix a) {
            Matrix x = new Matrix(this.m, a.n);
            for (int i = 0; i < this.m; i++) {
                for (int k = 0; k < a.n; k++) {
                    for (int j = 0; j < this.n; j++) {
                        x.matrix[i][k] += this.matrix[i][j] * a.matrix[j][k];
                    }
                }
            }
            return x;
        }

    }


}
