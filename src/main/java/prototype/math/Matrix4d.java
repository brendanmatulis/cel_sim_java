package prototype.math;

import static prototype.math.GetMatrix.*;

public class Matrix4d {
    private double[] elements = new double[16];

    public Matrix4d() {

    }

    public Matrix4d(double[] elements) { this.elements = elements; }

    public Matrix4d(Matrix4d matrix) { elements = matrix.getElements(); }

    public Matrix4d setElements(double[] elements) {
        this.elements = elements;
        return this;
    }

    public Matrix4d setIdentity() {
        for (int i = 0; i < 16; i++) {
            elements[i] = 0;
        }

        elements[0 + 0 * 4] = 1;
        elements[1 + 1 * 4] = 1;
        elements[2 + 2 * 4] = 1;
        elements[3 + 3 * 4] = 1;
        return this;
    }

    public double[] getElements() { return elements; }

    public Matrix4d multiply(Matrix4d matrix) {
        Matrix4d result = new Matrix4d();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int n = 0; n < 4; n++) {
                    result.elements[i + j * 4] += matrix.elements[i + n * 4] * this.elements[n + j * 4];
                }
            }
        }

        this.elements = result.elements;
        return this;
    }

    public Matrix4d translate(double x, double y, double z) {
        //ROW 1
        elements[0 + 0 * 4] += elements[3 + 0 * 4] * x;
        elements[0 + 1 * 4] += elements[3 + 1 * 4] * x;
        elements[0 + 2 * 4] += elements[3 + 2 * 4] * x;
        elements[0 + 3 * 4] += elements[3 + 3 * 4] * x;
        //ROW 2
        elements[1 + 0 * 4] += elements[3 + 0 * 4] * y;
        elements[1 + 1 * 4] += elements[3 + 1 * 4] * y;
        elements[1 + 2 * 4] += elements[3 + 2 * 4] * y;
        elements[1 + 3 * 4] += elements[3 + 3 * 4] * y;
        //ROW 3
        elements[2 + 0 * 4] += elements[3 + 0 * 4] * z;
        elements[2 + 1 * 4] += elements[3 + 1 * 4] * z;
        elements[2 + 2 * 4] += elements[3 + 2 * 4] * z;
        elements[2 + 3 * 4] += elements[3 + 3 * 4] * z;
        return this;
    }

    public Matrix4d translate(Vec3d vec) {
        return translate(vec.x, vec.y, vec.z);
    }

    public Matrix4d rotateX(double degrees) {
        return multiply(getRotationX4d(degrees));
    }

    public Matrix4d rotateY(double degrees) {
        return multiply(getRotationY4d(degrees));
    }

    public Matrix4d rotateZ(double degrees) {
        return multiply(getRotationZ4d(degrees));
    }

    public Matrix4d rotate(double x, double y, double z) {
        return multiply(getRotation4d(x, y, z));
    }

    public Matrix4d rotate(Vec3d vec) {
        return rotate(
                vec.x,
                vec.y,
                vec.z
        );
    }

    public Matrix4d scale(double scaleX, double scaleY, double scaleZ) {
        Matrix4d scale_mat = new Matrix4d();
        scale_mat.elements[0 + 0 * 4] = scaleX;
        scale_mat.elements[1 + 1 * 4] = scaleY;
        scale_mat.elements[2 + 2 * 4] = scaleZ;
        scale_mat.elements[3 + 3 * 4] = 1;

        multiply(scale_mat);
        return this;
    }

    public Matrix4d scale(float scale) {
        return scale(scale, scale, scale);
    }

    public Matrix4d scale(Vec3d vec) {
        return scale(vec.x, vec.y, vec.z);
    }

}
