import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Double-Double precision tricks, all based on the paper by T.J Dekkar
 * https://csclub.uwaterloo.ca/~pbarfuss/dekker1971.pdf
 * <p>
 */
public class DoublePrecision {

    private static final double DOUBLE_MANTISSA_SIZE = 53.0;
    private static final double DOUBLE_CONSTANT = Math.pow(2, (DOUBLE_MANTISSA_SIZE - (DOUBLE_MANTISSA_SIZE / 2.0)) + 1);
    // 3.14159265358979
    // 0.000000000000002384626433832795

    private static final double[] PI = DoublePrecision.create(3141592653589793e-15, 2384626433832795e-31, new double[2]);
    private static final double[] E = DoublePrecision.create(2718281828459045e-15, 2353602874713526e-31, new double[2]);

    public static final MathContext MATH_CONTEXT = new MathContext(256, RoundingMode.HALF_EVEN); // For converting and dealing with BigDecimal
    private static final long SIGN_MASK = 0x8000000000000000L;
    private static final long EXPONENT_MASK = 0x7ff0000000000000L;
    private static final long MANTISSA_MASK = 0x000fffffffffffffL;

    public static double[] create(double[] dest) {
        dest[0] = 0.0;
        dest[1] = 0.0;
        return dest;
    }

    public static double[] create(double x, double[] dest) {
        dest[0] = x;
        dest[1] = 0.0;
        return dest;
    }

    public static double[] create(double x, double xx, double[] dest) {
        dest[0] = x + xx;
        dest[1] = x - dest[0] + xx;
        return dest;
    }

    public static double[] add(double[] x, double[] y, double[] dest) {
        double r = x[0] + y[0];
        double s;
        if (Math.abs(x[0]) > Math.abs(y[0])) {
            s = x[0] - r + y[0] + y[1] + x[1];
        } else {
            s = y[0] - r + x[0] + x[1] + y[1];
        }
        dest[0] = r + s;
        dest[1] = r - dest[0] + s;
        return dest;
    }

    public static double[] subtract(double[] x, double[] y, double[] dest) {
        double r = x[0] - y[0];
        double s;
        if (Math.abs(x[0]) > Math.abs(y[0])) {
            s = x[0] - r - y[0] - y[1] + x[1];
        } else {
            s = -y[0] - r + x[0] + x[1] - y[1];
        }
        dest[0] = r + s;
        dest[1] = r - dest[0] + s;
        return dest;
    }

    public static double[] multiply(double x, double y, double[] dest) {
        double p = x * DOUBLE_CONSTANT;
        double hx = x - p + p;
        double tx = x - hx;
        p = y * DOUBLE_CONSTANT;
        double hy = y - p + p;
        double ty = y - hy;
        p = hx * hy;
        double q = hx * ty + tx * hy;
        dest[0] = p + q;
        dest[1] = p - dest[0] + q + tx * ty;
        return dest;
    }

    public static double[] multiply(double[] x, double[] y, double[] dest) {
        double[] c = multiply(x[0], y[0], new double[2]);
        c[1] += x[0] * y[1] + x[1] * y[0];
        dest[0] = c[0] + c[1];
        dest[1] = c[0] - dest[0] + c[1];
        return dest;
    }

    public static double[] divide(double[] x, double[] y, double[] dest) {
        double c = x[0] / y[0];
        double[] u = multiply(c, y[0], new double[2]);
        double cc = (x[0] - u[0] - u[1] + x[1] - c * y[1]) / y[0];
        dest[0] = c + cc;
        dest[1] = c - dest[0] + cc;
        return dest;
    }

    public static double[] sqrt(double[] x, double[] dest) {
        if (x[0] > 0) {
            double c = Math.sqrt(x[0]);
            double[] u = multiply(c, c, new double[2]);
            double cc = (x[0] - u[0] - u[1] + x[1]) * 0.5 / c;
            dest[0] = c + cc;
            dest[1] = c - dest[0] + cc;
            return dest;
        } else {
            dest[0] = 0.0;
            dest[1] = 0.0;
            return dest;
        }
    }

    public static BigDecimal toBigDecimal(double[] x) {
        BigDecimal a = doubleToBigDecimal(x[0]);
        BigDecimal b = doubleToBigDecimal(x[1]);
        return a.add(b);
    }

    public static String toString(double[] x) {
        return toBigDecimal(x).toString();
    }

    public static BigDecimal doubleToBigDecimal(double x) {
        long bits = Double.doubleToRawLongBits(x);
        long sign = bits & SIGN_MASK;
        int exponent = (int) ((bits & EXPONENT_MASK) >> 52);
        if (exponent == 0) {
            return BigDecimal.ZERO; // ignores subnormal numbers
        }
        long mantissa = (bits & MANTISSA_MASK) + (1L << 52);
        BigDecimal result = BigDecimal.valueOf(mantissa); // implicit bit
        exponent -= (1023 + 52);
        if (exponent < 0) {
            BigDecimal multiply = BigDecimal.TWO.pow(-exponent);
            result = result.divide(multiply, MATH_CONTEXT);
        } else {
            BigDecimal multiply = BigDecimal.TWO.pow(exponent);
            result = result.multiply(multiply);
        }
        if (sign < 0) {
            result = result.multiply(BigDecimal.valueOf(-1L));
        }
        return result;
    }
}
