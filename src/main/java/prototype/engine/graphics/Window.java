package prototype.engine.graphics;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private long window;

    private final String title;
    private int width, height;
    private boolean vSync;
    private boolean resized;

    private final float aspectRatio;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        resized = false;
        aspectRatio = width / height;
    }

    public Window(String title, int width, float aspectRatio, boolean vSync) {
        this.title = title;
        this.aspectRatio = aspectRatio;
        this.height = (int) (width / aspectRatio);
        this.width = width;
        this.vSync = vSync;
        resized = false;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Failed to init GLFW...");

        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == 0)
            throw new RuntimeException("Failed to create GLFW window...");

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            glViewport(0, 0, width, height);
            resized = true;

        });

        if (vSync)
            glfwSwapInterval(1);

        glfwSetWindowAspectRatio(window, width, height);
        glEnable(GL_DEPTH_TEST);

        glfwShowWindow(window);
    }

    public void update() {
        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    public float getAspectRatio() { return aspectRatio; }

    public void closeWindow() { glfwSetWindowShouldClose(window, true); }

    public boolean shouldClose() { return glfwWindowShouldClose(window); }

    public boolean vSync() { return vSync; }

    public long getHandle() { return window; }

    public boolean isResized() { return resized; }

    public void setResized(boolean resized) { this.resized = resized; }

    public boolean isKeyPressed(int KEY) { return glfwGetKey(window, KEY) == GLFW_PRESS; }

    public boolean isKeyReleased(int KEY) { return glfwGetKey(window, KEY) == GLFW_RELEASE; }

    public int getHeight() { return height; }

    public int getWidth() { return width; }
}
