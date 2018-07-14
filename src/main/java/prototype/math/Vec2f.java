package prototype.math;

public class Vec2f {
    public float x, y;

    public Vec2f() {
        this(0, 0);
    }

    public Vec2f(Vec2f vector2f) {
        this(vector2f.x, vector2f.y);
    }

    public Vec2f(float x, float y) {
        set(x, y);
    }

    public Vec2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vec2f set(Vec2f vector2f) {
        this.x = vector2f.x;
        this.y = vector2f.y;
        return this;
    }

    public Vec2f negate() {
        x = -x;
        y = -y;
        return this;
    }

    public Vec2f zero() {
        x = 0;
        y = 0;
        return this;
    }

    public Vec2f add(Vec2f vector2f) {
        x += vector2f.x;
        y += vector2f.y;
        return this;
    }

    public Vec2f subtract(Vec2f vector2f) {
        x -= vector2f.x;
        y -= vector2f.y;
        return this;
    }

        public Vec2f multiply(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public static void main(String[] args) {
        double count = 12345678901234567d;
        System.out.println(count);
    }
}
