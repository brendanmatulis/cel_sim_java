package prototype.utils;

import prototype.math.Vec3d;
import prototype.math.Vec3f;
import prototype.objects.CelestialMechanics.CelObj;
import prototype.objects.Mesh;
import prototype.objects.RenderObject;

import java.util.ArrayList;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static prototype.utils.GeneralUtils.readAllLines;
import static prototype.utils.OBJLoader.loadMesh;

public class ParseSetups {
    public static CelObj loadPlanet_d(String path, String nameInFile, String givenName) {
        ArrayList<String> lines = readAllLines(path);

        Vec3d pos = new Vec3d();
        Vec3d rot = new Vec3d();
        Vec3f color = new Vec3f();
        Vec3d vel = new Vec3d();
        Mesh mesh = null;
        double radius = 0;
        double mass = 0;

        for (String line : lines) {
            String[] tokens = line.split(",");
            if (tokens[0].equals(nameInFile)) {

                //NOTE for pos, rot, vel:
                // ICRF x -> OpenGL z
                // ICRF y -> OpenGL x
                // ICRF z -> OpenGL y
                //E.g. why order is 2,3,1 -- not 1,2,3
                pos = new Vec3d(
                        parseDouble(tokens[2]),
                        parseDouble(tokens[3]),
                        parseDouble(tokens[1])
                );

                rot = new Vec3d(
                        parseDouble(tokens[5]),
                        parseDouble(tokens[6]),
                        parseDouble(tokens[4])
                );


                color = new Vec3f(
                        parseFloat(tokens[7]),
                        parseFloat(tokens[8]),
                        parseFloat(tokens[9])
                );

                String meshPath = tokens[10];

                mesh = loadMesh(meshPath, color);
                mesh.setDrawType(GL_POINTS);

                radius = parseDouble(tokens[11]);
                mass = parseDouble(tokens[12]);

                vel = new Vec3d(
                        parseDouble(tokens[14]),
                        parseDouble(tokens[15]),
                        parseDouble(tokens[13])
                );
            }
        }
        CelObj celObj = new CelObj(color, mass, radius, givenName);
        celObj.add("main", new RenderObject(mesh));
        celObj.setCelPos(pos);
        celObj.setCelRot(rot);
        celObj.setCelVel(vel);
        return celObj;
    }
}
