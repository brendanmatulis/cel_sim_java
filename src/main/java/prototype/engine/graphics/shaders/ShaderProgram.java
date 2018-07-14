package prototype.engine.graphics.shaders;

import prototype.math.Matrix4f;
import prototype.math.Vec4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class ShaderProgram {
    private String program_name;
    private int program_id;
    private boolean bound = false;
    private ArrayList<Shader> shaders = new ArrayList<>();
    public Map<String, Integer> uniform_locations = new HashMap<>();

    public ShaderProgram(String program_name) {
        this.program_name = program_name;
    }

    public ShaderProgram(String program_name, ArrayList<Shader> shaders) {
        this(program_name);
        this.shaders = shaders;
    }

    public void link() throws Exception {
        if (ShaderManager.shader_programs.containsKey(program_name)) {
            program_id = ShaderManager.shader_programs.get(program_name);
        } else {
            program_id = glCreateProgram();

            if (program_id == 0)
                throw new Exception("Could not create Shader");


            //Compile shaders
            for (Shader shader : shaders) {
                shader.compile(shader.getType());
            }

            for (Shader shader : shaders) {
                int shader_id = shader.getID();
                if (shader_id != 0)
                    glAttachShader(program_id, shader_id);
            }

            glLinkProgram(program_id);
            if (glGetProgrami(program_id, GL_LINK_STATUS) == 0) {
                throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(program_id, 1024));
            }

            for (Shader shader : shaders) {
                int shader_id = shader.getID();
                if (shader_id != 0)
                    glDetachShader(program_id, shader_id);
            }

            //A temporary VAO must be created to validate program
            glBindVertexArray(ShaderManager.temp_vao);

            glValidateProgram(program_id);

            //Unbinding temporary VAO
            glBindVertexArray(0);

            if (glGetProgrami(program_id, GL_VALIDATE_STATUS) == 0) {
                System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(program_id, 1024));
            } else {
                System.out.println("Shader program \"" + program_name + "\" created!");
                ShaderManager.shader_programs.put(program_name, program_id);
            }
        }
    }

    public int getUniform(String uniform_name) throws Exception {
        //It is inefficient to call glGetUniformLocation every update because
        //there is a bottle neck in communication between CPU and GPU
        //So, we use a Map

        if (uniform_locations.containsKey(uniform_name))
            return uniform_locations.get(uniform_name);

        //Gets the uniform location of a certain shader so that we can set data
        int location = glGetUniformLocation(program_id, uniform_name);
        //If this return -1, it couldn't find it
        if (location == -1)
            throw new Exception("Could not find uniform variable \"" + uniform_name + "\"");

        uniform_locations.put(uniform_name, location);
        return location;
    }

    //Uniform for a single int:
    public void setUniform(String name, int value) throws Exception {
        if (!bound) bind();
        //Uniform variables are ways to provide data to shaders from CPU
        glUniform1i(getUniform(name), value);
    }

    //Uniform for a single float:
    public void setUniform(String name, float value) throws Exception {
        if (!bound) bind();
        //Uniform variables are ways to provide data to shaders from CPU
        glUniform1f(getUniform(name), value);
    }

    //Uniform for two floats:
    public void setUniform(String name, float x, float y) throws Exception {
        if (!bound) bind();
        //Uniform variables are ways to provide data to shaders from CPU
        glUniform2f(getUniform(name), x, y);
    }

    //Uniform for three floats:
    public void setUniform(String name, float x, float y, float z) throws Exception {
        if (!bound) bind();
        //Uniform variables are ways to provide data to shaders from CPU
        glUniform3f(getUniform(name), x, y, z);
    }

    //Uniform for four floats:
    public void setUniform(String name, float x, float y, float z, float w) throws Exception {
        if (!bound) bind();
        //Uniform variables are ways to provide data to shaders from CPU
        glUniform4f(getUniform(name), x, y, z, w);
    }

    //Uniform for four floats:
    public void setUniform(String name, Matrix4f matrix) throws Exception {
        if (!bound) bind();
        //Uniform variables are ways to provide data to shaders from CPU

        //False is for transpose
        //OpenGL wants column major
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }

    //Uniform for Vec4f
    public void setUniform(String name, Vec4f vector4f) throws Exception {
//        if (!bound) bind();
//        //Uniform variables are ways to provide data to shaders from CPU
//        float[] color_floats = new float[] {
//                vector4f.x, vector4f.y, vector4f.z, vector4f.w
//        };
//        glUniform4fv(getUniform(name), color_floats);
        setUniform(name, vector4f.x, vector4f.y, vector4f.z, vector4f.w);
    }

    public void bind() {
        glUseProgram(program_id);
        bound = true;
    }

    public void unbind() {
        glUseProgram(0);
        bound = false;
    }

    public void attach(Shader shader) {
        shaders.add(shader);
    }

    public void cleanup() {
        unbind();
        if (program_id != 0) {
            glDeleteProgram(program_id);
        }
    }
}
