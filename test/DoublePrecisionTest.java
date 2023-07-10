import java.math.BigDecimal;

class DoublePrecisionTest {
    private static final BigDecimal acceptableRelativeError = BigDecimal.ONE.divide(BigDecimal.TEN.pow(30), DoublePrecision.MATH_CONTEXT);

    @org.junit.jupiter.api.Test
    void add() {
        for (double[] aa : values) {
            for (double[] bb : values) {
                double[] a = DoublePrecision.create(aa[0], aa[1], new double[2]);
                double[] b = DoublePrecision.create(bb[0], bb[1], new double[2]);
                double[] c = DoublePrecision.add(a, b, new double[2]);
                BigDecimal abd = DoublePrecision.toBigDecimal(a);
                BigDecimal bbd = DoublePrecision.toBigDecimal(b);
                BigDecimal cbd = DoublePrecision.toBigDecimal(c);
                BigDecimal bigResult = abd.add(bbd);
                if (bigResult.signum() == 0) {
                    continue;
                }
                BigDecimal absoluteError = bigResult.subtract(cbd).abs();
                BigDecimal relativeError = absoluteError.divide(bigResult, DoublePrecision.MATH_CONTEXT);
                if (relativeError.compareTo(acceptableRelativeError) > 0) {
                    System.out.println(DoublePrecision.toString(a) + " + " + DoublePrecision.toString(b) + " = " + DoublePrecision.toString(c));
                    System.out.println(abd + " + " + bbd + " = " + bigResult);
                    System.out.println(absoluteError);
                    System.out.println(relativeError);
                    System.out.println();
                }
            }
        }
    }

    @org.junit.jupiter.api.Test
    void sub() {
        for (double[] aa : values) {
            for (double[] bb : values) {
                double[] a = DoublePrecision.create(aa[0], aa[1], new double[2]);
                double[] b = DoublePrecision.create(bb[0], bb[1], new double[2]);
                double[] c = DoublePrecision.subtract(a, b, new double[2]);
                BigDecimal abd = DoublePrecision.toBigDecimal(a);
                BigDecimal bbd = DoublePrecision.toBigDecimal(b);
                BigDecimal cbd = DoublePrecision.toBigDecimal(c);
                BigDecimal bigResult = abd.subtract(bbd);
                if (bigResult.signum() == 0) {
                    continue;
                }
                BigDecimal absoluteError = bigResult.subtract(cbd).abs();
                BigDecimal relativeError = absoluteError.divide(bigResult, DoublePrecision.MATH_CONTEXT);
                if (relativeError.compareTo(acceptableRelativeError) > 0) {
                    System.out.println(DoublePrecision.toString(a) + " - " + DoublePrecision.toString(b) + " = " + DoublePrecision.toString(c));
                    System.out.println(abd + " - " + bbd + " = " + bigResult);
                    System.out.println(absoluteError);
                    System.out.println(relativeError);
                    System.out.println();
                }
            }
        }
    }

    @org.junit.jupiter.api.Test
    void multiply() {
        for (double[] aa : values) {
            for (double[] bb : values) {
                double[] a = DoublePrecision.create(aa[0], aa[1], new double[2]);
                double[] b = DoublePrecision.create(bb[0], bb[1], new double[2]);
                double[] c = DoublePrecision.multiply(a, b, new double[2]);
                BigDecimal abd = DoublePrecision.toBigDecimal(a);
                BigDecimal bbd = DoublePrecision.toBigDecimal(b);
                BigDecimal cbd = DoublePrecision.toBigDecimal(c);
                BigDecimal bigResult = abd.multiply(bbd);
                if (bigResult.signum() == 0) {
                    continue;
                }
                BigDecimal absoluteError = bigResult.subtract(cbd).abs();
                BigDecimal relativeError = absoluteError.divide(bigResult, DoublePrecision.MATH_CONTEXT);
                if (relativeError.compareTo(acceptableRelativeError) > 0) {
                    System.out.println(DoublePrecision.toString(a) + " * " + DoublePrecision.toString(b) + " = " + DoublePrecision.toString(c));
                    System.out.println(abd + " * " + bbd + " = " + bigResult);
                    System.out.println(absoluteError);
                    System.out.println(relativeError);
                    System.out.println();
                }
            }
        }
    }

    @org.junit.jupiter.api.Test
    void divide() {
        for (double[] aa : values) {
            for (double[] bb : values) {
                double[] a = DoublePrecision.create(aa[0], aa[1], new double[2]);
                double[] b = DoublePrecision.create(bb[0], bb[1], new double[2]);
                double[] c = DoublePrecision.divide(a, b, new double[2]);
                BigDecimal abd = DoublePrecision.toBigDecimal(a);
                BigDecimal bbd = DoublePrecision.toBigDecimal(b);
                BigDecimal cbd = DoublePrecision.toBigDecimal(c);
                BigDecimal bigResult = abd.divide(bbd, DoublePrecision.MATH_CONTEXT);
                if (bigResult.signum() == 0) {
                    continue;
                }
                BigDecimal absoluteError = bigResult.subtract(cbd).abs();
                BigDecimal relativeError = absoluteError.divide(bigResult, DoublePrecision.MATH_CONTEXT);
                if (relativeError.compareTo(acceptableRelativeError) > 0) {
                    System.out.println(DoublePrecision.toString(a) + " / " + DoublePrecision.toString(b) + " = " + DoublePrecision.toString(c));
                    System.out.println(abd + " / " + bbd + " = " + bigResult);
                    System.out.println(absoluteError);
                    System.out.println(relativeError);
                    System.out.println();
                }
            }
        }
    }

