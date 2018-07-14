package prototype.objects;

import prototype.math.Vec3f;
import prototype.objects.Mesh;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static prototype.utils.OBJLoader.loadMesh;

public class GenMesh {
    public static Mesh getPoleMesh(Vec3f color) {
        float[] positions = new float[] {
                0, -1f, 0,
                0,  1f, 0
        };

        Mesh mesh = new Mesh(positions, color);
        mesh.setDrawType(GL_LINES);
        return mesh;
    }

    public static Mesh getSphereMesh(Vec3f color) {
        return loadMesh("/models/sphere1.obj", color);
    }
}
