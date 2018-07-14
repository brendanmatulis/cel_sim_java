package prototype.objects;

import org.lwjgl.system.MemoryUtil;
import prototype.math.Vec3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {
    private int VAO, POINT_COUNT;
    private final ArrayList<Integer> VBO_IDs;
    private float[] positions, normals, colors;
    private int[] indices;
    private boolean hasNormals, hasIndices;
    private int drawType; //e.g. GL_POINTS, GL_LINES, GL_TRIANGLES
    private float pointsize;

    public Mesh(float[] positions, int[] indices, float[] normals, float[] colors) {
        VBO_IDs = new ArrayList<>();
        POINT_COUNT = indices.length;
        this.positions = positions;

        this.indices = indices;
        this.normals = normals;
        drawType = GL_TRIANGLES; //Default draw type

        this.colors = colors;

        if (normals != null)
            hasNormals = true;

        hasIndices = true;

        pointsize = 1.3f;

        init();
    }

    public Mesh(float[] positions, int[] indices, float[] normals, Vec3f color) {
        VBO_IDs = new ArrayList<>();
        colors = new float[positions.length];
        for (int i = 0; i < colors.length / 3; i++) {
            colors[3 * i] = color.x;
            colors[3 * i + 1] = color.y;
            colors[3 * i + 2] = color.z;
        }

        POINT_COUNT = indices.length;
        this.positions = positions;
        this.indices = indices;
        this.normals = normals;
        drawType = GL_TRIANGLES; //Default draw type


        if (normals != null)
            hasNormals = true;

        hasIndices = true;

        pointsize = 1.3f;

        init();
    }

    public Mesh(float[] positions, float[] colors) {
        VBO_IDs = new ArrayList<>();
        this.positions = positions;
        this.colors = colors;
        POINT_COUNT = positions.length / 3;
        drawType = GL_POINTS;

        normals = null;
        indices = null;

        hasNormals = false;
        hasIndices = false;

        pointsize = 1.3f;

        init();
    }

    public Mesh(float[] positions, Vec3f color) {
        VBO_IDs = new ArrayList<>();
        colors = new float[positions.length];
        for (int i = 0; i < colors.length / 3; i++) {
            colors[3 * i] = color.x;
            colors[3 * i + 1] = color.y;
            colors[3 * i + 2] = color.z;
        }

        this.positions = positions;
        POINT_COUNT = positions.length / 3;
        drawType = GL_POINTS;

        normals = null;
        indices = null;

        hasNormals = false;
        hasIndices = false;

        pointsize = 1.3f;

        init();
    }

    public void init() {
        FloatBuffer posBuffer = null;
        FloatBuffer normalsBuffer = null;
        FloatBuffer colorsBuffer = null;
        IntBuffer indicesBuffer = null;

        try {
            VAO = glGenVertexArrays();
            glBindVertexArray(VAO);

            //Position VBO
            int VBO = glGenBuffers();
            VBO_IDs.add(VBO);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            //Color VBO
            VBO = glGenBuffers();
            VBO_IDs.add(VBO);
            colorsBuffer = MemoryUtil.memAllocFloat(colors.length);
            colorsBuffer.put(colors).flip();
            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            //Vertex normals VBO
            if (hasNormals) {
                VBO = glGenBuffers();
                VBO_IDs.add(VBO);
                normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
                normalsBuffer.put(normals).flip();
                glBindBuffer(GL_ARRAY_BUFFER, VBO);
                glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
                glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
            }

            //Index VBO
            if (hasIndices) {
                VBO = glGenBuffers();
                VBO_IDs.add(VBO);
                indicesBuffer = MemoryUtil.memAllocInt(indices.length);
                indicesBuffer.put(indices).flip();
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, VBO);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

                glBindBuffer(GL_ARRAY_BUFFER, 0);
                glBindVertexArray(0);
            }
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (normalsBuffer != null) {
                MemoryUtil.memFree(normalsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
            if (colorsBuffer != null) {
                MemoryUtil.memFree(colorsBuffer);
            }
        }
    }

    public int getVAO() { return VAO; }

    public void setPointSize(float pointsize) {
        this.pointsize = pointsize;
    }

    public void render() {
        glBindVertexArray(VAO);

        glPointSize(pointsize);

        //Enable vertex attribute arrays
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        if (hasNormals)
            glEnableVertexAttribArray(2);

        if (hasIndices)
            glDrawElements(drawType, POINT_COUNT, GL_UNSIGNED_INT, 0);
        else {
            glDrawArrays(drawType, 0, POINT_COUNT);
        }

        //Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        if (hasNormals)
            glDisableVertexAttribArray(2);

        glBindVertexArray(0);
    }

    public void setDrawType(int drawType) {
        this.drawType = drawType;
    }

    public void cleanup() {
        //Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        for (int VBO_ID: VBO_IDs) {
            glDeleteBuffers(VBO_ID);
        }

        //Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(VAO);
    }
}
