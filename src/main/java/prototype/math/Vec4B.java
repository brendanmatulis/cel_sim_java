package prototype.math;

import java.io.Serializable;
import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.BigDecimal.ZERO;
import static prototype.math.Operations.sqrtNewton;


public class Vec4B implements Serializable {
    public BigDecimal x, y, z, w;

    public Vec4B() {
        x = ZERO.setScale(32, ROUND_FLOOR);;
        y = ZERO.setScale(32, ROUND_FLOOR);;
        z = ZERO.setScale(32, ROUND_FLOOR);;
        w = ONE.setScale(32, ROUND_FLOOR);;
    }

    public Vec4B(BigDecimal x, BigDecimal y, BigDecimal z, BigDecimal w) {
        set(x, y, z, w);
    }

    public Vec4B(BigDecimal x, BigDecimal y, BigDecimal z) {
        this(x, y, z, ONE);
    }

    public Vec4B(double x, double y, double z, double w) {
        this(
                new BigDecimal(x),
                new BigDecimal(y),
                new BigDecimal(z),
                new BigDecimal(w)
        );
    }

    public Vec4B(double x, double y, double z) {
        this(x, y, z, 1);
    }

    public Vec4B(Vec4B vec4B) {
        this(vec4B.x, vec4B.y, vec4B.z, vec4B.w);
    }

    public Vec4B(Vec3b vec3B) {
        this(vec3B.x, vec3B.y, vec3B.z);
    }

    public Vec4B(Vec4f vector4f) {
        this(
                new BigDecimal(vector4f.x),
                new BigDecimal(vector4f.y),
                new BigDecimal(vector4f.z),
                new BigDecimal(vector4f.w)
        );
    }

    public Vec4B(Vec3f vec3F) {
        this(
                new BigDecimal(vec3F.x),
                new BigDecimal(vec3F.y),
                new BigDecimal(vec3F.z)
        );
    }

    public Vec4B set(BigDecimal x, BigDecimal y, BigDecimal z, BigDecimal w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vec4B set(BigDecimal x, BigDecimal y, BigDecimal z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = ONE;
        return this;
    }

    public Vec4B set(double x, double y, double z) {
        this.x = new BigDecimal(x);
        this.y = new BigDecimal(y);
        this.z = new BigDecimal(z);
        this.w = ONE;
        return this;
    }

    public BigDecimal dot(Vec4B vec4B) {
        return Operations.dot(this, vec4B);
    }

    public Vec4B cross(Vec4B vec4B) {
        //In the form:  this x vec4B
        return Operations.cross(this, vec4B);
    }

    public BigDecimal magnitude() {
                //Note: accuracy not certain. Increase iterations as needed...
        BigDecimal magnitude =
                sqrtNewton(
                        Operations.dot(this, this),
                        30); //iterations

        return magnitude;
    }

    public Vec4B normalize() {
        BigDecimal magnitude = magnitude();
        x = x.divide(magnitude, ROUND_FLOOR);
        y = y.divide(magnitude, ROUND_FLOOR);
        z = z.divide(magnitude, ROUND_FLOOR);
        w = w.divide(magnitude, ROUND_FLOOR);
        return this;
    }

    public Vec4B multiply(Matrix4B matrix) {
        BigDecimal[] elements = matrix.getElements();

        x =
           (x    .multiply   (elements[0 + 0 * 4])   ).add
           (y    .multiply   (elements[0 + 1 * 4])   ).add
           (z    .multiply   (elements[0 + 2 * 4])   ).add
           (w    .multiply   (elements[0 + 3 * 4])   );

        y =
           (x    .multiply   (elements[1 + 0 * 4])   ).add
           (y    .multiply   (elements[1 + 1 * 4])   ).add
           (z    .multiply   (elements[1 + 2 * 4])   ).add
           (w    .multiply   (elements[1 + 3 * 4])   );

        z =
           (x    .multiply   (elements[2 + 0 * 4])   ).add
           (y    .multiply   (elements[2 + 1 * 4])   ).add
           (z    .multiply   (elements[2 + 2 * 4])   ).add
           (w    .multiply   (elements[2 + 3 * 4])   );

        w =
           (x    .multiply   (elements[3 + 0 * 4])   ).add
           (y    .multiply   (elements[3 + 1 * 4])   ).add
           (z    .multiply   (elements[3 + 2 * 4])   ).add
           (w    .multiply   (elements[3 + 3 * 4])   );
        return this;
    }
}
