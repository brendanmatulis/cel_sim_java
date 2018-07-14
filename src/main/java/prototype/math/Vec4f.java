package prototype.math;

public class Vec4f {
    public float x, y, z, w;

    public Vec4f() {
        this(0, 0, 0);
    }

    public Vec4f(float x, float y, float z, float w) {
        set(x, y, z, w);
    }

    public Vec4f(float x, float y, float z) {
        this(x, y, z, 1);
    }

    public Vec4f(Vec4f vector4f) {
        this(vector4f.x, vector4f.y, vector4f.z, vector4f.w);
    }

    public Vec4f(Vec3f vec3F) {
        this(vec3F.x, vec3F.y, vec3F.z);
    }

    public Vec4f set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vec4f set(float x, float y, float z) {
        return set(x, y, z, 1);
    }

    public float dot(Vec4f vector4f) {
        return Operations.dot(this, vector4f);
    }

    public Vec4f cross(Vec4f vector4f) {
        //In the form:  this x vector4f
        return Operations.cross(this, vector4f);
    }

    public float magnitude() {
        float magnitude = (float) Math.sqrt(
              Operations.dot(this, this)
        );

        return magnitude;
    }

    public Vec4f normalize() {
        float magnitude = magnitude();
        x /= magnitude;
        y /= magnitude;
        z /= magnitude;
        w /= magnitude;
        return this;
    }

    public Vec4f multiply(Matrix4f matrix) {
        float[] elements = matrix.getElements();
        x =
           x * elements[0 + 0 * 4] +
           y * elements[0 + 1 * 4] +
           z * elements[0 + 2 * 4] +
           w * elements[0 + 3 * 4];

        y =
           x * elements[1 + 0 * 4] +
           y * elements[1 + 1 * 4] +
           z * elements[1 + 2 * 4] +
           w * elements[1 + 3 * 4];

        z =
           x * elements[2 + 0 * 4] +
           y * elements[2 + 1 * 4] +
           z * elements[2 + 2 * 4] +
           w * elements[2 + 3 * 4];

        w =
           x * elements[3 + 0 * 4] +
           y * elements[3 + 1 * 4] +
           z * elements[3 + 2 * 4] +
           w * elements[3 + 3 * 4];

        return this;
    }
}
