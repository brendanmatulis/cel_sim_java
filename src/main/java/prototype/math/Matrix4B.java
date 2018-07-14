package prototype.math;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.BigDecimal.ZERO;
import static prototype.math.GetMatrix.*;

public class Matrix4B {
    private BigDecimal[] elements = new BigDecimal[16];

    public Matrix4B() {
        //All elements must be zero for multiplication to work

        for (int i = 0; i < 16; i++) {
            elements[i] = ZERO.setScale(32, ROUND_FLOOR);
        }
    }

    public Matrix4B(BigDecimal[] elements) {
        this.elements = elements;

        for (int i = 0; i < 16; i++) {
            if (elements[i] == null)
                elements[i] = ZERO.setScale(32, ROUND_FLOOR);
        }
    }

    public Matrix4B(Matrix4B matrix) { elements = matrix.getElements(); }

    public Matrix4B(Matrix4f matrix) {
        float[] elements = matrix.getElements();
        for (int i = 0; i < 16; i++) {
            this.elements[i] = new BigDecimal(elements[i]);
        }
    }

    public void setElements(BigDecimal[] elements) { this.elements = elements; }

    public BigDecimal[] getElements() { return elements; }

    public Matrix4B multiply(Matrix4B matrix) {
        Matrix4B result = new Matrix4B();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int n = 0; n < 4; n++) {
                    BigDecimal product =
                            matrix.elements[i + n * 4]
                            .multiply(this.elements[n + j * 4]);

                    result.elements[i + j * 4] = result.elements[i + j * 4].add(product);
                }
            }
        }
        this.elements = result.elements;
        return this;
    }

    public Matrix4B multiply(Matrix4f matrix4f) {
        Matrix4B result = new Matrix4B();
        Matrix4B matrix = new Matrix4B(matrix4f);
        return multiply(matrix);
    }

    public Matrix4B translate(BigDecimal x, BigDecimal y, BigDecimal z) {
        //It is more efficient to manually set the entries in this form
        //then to create a new translation matrix and then multiply the two
        //which is computationally way more demanding
        //ROW 1
        elements[0 + 0 * 4] = elements[0 + 0 * 4].add(
                elements[3 + 0 * 4].multiply(x)
        );
        elements[0 + 1 * 4] = elements[0 + 1 * 4].add(
                elements[3 + 1 * 4].multiply(x)
        );
        elements[0 + 2 * 4] = elements[0 + 2 * 4].add(
                elements[3 + 2 * 4].multiply(x)
        );
        elements[0 + 3 * 4] = elements[0 + 3 * 4].add(
                elements[3 + 3 * 4].multiply(x)
        );
        //ROW 2
        elements[1 + 0 * 4] = elements[1 + 0 * 4].add(
                elements[3 + 0 * 4].multiply(y)
        );
        elements[1 + 1 * 4] = elements[1 + 1 * 4].add(
                elements[3 + 1 * 4].multiply(y)
        );
        elements[1 + 2 * 4] = elements[1 + 2 * 4].add(
                elements[3 + 2 * 4].multiply(y)
        );
        elements[1 + 3 * 4] = elements[1 + 3 * 4].add(
                elements[3 + 3 * 4].multiply(y)
        );
        //ROW 3
        elements[2 + 0 * 4] = elements[2 + 0 * 4].add(
                elements[3 + 0 * 4].multiply(z)
        );
        elements[2 + 1 * 4] = elements[2 + 1 * 4].add(
                elements[3 + 1 * 4].multiply(z)
        );
        elements[2 + 2 * 4] = elements[2 + 2 * 4].add(
                elements[3 + 2 * 4].multiply(z)
        );
        elements[2 + 3 * 4] = elements[2 + 3 * 4].add(
                elements[3 + 3 * 4].multiply(z)
        );
        return this;
    }

    public Matrix4B translate(Vec3b vector) {
        return translate(vector.x, vector.y, vector.z);
    }

    public Matrix4B rotateX(BigDecimal radians) {
        return multiply(getBigRotationX(radians));
    }

    public Matrix4B rotateY(BigDecimal radians) {
        return multiply(getBigRotationY(radians));
    }

    public Matrix4B rotateZ(BigDecimal radians) {
       return multiply(getBigRotationZ(radians));
    }

    public Matrix4B rotate(BigDecimal x, BigDecimal y, BigDecimal z) {
        return multiply(getBigRotation(x, y, z));
    }

    public Matrix4B rotate(Vec3b vec3B) {
        return multiply(getBigRotation(vec3B));
    }

    public Matrix4B scale(BigDecimal scaleX, BigDecimal scaleY, BigDecimal scaleZ) {
        Matrix4B scale_mat = new Matrix4B();
        scale_mat.elements[0 + 0 * 4] = scaleX;
        scale_mat.elements[1 + 1 * 4] = scaleY;
        scale_mat.elements[2 + 2 * 4] = scaleZ;
        scale_mat.elements[3 + 3 * 4] = ONE;

        multiply(scale_mat);
        return this;
    }
}
