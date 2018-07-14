package prototype.math;

import static prototype.math.GetMatrix.*;

public class Vec3f {
    public float x, y, z;

    public Vec3f() {
        this(0, 0, 0);
    }

    public Vec3f(float x, float y, float z) {
        set(x, y, z);
    }

    public Vec3f(Vec3f vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vec3f(Vec3d vec) {
        this((float) vec.x, (float) vec.y, (float) vec.z);
    }

    public Vec3f(Vec4f vector4f) {
        this(vector4f.x, vector4f.y, vector4f.z);
    }

    public Vec3f(Vec3b vec3B) {
        set(vec3B);
    }

    public Vec3f set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vec3f set(Vec3f vec3f) {
        return set(vec3f.x, vec3f.y, vec3f.z);
    }

    public Vec3f set(Vec3b vec3B) {
        this.x = vec3B.x.floatValue();
        this.y = vec3B.y.floatValue();
        this.z = vec3B.z.floatValue();
        return this;
    }

    public float dot(Vec3f vec3F) {
        return Operations.dot(this, vec3F);
    }

    public Vec3f cross(Vec3f vec3F) {
        //In the form:  this x vector4f
        return Operations.cross(this, vec3F);
    }

    public float magnitude() {
        float magnitude = (float) Math.sqrt(
                Operations.dot(this, this)
        );

        return magnitude;
    }

    public Vec3f normalize() {
        float magnitude = magnitude();
        if (magnitude != 0) {
            x /= magnitude;
            y /= magnitude;
            z /= magnitude;
        }
        return this;
    }

    public Vec3f multiply(Matrix4f matrix) {
        float[] elements = matrix.getElements();
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

    public Vec3f multiply(float scalar) {
        set(Operations.multiply(scalar, this));
        return this;
    }

    public Vec3f negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Vec3f zero() {
        x = 0;
        y = 0;
        z = 0;
        return this;
    }

    public Vec3f add(Vec3f vec3F) {
        x += vec3F.x;
        y += vec3F.y;
        z += vec3F.z;
        return this;
    }

    public Vec3f subtract(Vec3f vec3F) {
        x -= vec3F.x;
        y -= vec3F.y;
        z -= vec3F.z;
        return this;
    }

    public Vec3f translate(float x, float y, float z) {
        multiply(getTranslation4f(x, y, z));
        return this;
    }

    public Vec3f translate(Vec3f vec3F) {
        multiply(getTranslation4f(vec3F));
        return this;
    }

    public Vec3f rotateX(float degrees) {
        multiply(getRotationX4f(degrees));
        return this;
    }

    public Vec3f rotateY(float degrees) {
        multiply(getRotationY4f(degrees));
        return this;
    }

    public Vec3f rotateZ(float degrees) {
        multiply(getRotationZ4f(degrees));
        return this;
    }

    public Vec3f scale(float scale) {
        multiply(getScale4f(scale));
        return this;
    }

    public Vec3f scale(float scaleX, float scaleY, float scaleZ) {
        multiply(getScale4f(scaleX, scaleY, scaleZ));
        return this;
    }

    public Vec3f scale(Vec3f vec3F) {
        multiply(getScale4f(vec3F));
        return this;
    }
}
