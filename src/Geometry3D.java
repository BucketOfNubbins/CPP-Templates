public class Geometry3D {

    // Volume of a Cone
    private static double coneVolume(double radius, double height) {
        return Math.PI * radius * radius * height / 3;
    }

    // Volume of a Cylinder
    private static double cylinderVolume(double radius, double height) {
        return Math.PI * radius * radius * height;
    }


}
