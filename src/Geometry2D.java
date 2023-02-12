public class Geometry2D {

    private static double dotProduct(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1];
    }

    private static double crossProduct(double[] a, double[] b) {
        return a[0] * b[1] - a[1] * b[0];
    }

    // project a onto b
    private static double[] project(double[] a, double[] b) {
        return scale(dotProduct(a, b) / (magnitude(b) * magnitude(b)), b);
    }

    private static double triangleArea(double[] a, double[] b) {
        return Math.abs(crossProduct(a, b) / 2);
    }

    // gives the length of the last side of the triangle with side lengths a and b and an angle c
    private static double sasSide(double a, double c, double b) {
        return Math.sqrt(a * a + b * b - 2 * a * b * Math.cos(c));
    }

    // Gives the angle of the triangle at the point between a and b
    private static double sssTheta(double a, double b, double c) {
        return Math.acos((a * a + b * b - c * c) / (2 * a * b));
    }

    private static double polygonArea(double[][] vertices) {
        double area = 0;
        for (int i = 0; i < vertices.length; i++) {
            area += crossProduct(vertices[i], vertices[(i + 1) % vertices.length]);
        }
        return Math.abs(area / 2);
    }

    private static double magnitude(double[] vec) {
        return Math.hypot(vec[0], vec[1]);
    }

    private static double magnitudeSquared(double[] vec) {
        return vec[0] * vec[0] + vec[1] * vec[1];
    }

    private static double[] scale(double s, double[] vec) {
        return new double[]{vec[0] * s, vec[1] * s};
    }

    private static double[] add(double[] a, double[] b) {
        return new double[]{a[0] + b[0], a[1] + b[1]};
    }

    // creates new vector from a to b
    private static double[] to(double[] a, double[] b) {
        return new double[]{b[0] - a[0], b[1] - a[1]};
    }

    private static boolean segmentIntersection(double[] a, double[] b, double[] p, double[] q) {
        double[] ab = to(a, b);
        double[] ap = to(a, p);
        double[] aq = to(a, q);
        double[] pq = to(p, q);
        double[] pa = to(p, a);
        double[] pb = to(p, b);
        return crossProduct(ab, ap) * crossProduct(ab, aq) < 0 && crossProduct(pq, pa) * crossProduct(pq, pb) < 0;
    }

    // Line goes through a and b. Point p
    private static double[] pointLineClosestPoint(double[] p, double[] a, double[] b) {
        double[] ab = to(a, b);
        double[] ap = to(a, p);
        return add(a, project(ap, ab));
    }

    private static double[] pointSegmentClosestPoint(double[] p, double[] a, double[] b) {
        double[] t = pointLineClosestPoint(p, a, b);
        double[] at = to(a, t);
        double[] ap = to(a, b);
        if (dotProduct(at, ap) > 0 && magnitude(at) < magnitude(ap)) {
            return t;
        }
        if (magnitude(to(a, p)) < magnitude(to(b, p))) {
            return a;
        } else {
            return b;
        }
    }

    // intersection point of two lines
    // TODO: Verify
    private static double[] lineLineIntersection(double[] a, double[] b, double[] p, double[] q) {
        double denominator = (a[0] - b[0]) * (p[1] - q[1]) - (a[1] - b[1]) * (p[0] - q[0]);
        if (denominator == 0) {
            return null; // parallel or coincident
        }
        double x = (a[0] * b[1] - a[1] * b[0]) * (p[0] - q[0]) - (a[1] - b[1]) * (p[0] * q[1] - p[1] * q[0]);
        x /= denominator;
        double y = (a[0] * b[1] - a[1] * b[0]) * (p[1] - q[1]) - (a[0] - b[0]) * (p[0] * q[1] - p[1] * q[0]);
        y /= denominator;
        return new double[]{x, y};
    }

    // closest point(s) between two segments
    private static double[][] segmentSegmentClosestPoints(double[] a, double[] b, double[] p, double[] q) {
        if (segmentIntersection(a, b, p, q)) {
            return new double[][]{lineLineIntersection(a, b, p, q)};
        }
        double[] ac = pointSegmentClosestPoint(a, p, q);
        double[] bc = pointSegmentClosestPoint(b, p, q);
        double[] abc;
        if (magnitude(to(a, ac)) < magnitude(to(b, bc))) {
            abc = ac;
        } else {
            abc = bc;
        }
        double[] pc = pointSegmentClosestPoint(p, a, b);
        double[] qc = pointSegmentClosestPoint(q, a, b);
        double[] pqc;
        if (magnitude(to(p, pc)) < magnitude(to(q, qc))) {
            pqc = pc;
        } else {
            pqc = qc;
        }
        return new double[][]{abc, pqc};
    }

}
