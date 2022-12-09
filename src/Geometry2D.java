public class Geometry2D {


    private static double dotProduct(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1];
    }

    private static double crossProduct(double[] a, double[] b) {
        return a[0] * b[1] - a[1] * b[0];
    }

    private static double triangleArea(double[] a, double[] b) {
        return Math.abs(crossProduct(a, b) / 2);
    }

    private static double polygonArea(double[][] vertices) {
        double area = 0;
        for (int i = 0; i < vertices.length; i++) {
            area += crossProduct(vertices[i], vertices[(i + 1) % vertices.length]) / 2;
        }
        return Math.abs(area);
    }

    // creates new vector from a to b
    private static double[] difference(double[] a, double[] b) {
        return new double[]{b[0] - a[0], b[1] - a[1]};
    }

    private static boolean segmentIntersection(double[] a, double[] b, double[] p, double[] q) {
        double[] ab = difference(a, b);
        double[] ap = difference(a, p);
        double[] aq = difference(a, q);
        double[] pq = difference(p, q);
        double[] pa = difference(p, a);
        double[] pb = difference(p, b);
        return crossProduct(ab, ap) * crossProduct(ab, aq) < 0 && crossProduct(pq, pa) * crossProduct(pq, pb) < 0;
    }

}
