package prototype.engine.graphics.shaders;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static prototype.utils.GeneralUtils.loadAsString;

public class Shader {
    private final String shader_path;
    //eg, shader_path = /shaders/item-vert.glsl
    private final int type;
    //eg, type = GL_VERTEX_SHADER
    private int shader_id;

    public Shader(String shader_path, int type) throws Exception {
        this.shader_path = shader_path;
        this.type = type;
    }

    public void compile(int type) throws Exception {
        //First, check to see if shader already exists
        if (ShaderManager.shaders.containsKey(shader_path)) {
            shader_id = ShaderManager.shaders.get(shader_path);
            return;
        }

        shader_id = glCreateShader(type);
        String src = loadAsString(shader_path);

        if (src == null)
            throw new Exception("Unable to load source for shader \"" + shader_path);

        glShaderSource(shader_id, src);

        glCompileShader(shader_id);

        if (glGetShaderi(shader_id, GL_COMPILE_STATUS) == 0)
            throw new Exception("Error compiling shader code: " + glGetShaderInfoLog(shader_id, 1024));

        System.out.println("Shader \"" + shader_path + "\" created!");
        ShaderManager.shaders.put(shader_path, shader_id);
    }

    public int getID() { return shader_id; }

    public int getType() { return type; }
}