    @org.junit.jupiter.api.Test
    void sqrt() {
        for (double[] aa : values) {
            if (aa[0] + aa[1] < 0) {
                continue;
            }
            double[] a = DoublePrecision.create(aa[0], aa[1], new double[2]);
            double[] c = DoublePrecision.sqrt(a, new double[2]);
            BigDecimal abd = DoublePrecision.toBigDecimal(a);
            BigDecimal cbd = DoublePrecision.toBigDecimal(c);
            BigDecimal bigResult = abd.sqrt(DoublePrecision.MATH_CONTEXT);
            if (cbd.signum() == 0) {
                continue;
            }
            BigDecimal absoluteError = bigResult.subtract(cbd).abs();
            BigDecimal relativeError = absoluteError.divide(cbd, DoublePrecision.MATH_CONTEXT);
            if (relativeError.compareTo(acceptableRelativeError) > 0) {
                System.out.println("sqrt(" + DoublePrecision.toString(a) + ") = " + DoublePrecision.toString(c));
                System.out.println("sqrt(" + abd + ") = " + bigResult);
                System.out.println(absoluteError);
                System.out.println(relativeError);
                System.out.println();
            }
        }
    }

    private static final double[][] values = new double[][]{
            {1.0e100, 0.0},
            {1.0e50, 0.0},
            {1.0e25, 0.0},
            {1.0e10, 0.0},
            {1.0e8, 0.0},
            {1.0e5, 0.0},
            {1.0e4, 0.0},
            {1.0e3, 0.0},
            {1.0e2, 0.0},
            {1.0e1, 0.0},
            {1.0e0, 0.0},
            {1.0e-1, 0.0},
            {1.0e-2, 0.0},
            {1.0e-3, 0.0},
            {1.0e-4, 0.0},
            {1.0e-5, 0.0},
            {1.0e-8, 0.0},
            {1.0e-10, 0.0},
            {1.0e-25, 0.0},
            {1.0e-50, 0.0},
            {1.0e-100, 0.0},
            {2.36987e100, 0.0},
            {2.36987e50, 0.0},
            {2.36987e25, 0.0},
            {2.36987e10, 0.0},
            {2.36987e8, 0.0},
            {2.36987e5, 0.0},
            {2.36987e4, 0.0},
            {2.36987e3, 0.0},
            {2.36987e2, 0.0},
            {2.36987e1, 0.0},
            {2.36987e0, 0.0},
            {2.36987e-1, 0.0},
            {2.36987e-2, 0.0},
            {2.36987e-3, 0.0},
            {2.36987e-4, 0.0},
            {2.36987e-5, 0.0},
            {2.36987e-8, 0.0},
            {2.36987e-10, 0.0},
            {2.36987e-25, 0.0},
            {2.36987e-50, 0.0},
            {2.36987e-100, 0.0},
            {-1.0e100, 0.0},
            {-1.0e50, 0.0},
            {-1.0e25, 0.0},
            {-1.0e10, 0.0},
            {-1.0e8, 0.0},
            {-1.0e5, 0.0},
            {-1.0e4, 0.0},
            {-1.0e3, 0.0},
            {-1.0e2, 0.0},
            {-1.0e1, 0.0},
            {-1.0e0, 0.0},
            {-1.0e-1, 0.0},
            {-1.0e-2, 0.0},
            {-1.0e-3, 0.0},
            {-1.0e-4, 0.0},
            {-1.0e-5, 0.0},
            {-1.0e-8, 0.0},
            {-1.0e-10, 0.0},
            {-1.0e-25, 0.0},
            {-1.0e-50, 0.0},
            {-1.0e-100, 0.0},
            {-2.36987e100, 0.0},
            {-2.36987e50, 0.0},
            {-2.36987e25, 0.0},
            {-2.36987e10, 0.0},
            {-2.36987e8, 0.0},
            {-2.36987e5, 0.0},
            {-2.36987e4, 0.0},
            {-2.36987e3, 0.0},
            {-2.36987e2, 0.0},
            {-2.36987e1, 0.0},
            {-2.36987e0, 0.0},
            {-2.36987e-1, 0.0},
            {-2.36987e-2, 0.0},
            {-2.36987e-3, 0.0},
            {-2.36987e-4, 0.0},
            {-2.36987e-5, 0.0},
            {-2.36987e-8, 0.0},
            {-2.36987e-10, 0.0},
            {-2.36987e-25, 0.0},
            {-2.36987e-50, 0.0},
            {-2.36987e-100, 0.0},
            {3141592653589793e-15, 2384626433832795e-31},
            {2718281828459045e-15, 2353602874713526e-31},
            {1618033988749894e-15, 8482045868343656e-31},
            {-3141592653589793e-15, 2384626433832795e-31},
            {-2718281828459045e-15, 2353602874713526e-31},
            {-1618033988749894e-15, 8482045868343656e-31},
            {1e9 + 7, 1e-20}
    };

}
