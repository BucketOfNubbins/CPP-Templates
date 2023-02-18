public class Geometry2D {

    static double getTheta(double[] a) {
        return Math.atan2(a[1], a[0]);
    }

    static double dotProduct(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1];
    }

    static double crossProduct(double[] a, double[] b) {
        return a[0] * b[1] - a[1] * b[0];
    }

    // project a onto b
    static double[] project(double[] a, double[] b) {
        return scale(dotProduct(a, b) / (magnitude(b) * magnitude(b)), b);
    }

    static double magnitude(double[] vec) {
        return Math.hypot(vec[0], vec[1]);
    }

    static double magnitudeSquared(double[] vec) {
        return vec[0] * vec[0] + vec[1] * vec[1];
    }

    static double[] scale(double s, double[] vec) {
        return new double[]{vec[0] * s, vec[1] * s};
    }

    static double[] add(double[] a, double[] b) {
        return new double[]{a[0] + b[0], a[1] + b[1]};
    }

    // creates new vector from a to b
    static double[] to(double[] a, double[] b) {
        return new double[]{b[0] - a[0], b[1] - a[1]};
    }

    static double triangleArea(double[] a, double[] b) {
        return Math.abs(crossProduct(a, b) / 2);
    }

    // gives the length of the last side of the triangle with side lengths a and b and an angle c
    static double sasSide(double a, double c, double b) {
        return Math.sqrt(a * a + b * b - 2 * a * b * Math.cos(c));
    }

    // Gives the angle of the triangle at the point between a and b
    static double sssTheta(double a, double b, double c) {
        return Math.acos((a * a + b * b - c * c) / (2 * a * b));
    }

    static double polygonArea(double[][] vertices) {
        double area = 0;
        for (int i = 0; i < vertices.length; i++) {
            area += crossProduct(vertices[i], vertices[(i + 1) % vertices.length]);
        }
        return Math.abs(area / 2);
    }

    static boolean segmentIntersection(double[] a, double[] b, double[] p, double[] q) {
        double[] ab = to(a, b);
        double[] ap = to(a, p);
        double[] aq = to(a, q);
        double[] pq = to(p, q);
        double[] pa = to(p, a);
        double[] pb = to(p, b);
        return crossProduct(ab, ap) * crossProduct(ab, aq) < 0 && crossProduct(pq, pa) * crossProduct(pq, pb) < 0;
    }

    // Line goes through a and b. Point p
    static double[] pointLineClosestPoint(double[] p, double[] a, double[] b) {
        double[] ab = to(a, b);
        double[] ap = to(a, p);
        return add(a, project(ap, ab));
    }

    static double[] pointSegmentClosestPoint(double[] p, double[] a, double[] b) {
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
    static double[] lineLineIntersection(double[] a, double[] b, double[] p, double[] q) {
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
    static double[][] segmentSegmentClosestPoints(double[] a, double[] b, double[] p, double[] q) {
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

    // returns the two indices of min and max x points
    static int[] splitConvexPolygon(double[][] polygon) {
        int minX = -1;
        int maxX = -1;
        for (int i = 0; i < polygon.length; i++) {
            if (minX == -1 || polygon[i][0] < polygon[minX][0]) {
                minX = i;
            }
            if (maxX == -1 || polygon[i][0] > polygon[maxX][0]) {
                maxX = i;
            }
        }
        return new int[]{minX, maxX};
    }

    // polygon given in counterclockwise order
    static boolean pointInConvexPolygon(double[][] polygon, int[] indices, double[] p) {
        if (p[0] < polygon[indices[0]][0] || p[0] > polygon[indices[1]][0]) { // Check x bounding interval
            return false;
        }
        // -> bottom chain
        int lo = 0;
        int hi = polygon.length; // exclusive
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (polygon[(indices[0] + mid) % polygon.length][0] < p[0]) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        int firstIndex = (lo - 1 + polygon.length) % polygon.length;
        double[] pa = to(p, polygon[firstIndex]);
        double[] ab = to(polygon[firstIndex], polygon[lo]);
        double bottomCross = crossProduct(pa, ab);

        // <- top chain
        lo = 0;
        hi = polygon.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (polygon[(indices[0] - mid + polygon.length) % polygon.length][0] < p[0]) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        firstIndex = (lo + 1) % polygon.length;
        pa = to(p, polygon[firstIndex]);
        ab = to(polygon[firstIndex], polygon[lo]);
        double topCross = crossProduct(pa, ab);
        return bottomCross >= 0 && topCross <= 0;
    }

    /**
     * Returns the distance between the two specified points.
     *
     * @param a The first point.
     * @param b The second point.
     * @return The distance between the two specified points.
     */
    static double distance(double[] a, double[] b) {
        return Math.hypot(a[0] - b[0], a[1] - b[1]);
    }

    /**
     * Returns the indices of the two farthest (not necessarily distinct) points in the convex hull. Returns null if
     * there are no points in the convex hull.
     *
     * @param convexHull The convex hull.
     * @return The indices of the two farthest (not necessarily distinct) points in the convex hull, or null if there
     *         are no points in the convex hull.
     */
    static int[] farthestPointsIndices(double[][] convexHull) {
        final int n = convexHull.length;
        if (n == 0) {
            return null;
        }

        int[] res = {0, 0};
        double resDistance = 0;
        int l = 0, r = 1;
        while (l < r && r < n) {
            double prevDistance = distance(convexHull[l], convexHull[r - 1]);
            double curDistance = distance(convexHull[l], convexHull[r]);
            double nextDistance = r < n - 1 ? distance(convexHull[l], convexHull[r + 1]) : 0;

            if (prevDistance < curDistance && curDistance > nextDistance) {
                if (resDistance > curDistance) {
                    res = new int[]{l, r};
                    resDistance = curDistance;
                }
                ++l;
            } else {
                ++r;
            }
        }

        return res;
    }
}
