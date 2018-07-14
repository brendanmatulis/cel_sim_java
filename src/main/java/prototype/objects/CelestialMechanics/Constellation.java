package prototype.objects.CelestialMechanics;

import prototype.math.Vec3f;
import prototype.objects.Mesh;
import prototype.objects.RenderObject;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static prototype.objects.CelestialMechanics.GenerateStars.celCoordsToXYZ;
import static prototype.utils.GeneralUtils.readAllLines;

public class Constellation {
    public static ArrayList<RenderObject> constellations;

    public static void init() {
        float sky_radius = 14800;
        Vec3f constellation_color = new Vec3f(0.2f, 0.1f, 0.4f);

        constellations = new ArrayList<>();

        //Orion
        RenderObject orion = getConstellation("/data/constellations/Orion.csv", sky_radius, constellation_color);
        constellations.add(orion);

        //Aries
        RenderObject aries = getConstellation("/data/constellations/Aries.csv", sky_radius, constellation_color);
        constellations.add(aries);

        //Taurus
        RenderObject taurus = getConstellation("/data/constellations/Taurus.csv", sky_radius, constellation_color);
        constellations.add(taurus);

        //Gemini
        RenderObject gemini = getConstellation("/data/constellations/Gemini.csv", sky_radius, constellation_color);
        constellations.add(gemini);

        //Cancer
        RenderObject cancer = getConstellation("/data/constellations/Cancer.csv", sky_radius, constellation_color);
        constellations.add(cancer);

        //Leo
        RenderObject leo = getConstellation("/data/constellations/Leo.csv", sky_radius, constellation_color);
        constellations.add(leo);

        //Virgo
        RenderObject virgo = getConstellation("/data/constellations/Virgo.csv", sky_radius, constellation_color);
        constellations.add(virgo);

        //Libra
        RenderObject libra = getConstellation("/data/constellations/Libra.csv", sky_radius, constellation_color);
        constellations.add(libra);

        //Scorpio
        RenderObject scorpio = getConstellation("/data/constellations/Scorpio.csv", sky_radius, constellation_color);
        constellations.add(scorpio);

        //Sagittarius
        RenderObject sagittarius = getConstellation("/data/constellations/Sagittarius.csv", sky_radius, constellation_color);
        constellations.add(sagittarius);

        //Capricorn
        RenderObject capricorn = getConstellation("/data/constellations/Capricorn.csv", sky_radius, constellation_color);
        constellations.add(capricorn);

        //Aquarius
        RenderObject aquarius = getConstellation("/data/constellations/Aquarius.csv", sky_radius, constellation_color);
        constellations.add(aquarius);


        //Pisces
        RenderObject pisces = getConstellation("/data/constellations/Pisces.csv", sky_radius, constellation_color);
        constellations.add(pisces);
    }

    public static void setConstellationVisibility(boolean visibility) {
        for (RenderObject obj : constellations) {
            obj.setVisibility(visibility);
        }
    }

    public static RenderObject getConstellation(String path, float radius, Vec3f color) {
        ArrayList<String> lines = readAllLines(path);
        ArrayList<Vec3f> positions = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split(",");
            if (tokens[0].equals("star")) {
                float ra = Float.parseFloat(tokens[3]);
                float dec = Float.parseFloat(tokens[4]);

                positions.add(celCoordsToXYZ(ra, dec, radius));
            } else if (tokens[0].equals("indices")) {
                for (int i = 1; i < tokens.length; i++) {
                    try {
                        indices.add(Integer.parseInt(tokens[i]));
                    } catch (Exception e) {}
                }
            }
        }

        float[] posArr = new float[indices.size() * 3];

        int k = 0;
        for (int i : indices) {
            Vec3f pos = positions.get(i);
            posArr[k * 3] = pos.x;
            posArr[k * 3 + 1] = pos.y;
            posArr[k * 3 + 2] = pos.z;
            k++;
        }

        Mesh mesh = new Mesh(posArr, color);
        mesh.setDrawType(GL_LINES);
        return new RenderObject(mesh);
    }
}
