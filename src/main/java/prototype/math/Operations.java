package prototype.math;

import org.apfloat.Apfloat;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static java.math.BigDecimal.ROUND_FLOOR;
import static org.apfloat.ApfloatMath.sqrt;

public class Operations {
    //VECTOR OPERATIONS:

    //DOT PRODUCT
    public static float dot(Vec4f a, Vec4f b) {
        float result =
                a.x * b.x +
                a.y * b.y +
                a.z * b.z +
                a.w * a.w;
        return result;
    }

    public static float dot(Vec3f a, Vec3f b) {
        float result =
                a.x * b.x +
                a.y * b.y +
                a.z * b.z;
        return result;
    }

    public static double dot(Vec3d a, Vec3d b) {
        double result =
                a.x * b.x +
                a.y * b.y +
                a.z * b.z;
        return result;
    }

    public static BigDecimal dot(Vec4B a, Vec4B b) {
        BigDecimal result =
                a.x     .multiply   (b.x)   .add(
                a.y     .multiply   (b.y))  .add(
                a.z     .multiply   (b.z))  .add(
                a.w     .multiply   (b.w)
                );
        return result;
    }

    public static BigDecimal dot(Vec3b a, Vec3b b) {
        BigDecimal result =
                a.x     .multiply   (b.x)   .add(
                a.y     .multiply   (b.y))  .add(
                a.z     .multiply   (b.z)
                );
        return result;
    }

    //CROSS PRODUCT
    public static Vec4f cross(Vec4f a, Vec4f b) {
        Vec4f result = new Vec4f();

        result.x = (a.y * b.z) - (a.z * b.y);
        result.y = (a.z * b.x) - (a.x * b.z);
        result.z = (a.x * b.y) - (a.y * b.x);
        result.w = 1;
        return result;
    }

    public static Vec3f cross(Vec3f a, Vec3f b) {
        Vec3f result = new Vec3f();

        result.x = (a.y * b.z) - (a.z * b.y);
        result.y = (a.z * b.x) - (a.x * b.z);
        result.z = (a.x * b.y) - (a.y * b.x);
        return result;
    }

    public static Vec3d cross(Vec3d a, Vec3d b) {
        Vec3d result = new Vec3d();

        result.x = (a.y * b.z) - (a.z * b.y);
        result.y = (a.z * b.x) - (a.x * b.z);
        result.z = (a.x * b.y) - (a.y * b.x);
        return result;
    }

    public static Vec4B cross(Vec4B a, Vec4B b) {
        Vec4B result = new Vec4B();
        result.x = (a.y    .multiply   (b.z))  .subtract
                   (a.z    .multiply   (b.y));

        result.y = (a.z    .multiply   (b.x))  .subtract
                   (a.x    .multiply   (b.z));

        result.y = (a.x    .multiply   (b.y))  .subtract
                   (a.y    .multiply   (b.x));

        //w already set to one in constructor
        return result;
    }

    public static Vec3b cross(Vec3b a, Vec3b b) {
        Vec3b result = new Vec3b();
        result.x = (a.y    .multiply   (b.z))  .subtract
                   (a.z    .multiply   (b.y));

        result.y = (a.z    .multiply   (b.x))  .subtract
                   (a.x    .multiply   (b.z));

        result.y = (a.x    .multiply   (b.y))  .subtract
                   (a.y    .multiply   (b.x));

        return result;
    }

    //SCALAR MULTIPLICATION
    public static Vec3f multiply(float scalar, Vec3f vec) {
        return new Vec3f(
                vec.x * scalar,
                vec.y * scalar,
                vec.z * scalar
        );
    }

    public static Vec3d multiply(double scalar, Vec3d vec) {
        return new Vec3d(
                vec.x * scalar,
                vec.y * scalar,
                vec.z * scalar
        );
    }

    public static Vec2f multiply(float scalar, Vec2f vector2f) {
        return new Vec2f(
                vector2f.x * scalar,
                vector2f.y * scalar
        );
    }

    public static Vec3b multiply(BigDecimal scalar, Vec3b vec3b) {
        return new Vec3b(
                    vec3b.x.multiply(scalar),
                    vec3b.y.multiply(scalar),
                    vec3b.z.multiply(scalar)
                );
    }

    public static Vec3b multiply(double scalar, Vec3b vec3b) {
        BigDecimal bigScalar = new BigDecimal(scalar).setScale(32, ROUND_FLOOR);
        return multiply(bigScalar, vec3b);
    }

    //NORMALIZE
    public static Vec4f normalize(Vec4f vector4f) {
        Vec4f normalized_vec = new Vec4f(vector4f);
        normalized_vec.normalize();
        return normalized_vec;
    }

    public static Vec3f normalize(Vec3f vec) {
        Vec3f normalized_vec = new Vec3f(vec);
        normalized_vec.normalize();
        return normalized_vec;
    }

    public static Vec3d normalize(Vec3d vec) {
        Vec3d normalized_vec = new Vec3d(vec);
        normalized_vec.normalize();
        return normalized_vec;
    }

    public static Vec3b normalize(Vec3b vec3b) {
        BigDecimal mag = magnitude(vec3b);
        return new Vec3b(
                vec3b.x.divide(mag, ROUND_FLOOR),
                vec3b.x.divide(mag, ROUND_FLOOR),
                vec3b.x.divide(mag, ROUND_FLOOR)
        );
    }

    public static BigDecimal magnitude(Vec3b vec3b) {
        BigDecimal dotproduct = dot(vec3b, vec3b);
        Apfloat mag = new Apfloat(dotproduct);
        mag = sqrt(mag);
        return new BigDecimal(mag.toString()).setScale(32, ROUND_FLOOR);
    }

