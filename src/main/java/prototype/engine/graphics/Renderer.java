package prototype.engine.graphics;

import prototype.engine.graphics.shaders.ShaderProgram;
import prototype.math.Matrix4f;
import prototype.objects.RenderObject;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static prototype.math.GetMatrix.*;

public class Renderer {

    public void render(ArrayList<RenderObject> renderObjects, Matrix4f vw_mat, Matrix4f proj_mat, ShaderProgram shader) throws Exception {
        if (renderObjects.size() > 0) {
            shader.bind();
            shader.setUniform("proj_mat", proj_mat);

            for (RenderObject obj : renderObjects) {
                if (obj.isVisible()) {
                    //Set model view matrix
                    shader.setUniform(
                            "ml_vw_mat",
                            getModelView(obj, vw_mat)
                    );
                    obj.getMesh().render();
                }
            }
            shader.unbind();
        }
        checkErrors();

    }

    public void render(RenderObject renderObj, Matrix4f vw_mat, Matrix4f proj_mat, ShaderProgram shader) throws Exception {
        shader.bind();
        shader.setUniform("proj_mat", proj_mat);

        if (renderObj.isVisible()) {
            //Set model view matrix
            shader.setUniform(
                    "ml_vw_mat",
                    getModelView(renderObj, vw_mat)
            );
            renderObj.getMesh().render();
        }

        shader.unbind();

        checkErrors();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private static void checkErrors() {
        int GL_ERROR;
        while ((GL_ERROR = glGetError()) != 0) {
            System.err.println("[GL ERROR] Error code: " + GL_ERROR);
        }
    }
}
