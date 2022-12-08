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

}
