import java.io.*;

public class LinearAlgebra {

    public static void main(String[] args) throws IOException {
        // do stuff
    }

    static class Vector {
        int n;
        double[] vector;

        public Vector(int nn) {
            n = nn;
            vector = new double[n];
        }

        public Vector(Vector v) {
            n = v.n;
            vector = new double[n];
            if (n >= 0) System.arraycopy(v.vector, 0, vector, 0, n);
        }

        public Vector(double[] v) {
            n = v.length;
            vector = new double[n];
            System.arraycopy(v, 0, vector, 0, n);
        }

        public void addRow(int a, double s, int b) {
            vector[b] += vector[a] * s;
        }

        public void scaleRow(int a, double s) {
            vector[a] *= s;
        }

        public void swap(int a, int b) {
            double t = vector[a];
            vector[a] = vector[b];
            vector[b] = t;
        }
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

        public Matrix(Matrix mat) {
            m = mat.m;
            n = mat.n;
            matrix = new double[m][n];
            for (int i = 0; i < m; i++) {
                if (n >= 0) System.arraycopy(mat.matrix[i], 0, matrix[i], 0, n);
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

        public double[] multiply(double[] v) {
            if (v.length != n) {
                return null;
            }
            double[] result = new double[m];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    result[j] += matrix[i][j] * v[j];
                }
            }
            return result;
        }

        // adds scaled row a to b
        public void addRow(int a, double s, int b) {
            for (int i = 0; i < n; i++) {
                matrix[b][i] += matrix[a][i] * s;
            }
        }

        public void scaleRow(int a, double s) {
            for (int i = 0; i < n; i++) {
                matrix[a][i] *= s;
            }
        }

        public void swap(int a, int b) {
            double[] t = matrix[a];
            matrix[a] = matrix[b];
            matrix[b] = t;
        }

        public Matrix reduce() {
            Matrix copy = new Matrix(this);
            for (int col = 0, row = 0; col < n && row < m; col++, row++) {
                int r = row;
                for (int j = row + 1; j < m; j++) {
                    if (Math.abs(Math.log(copy.matrix[j][col])) < Math.abs(Math.log(copy.matrix[r][col]))) {
                        r = j;
                    }
                }
                if (Math.abs(copy.matrix[r][col]) < 1e-15) {
                    row--;
                    continue;
                }
                if (r != row) {
                    copy.swap(r, row);
                }
                double x = 1 / copy.matrix[row][col];
                copy.scaleRow(row, x);
                for (int j = 0; j < m; j++) {
                    if (row == j) {
                        continue;
                    }
                    double y = -copy.matrix[j][col];
                    copy.addRow(row, y, j);
                }
            }
            return copy;
        }

    }

    // for square matrices
    public static int[][] rotate(int[][] y) {
        int[][] x = new int[y.length][y.length];
        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < y.length; j++) {
                x[i][j] = y[j][y.length - i - 1];
            }
        }
        return x;
    }


}