    //ADD AND SUBTRACT
    public static Vec3f add(Vec3f a, Vec3f b) {
        Vec3f result = new Vec3f();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        result.z = a.z + b.z;
        return result;
    }

    public static Vec3f subtract(Vec3f a, Vec3f b) {
        Vec3f result = new Vec3f();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        result.z = a.z - b.z;
        return result;
    }

    public static Vec3d add(Vec3d a, Vec3d b) {
        Vec3d result = new Vec3d();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        result.z = a.z + b.z;
        return result;
    }

    public static Vec3d subtract(Vec3d a, Vec3d b) {
        Vec3d result = new Vec3d();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        result.z = a.z - b.z;
        return result;
    }

    public static Vec2f add(Vec2f a, Vec2f b) {
        Vec2f result = new Vec2f();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        return result;
    }

    public static Vec2f subtract(Vec2f a, Vec2f b) {
        Vec2f result = new Vec2f();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        return result;
    }

    public static Vec3b add(Vec3b a, Vec3b b) {
        Vec3b result = new Vec3b(
                a.x.add(b.x),
                a.y.add(b.y),
                a.z.add(b.z)
        );
        return result;
    }

    public static Vec3b subtract(Vec3b a, Vec3b b) {
        Vec3b result = new Vec3b(
                a.x.subtract(b.x),
                a.y.subtract(b.y),
                a.z.subtract(b.z)
        );
        return result;
    }

    //NEGATE
    public static Vec3f negate(Vec3f vec) {
        return new Vec3f(-vec.x, -vec.y, -vec.z);
    }

    public static Vec3d negate(Vec3d vec) {
        return new Vec3d(-vec.x, -vec.y, -vec.z);
    }

    //PRINT VECTORS
    public static String printVector4f(Vec4f vector4f) {
        StringBuilder result = new StringBuilder();
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        result
                .append(numberFormat.format(vector4f.x)).append(' ')
                .append(numberFormat.format(vector4f.y)).append(' ')
                .append(numberFormat.format(vector4f.z)).append(' ')
                .append(numberFormat.format(vector4f.w));

        return result.toString();
    }

    public static String printVector3f(Vec3f vec3F) {
        StringBuilder result = new StringBuilder();
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        result
                .append(numberFormat.format(vec3F.x)).append(' ')
                .append(numberFormat.format(vec3F.y)).append(' ')
                .append(numberFormat.format(vec3F.z));

        return result.toString();
    }

    //MATRIX OPERATIONS

    //MULTIPLY MATRICES
    //In the form AB = result
    public static Matrix4f multiply(Matrix4f A, Matrix4f B) {
        return (new Matrix4f(B)).multiply(A);
    }

    //PRINT MATRICES
    public static String printMatrix4f(Matrix4f matrix) {
        StringBuilder result = new StringBuilder();
        float[] elements = matrix.getElements();

        DecimalFormat numberFormat = new DecimalFormat("#.00");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float element = elements[i + j * 4];
                if (element >= 1f)
                    result.append(' ').append(numberFormat.format(element)).append(' ');
                else if (element <= -1f)
                    result.append(numberFormat.format(element)).append(' ');
                else if (element < 1 && element > 0)
                    result.append("  ").append(numberFormat.format(element)).append(' ');
                else if (element < 0 && element > -1)
                    result.append(' ').append(numberFormat.format(element)).append(' ');
                else
                    result.append(' ').append('0').append(numberFormat.format(element)).append(' ');
            }
            result.append('\n');
        }

        return result.toString();
    }

    public static String printBigMatrix(Matrix4B matrix) {
        StringBuilder result = new StringBuilder();
        BigDecimal[] elements = matrix.getElements();

        DecimalFormat numberFormat = new DecimalFormat("#.0000");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float element = elements[i + j * 4].floatValue();
                if (element >= 1f)
                    result.append(' ').append(numberFormat.format(element)).append(' ');
                else if (element <= -1f)
                    result.append(numberFormat.format(element)).append(' ');
                else if (element < 1 && element > 0)
                    result.append("  ").append(numberFormat.format(element)).append(' ');
                else if (element < 0 && element > -1)
                    result.append(' ').append(numberFormat.format(element)).append(' ');
                else
                    result.append(' ').append('0').append(numberFormat.format(element)).append(' ');
            }
            result.append('\n');
        }

        return result.toString();
    }

    //VECTOR-MATRIX OPERATION
    public static Vec4f multiply(Vec4f vec, Matrix4f matrix) {
        return (new Vec4f(vec)).multiply(matrix);
    }

    public static Vec3f multiply(Vec3f vec, Matrix4f matrix) {
        return (new Vec3f(vec)).multiply(matrix);
    }

    public static Vec3d multiply(Vec3d vec, Matrix4d matrix) {
        return (new Vec3d(vec)).multiply(matrix);
    }

    //BIG DECIMAL OPERATIONS

    //SQUARE ROOT (NEWTON'S METHOD)
    public static BigDecimal sqrtNewton(BigDecimal num, int iterations) {
        num = num.abs().setScale(32, ROUND_FLOOR);

        double initial_guess = Math.sqrt(num.doubleValue());
        if (initial_guess == 0)
            return BigDecimal.ZERO;

        BigDecimal root = new BigDecimal(initial_guess);
        BigDecimal TWO = new BigDecimal(2);
        for (int i = 0; i < iterations; i++) {
            root = (
                    root    .add
                    (num    .divide    (root, ROUND_FLOOR))
                   ).divide(TWO, ROUND_FLOOR);
        }
        return root;
    }

    //ORBITAL MATH
//    public static Vec3d[] getOrbitalVel(CelObj celObj, double BarycenterMass, double r) {
//        Vec3d velA, velB;
//        //TODO
//    }
}
