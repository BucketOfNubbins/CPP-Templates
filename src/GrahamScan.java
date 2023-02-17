import java.util.*;

public class GrahamScan {

    static Comparator<double[]> compareTheta = Comparator.comparingDouble(o -> o[2]);
    static Comparator<double[]> compareThetaThenDist = compareTheta.thenComparing(o -> o[3]);

    /**
     * @param points points[0] = x
     *               points[1] = y
     *               points[2] = angle from reference point
     *               points[3] = dist from reference point
     * @return list of points on convex hull
     */
    public static double[][] grahamScan(double[][] points) {
        Arrays.sort(points, compareThetaThenDist);
        Stack<double[]> convexHull = new Stack<>();
        for (double[] point : points) {
            convexHull.push(point);
            while (!validateEnd(convexHull)) {
                double[] tmp = convexHull.pop();
                convexHull.pop();
                convexHull.push(tmp);
            }
        }
        if (convexHull.size() == 2 && convexHull.get(0)[0] == convexHull.get(1)[0] && convexHull.get(0)[1] == convexHull.get(1)[1]) {
            convexHull.pop();
        }
        return convexHull.toArray(new double[convexHull.size()][]);
    }

    private static boolean validateEnd(Stack<double[]> convexHull) {
        int size = convexHull.size();
        if (size < 3) {
            return true;
        }
        double[] ab = Geometry2D.to(convexHull.get(size - 3), convexHull.get(size - 2));
        double[] bc = Geometry2D.to(convexHull.get(size - 2), convexHull.get(size - 1));
        double cross = Geometry2D.crossProduct(ab, bc);
        return cross > 0; // co-linear if == 0
    }
}
