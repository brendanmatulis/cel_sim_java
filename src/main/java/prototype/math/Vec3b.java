package prototype.math;

import java.io.Serializable;
import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.BigDecimal.ZERO;
import static prototype.math.Operations.sqrtNewton;

public class Vec3b implements Serializable {
    public BigDecimal x, y, z;


    public Vec3b() {
        x = ZERO.setScale(32, ROUND_FLOOR);
        y = ZERO.setScale(32, ROUND_FLOOR);
        z = ZERO.setScale(32, ROUND_FLOOR);
    }

    public Vec3b(BigDecimal x, BigDecimal y, BigDecimal z) {
        set(x, y, z);
    }

    public Vec3b(double x, double y, double z) {
        this(
                new BigDecimal(x).setScale(32, ROUND_FLOOR),
                new BigDecimal(y).setScale(32, ROUND_FLOOR),
                new BigDecimal(z).setScale(32, ROUND_FLOOR)
        );
    }

    public Vec3b(Vec3b vec3B) {
        this(vec3B.x, vec3B.y, vec3B.z);
    }

    public Vec3b(Vec4B vec4B) {
        this(vec4B.x, vec4B.y, vec4B.z);
    }

    public Vec3b(Vec4f vector4f) {
        this(
                new BigDecimal(vector4f.x).setScale(32, ROUND_FLOOR),
                new BigDecimal(vector4f.y).setScale(32, ROUND_FLOOR),
                new BigDecimal(vector4f.z).setScale(32, ROUND_FLOOR)
        );
    }

    public Vec3b(Vec3f vec3F) {
        this(
                new BigDecimal(vec3F.x).setScale(32, ROUND_FLOOR),
                new BigDecimal(vec3F.y).setScale(32, ROUND_FLOOR),
                new BigDecimal(vec3F.z).setScale(32, ROUND_FLOOR)
        );
    }

    public Vec3b set(BigDecimal x, BigDecimal y, BigDecimal z) {
        this.x = x.setScale(32, ROUND_FLOOR);
        this.y = y.setScale(32, ROUND_FLOOR);
        this.z = z.setScale(32, ROUND_FLOOR);
        return this;
    }

    public Vec3b set(double x, double y, double z) {
        this.x = new BigDecimal(x).setScale(32, ROUND_FLOOR);
        this.y = new BigDecimal(y).setScale(32, ROUND_FLOOR);
        this.z = new BigDecimal(z).setScale(32, ROUND_FLOOR);
        return this;
    }

    public Vec3b set(Vec3b vec3B) {
        this.x = vec3B.x;
        this.y =  vec3B.y;
        this.z = vec3B.z;
        return this;
    }

    public BigDecimal dot(Vec3b vec3B) {
        return Operations.dot(this, vec3B);
    }

    public Vec3b cross(Vec3b vec3B) {
        //In the form:  this x bigVector4
        return Operations.cross(this, vec3B);
    }

    public BigDecimal magnitude() {
        //Note: accuracy not certain. Increase iterations as needed...
        BigDecimal magnitude =
                sqrtNewton(
                        Operations.dot(this, this),
                        30); //iterations

        return magnitude;
    }


    public Vec3b normalize() {
        BigDecimal magnitude = magnitude();
        x = x.divide(magnitude, ROUND_FLOOR);
        y = y.divide(magnitude, ROUND_FLOOR);
        z = z.divide(magnitude, ROUND_FLOOR);
        return this;
    }

    public Vec3b multiply(Matrix4B matrix) {
        //Assume vector has a homogeneous component w
        BigDecimal[] elements = matrix.getElements();
        BigDecimal w = ONE;

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
        return this;
    }

    public Vec3b add(Vec3b vec3b) {
        x = x.add(vec3b.x);
        y = y.add(vec3b.y);
        z = z.add(vec3b.z);
        return this;
    }
}
