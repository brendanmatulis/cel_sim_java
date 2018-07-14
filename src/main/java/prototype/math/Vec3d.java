package prototype.math;

import static prototype.math.GetMatrix.*;

public class Vec3d {
    public double x, y, z;

    public Vec3d() {
        this(0, 0, 0);
    }

    public Vec3d(double x, double y, double z) {
        set(x, y, z);
    }

    public Vec3d(Vec3d vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vec3d set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vec3d set(Vec3d vec) {
        return set(vec.x, vec.y, vec.z);
    }

    public double dot(Vec3d vec) {
        return Operations.dot(this, vec);
    }

    public Vec3d cross(Vec3d vec) {
        //In the form:  this x vector4f
        return Operations.cross(this, vec);
    }

    public double magnitude() {
        double magnitude = Math.sqrt(
                Operations.dot(this, this)
        );

        return magnitude;
    }

    public Vec3d normalize() {
        double magnitude = magnitude();
        if (magnitude != 0) {
            x /= magnitude;
            y /= magnitude;
            z /= magnitude;
        }
        return this;
    }

    public Vec3d multiply(Matrix4d matrix) {
        double[] elements = matrix.getElements();
        x =
           x * elements[0 + 0 * 4] +
           y * elements[0 + 1 * 4] +
           z * elements[0 + 2 * 4] +
           1 * elements[0 + 3 * 4];

        y =
           x * elements[1 + 0 * 4] +
           y * elements[1 + 1 * 4] +
           z * elements[1 + 2 * 4] +
           1 * elements[1 + 3 * 4];

        z =
            x * elements[2 + 0 * 4] +
            y * elements[2 + 1 * 4] +
            z * elements[2 + 2 * 4] +
            1 * elements[2 + 3 * 4];

        return this;
    }

    public Vec3d multiply(double scalar) {
        set(Operations.multiply(scalar, this));
        return this;
    }

    public Vec3d negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Vec3d zero() {
        x = 0;
        y = 0;
        z = 0;
        return this;
    }

    public Vec3d add(Vec3d vec) {
        x += vec.x;
        y += vec.y;
        z += vec.z;
        return this;
    }

    public Vec3d subtract(Vec3d vec) {
        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        return this;
    }

    public Vec3d translate(double x, double y, double z) {
        multiply(getTranslation4d(x, y, z));
        return this;
    }

    public Vec3d translate(Vec3d vec) {
        multiply(getTranslation4d(vec));
        return this;
    }

    public Vec3d rotateX(double degrees) {
        multiply(getRotationX4d(degrees));
        return this;
    }

    public Vec3d rotateY(double degrees) {
        multiply(getRotationY4d(degrees));
        return this;
    }

    public Vec3d rotateZ(double degrees) {
        multiply(getRotationZ4d(degrees));
        return this;
    }
}
