package prototype.engine.input;

import org.lwjgl.BufferUtils;
import prototype.engine.graphics.Window;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class FPSMouse extends Mouse {
    private boolean disableMouse;

    public FPSMouse(Window window, float sensitivity) {
        super(window, sensitivity);
        disableMouse = true;
    }

    @Override
    public void init() {
        disableMouse = false;

        lastPosition.set(position);
        offset.zero();

        initRestrictedMouse();

        //Set cursor position callback
        glfwSetCursorPosCallback(window.getHandle(), (win, xpos, ypos) -> {
            if (!disableMouse) {
                position.set((float) xpos, (float) ypos);
            }

        });
    }

    public void initRestrictedMouse() {
        //Resets mouse position so that there is no jump when cursor is first disabled

        //Lock mouse to center and disable
        glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        //Find initial mouse coordinates
        DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window.getHandle(), xpos, ypos);

        //Set initial mouse position to center of the screen
        position.x = (float) xpos.get();
        position.y = (float) ypos.get();
    }

    public void setDisableMouseCallback() {
        glfwSetKeyCallback(window.getHandle(), (long win, int key, int scancode, int action, int mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                disableMouse = !disableMouse;
                if (disableMouse) {
                    glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                } else {
                    initRestrictedMouse();
                    lastPosition.set(position);
                    offset.zero();
                }
            }
        });
    }
}
