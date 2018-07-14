package prototype.engine.graphics.shaders;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ShaderManager {
    public static Map<String, Integer> shaders = new HashMap<>();
    public static Map<String, Integer> shader_programs = new HashMap<>();

    //To validate programs, a VAO must be bound.
    //To avoid creating one for each program to be linked,
    //a universal temporary one was created here.
    public static int temp_vao = glGenVertexArrays();
}
