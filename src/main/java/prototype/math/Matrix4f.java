package prototype.math;

import java.math.BigDecimal;
import java.nio.FloatBuffer;

import static prototype.math.GetMatrix.*;
import static prototype.utils.GeneralUtils.createBuffer;

public class Matrix4f {
    private float[] elements = new float[16];

    public Matrix4f() {

    }

    public Matrix4f(float[] elements) { this.elements = elements; }

    public Matrix4f(Matrix4f matrix) { elements = matrix.getElements(); }

    public Matrix4f(Matrix4B matrix) {
        BigDecimal[] elements = matrix.getElements();

        for (int i = 0; i < 16; i++) {
            this.elements[i] = elements[i].floatValue();
        }
    }

    public Matrix4f setElements(float[] elements) {
        this.elements = elements;
        return this;
    }

    public Matrix4f setIdentity() {
        elements = getIdentity4f().getElements();
        return this;
    }

    public float[] getElements() { return elements; }

    public Matrix4f multiply(Matrix4f matrix) {
        Matrix4f result = new Matrix4f();

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

    public Matrix4f translate(float x, float y, float z) {
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

    public Matrix4f translate(Vec3f vector) {
        return translate(vector.x, vector.y, vector.z);
    }

    public Matrix4f rotateX(float degrees) {
        return multiply(getRotationX4f(degrees));
    }

    public Matrix4f rotateY(float degrees) {
        return multiply(getRotationY4f(degrees));
    }

    public Matrix4f rotateZ(float degrees) {
        return multiply(getRotationZ4f(degrees));
    }

    public Matrix4f rotate(float x, float y, float z) {
        return multiply(getRotation4f(x, y, z));
    }

    public Matrix4f rotate(Vec3f vec3F) {
        return rotate(
                vec3F.x,
                vec3F.y,
                vec3F.z
        );
    }

    public Matrix4f scale(float scaleX, float scaleY, float scaleZ) {
        Matrix4f scale_mat = new Matrix4f();
        scale_mat.elements[0 + 0 * 4] = scaleX;
        scale_mat.elements[1 + 1 * 4] = scaleY;
        scale_mat.elements[2 + 2 * 4] = scaleZ;
        scale_mat.elements[3 + 3 * 4] = 1;

        multiply(scale_mat);
        return this;
    }

    public Matrix4f scale(float scale) {
        return scale(scale, scale, scale);
    }

    public Matrix4f scale(Vec3f vec3F) {
        return scale(vec3F.x, vec3F.y, vec3F.z);
    }

    public FloatBuffer toFloatBuffer() {
        return createBuffer(elements);
    }
}
