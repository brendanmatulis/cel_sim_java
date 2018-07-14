package prototype.engine.input;

import prototype.engine.graphics.Window;
import prototype.math.Vec2f;

import static org.lwjgl.glfw.GLFW.*;
import static prototype.math.Operations.multiply;
import static prototype.math.Operations.subtract;

public class Mouse {
    public Vec2f position;
    public Vec2f lastPosition;
    public Vec2f offset;
    private float sensitivity;
    public final Window window;

    public Mouse(Window window, float sensitivity) {
        this.window = window;
        this.sensitivity = sensitivity;
        position = new Vec2f();
        lastPosition = new Vec2f();
        offset = new Vec2f();
    }

    public void init() {
        //Set cursor position callback
        glfwSetCursorPosCallback(window.getHandle(), (win, xpos, ypos) -> {
            position.set((float) xpos, (float) ypos);

        });
    }

    public void update() {
        offset = multiply(
                    sensitivity,
                    subtract(position, lastPosition)
            );

        lastPosition.set(position);
    }

    public Vec2f getOffset() {
        return offset;
    }

    public Vec2f getLastPosition() {
        return lastPosition;
    }

    public Vec2f getPosition() {
        return position;
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }

    public float getOffsetX() {
        return offset.x;
    }

    public float getOffsetY() {
        return offset.y;
    }
}
