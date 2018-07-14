package prototype.utils;

import prototype.math.Vec3f;
import prototype.math.Vec2f;
import prototype.objects.Mesh;

import java.util.ArrayList;
import java.util.List;

import static prototype.utils.GeneralUtils.readAllLines;

public class OBJLoader {

    //TODO: use assimp or recode this yourself
    public static Mesh loadMesh(String fileName, Vec3f color) {
        ArrayList<String> lines = readAllLines(fileName);

        List<Vec3f> vertices = new ArrayList<>();
        List<Vec2f> textures = new ArrayList<>();
        List<Vec3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v":
                    //Geometric vertex
                    Vec3f vec3f = new Vec3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );

                    vertices.add(vec3f);
                    break;
                case "vt":
                    //Texture coordinates
                    Vec2f vec2f = new Vec2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    textures.add(vec2f);
                    break;
                case "vn":
                    //Vertex normal
                    Vec3f vec3fNorm = new Vec3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normals.add(vec3fNorm);
                    break;
                case "f":
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);
                    faces.add(face);
                    break;
                default:
                    //Ignore other lines
                    break;
            }
        }
        return reorderLists(vertices, textures, normals, faces, color);
    }

    private static Mesh reorderLists(List<Vec3f> posList, List<Vec2f> texCoordList,
                                     List<Vec3f> normList, List<Face> faceList, Vec3f color) {
        List<Integer> indices = new ArrayList<>();
        //Create position array in the order it has been declared
        float[] posArr = new float[posList.size() * 3];
        int i = 0;
        for (Vec3f pos : posList) {
            posArr[i * 3] = pos.x;
            posArr[i * 3 + 1] = pos.y;
            posArr[i * 3 + 2] = pos.z;
            i++;
        }
        float[] textCoordArr = new float[posList.size() * 2];
        float[] normArr = new float[posList.size() * 3];

        for (Face face : faceList) {
            IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
            for (IdxGroup indValue : faceVertexIndices) {
                processFaceVertex(indValue, texCoordList, normList,
                        indices, textCoordArr, normArr);
            }
        }
        int[] indicesArr;

        //TODO: research this...
        indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
//        Mesh mesh = new Mesh(posArr, indicesArr, normArr, color);
        Mesh mesh = new Mesh(posArr, color); //TODO
        return mesh;
    }

    public static void processFaceVertex(IdxGroup indices, List<Vec2f> texCoordList,
                                         List<Vec3f> normList, List<Integer> indicesList,
                                         float[] textCoordArr, float[] normArr) {

        //Set index for vertex coordinates
        int posIndex = indices.idxPos;
        indicesList.add(posIndex);

        //Reorder texture coordinates
        if (indices.idxTextCoord >= 0) {
            Vec2f textCoord = texCoordList.get(indices.idxTextCoord);
            textCoordArr[posIndex * 2] = textCoord.x;
            textCoordArr[posIndex * 2 + 1] = 1 - textCoord.y; //Why 1 - y? (UV coords?)
        }
        if (indices.idxVecNormal >= 0) {
            //Reorder normals
            Vec3f vecNorm = normList.get(indices.idxVecNormal);
            normArr[posIndex * 3] = vecNorm.x;
            normArr[posIndex * 3 + 1] = vecNorm.y;
            normArr[posIndex * 3 + 2] = vecNorm.z;
        }
    }

    protected static class Face {
        /**
         * List of idxGroup groups for a face triangle (3 vertices per face).
         */

        private IdxGroup[] idxGroups = new IdxGroup[3];

        public Face(String v1, String v2, String v3) {
            idxGroups = new IdxGroup[3];
            //Parse the lines
            idxGroups[0] = parseLine(v1);
            idxGroups[1] = parseLine(v2);
            idxGroups[2] = parseLine(v3);
        }

        private IdxGroup parseLine(String line) {
            IdxGroup idxGroup = new IdxGroup();

            String[] lineTokens = line.split("/");
            int length = lineTokens.length;
            idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
            if (length > 1) {
                //It can be empty if the obj does not define text coords
                String textCoord = lineTokens[1];
                idxGroup.idxTextCoord = textCoord.length() > 0 ? Integer.parseInt(textCoord) -1 : IdxGroup.NO_VALUE;
                if (length > 2) {
                    idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }
            return idxGroup;
        }

        public IdxGroup[] getFaceVertexIndices() {
            return idxGroups;
        }
    }

    protected static class IdxGroup {
        public static final int NO_VALUE = -1;
        public int idxPos;
        public int idxTextCoord;
        public int idxVecNormal;

        public IdxGroup() {
            idxPos = NO_VALUE;
            idxTextCoord = NO_VALUE;
            idxVecNormal = NO_VALUE;
        }
    }

    public static void main(String[] args) {
        ArrayList lines = readAllLines("/models/bunny.obj");
        ArrayList lines2 = readAllLines("/models/bunny.obj");
    }
}
