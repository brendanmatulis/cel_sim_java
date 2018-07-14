package prototype.objects.CelestialMechanics;

import prototype.math.Vec3f;
import prototype.objects.Mesh;
import prototype.objects.RenderObject;

import java.util.ArrayList;

import static java.lang.Math.*;
import static prototype.utils.GeneralUtils.readAllLines;

public class GenerateStars {

    public static RenderObject genStars_HYG(Vec3f color, String fileName, float radius) {

        //TODO: TEMP: ??? z = (cos ra)(cos dec), x = (sin ra)(cos dec), y = sin dec
        ArrayList<Vec3f> coords = parseHYG(fileName);

        int size = coords.size();

        //Apparent magnitude of Sirius, the brightest star
        float min_brightness = 10;
        float max_brightness = 0;
        float brightness;
        //Brightness on a scale of 0 to 1 where 0 corresponds to an apparent magnitude
        //of, for example, 6 and 0 corresponds to, for example, the apparent magnitude of Sirius (-1.45)
        /* Formula:
         *
         * (6 - m) / (6 - (-1.45))
         *
         * = (6 - m) / 7.45,
         * where m is the apparent mag
         *
         * e.g. when m = m_sirius, formula = 1.
         *      when m = 6, formula = 0.
         *      Other values are excluded use Math.min() and Math.max()
         */

        //Factor used to randomly slightly alter the red and blue color
        //components of a star
        float vary_factor = 0.2f;


        float[] positions = new float[size * 3];
        float[] colors = new float[size * 3];


        for (int i = 0; i < size; i++) {
            Vec3f coord = coords.get(i);
            float mag = coord.z;

            Vec3f pos = celCoordsToXYZ(coord.x, coord.y, radius);

            positions[3 * i] = pos.x;
            positions[3 * i + 1] = pos.y;
            positions[3 * i + 2] = pos.z;

            brightness = (min_brightness - mag) / (min_brightness - max_brightness);
            brightness = min(brightness, 1);
            brightness = max(brightness, 0.01f);

            float r = color.x * brightness;
            float g = color.x * brightness;
            float b = color.x * brightness;

            //Vary colors slightly with slight red shift and blue shift:
            double vary_blue = (random() > 0.5 ? 1 : -1) * random() * brightness * vary_factor;
            double vary_red = (random() > 0.5 ? 1 : -1) * random() * brightness * vary_factor;
            r += vary_red;
            b += vary_blue;

            r = min(r, 1);
            r = max(r, 0);
            b = min(b, 1);
            b = max(b, 0);
            g = min(g, 1);
            g = max(g, 0);

            colors[3 * i]     = r;
            colors[3 * i + 1] = g;
            colors[3 * i + 2] = b;
        }

        int[] indices = new int[positions.length / 3];
        for(int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }

//        Mesh mesh = new Mesh(positions, indices, null, colors);
//        mesh.setDrawType(GL_POINTS);

        Mesh mesh = new Mesh(positions, colors);

        return new RenderObject(mesh);
    }

    public static ArrayList<Vec3f> parseHYG(String fileName) {
        ArrayList<String> lines = readAllLines(fileName);

        //x = right ascension, y = declination, z = magnitude
        ArrayList<Vec3f> coords = new ArrayList<>();


        //Start at lines.get(2), because lines.get(0) defines format and lines.get(1) is the Sun
        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] tokens = line.split(",");

            //token[7] = right ascension (ra)
            //token[8] = declination (dec)
            //token[13] = Apparent visual magnitude (mag)
            Vec3f coord = new Vec3f(
                    Float.parseFloat(tokens[7]),
                    Float.parseFloat(tokens[8]),
                    Float.parseFloat(tokens[13])
            );

            coords.add(coord);
        }

        return coords;
    }

    public static float hoursToRadians(float hours) {
        //[hours] * [2 PI / 24 hr]
        return (float) (hours * (PI / 12f));
    }

    public static Vec3f celCoordsToXYZ(float ra, float dec, float r) {
            ra = hoursToRadians(ra);
            dec = (float) toRadians(dec);

            float x = (float) (r * sin(ra) * cos(dec));
            float y = (float) (r * sin(dec));
            float z = (float) (r * cos(ra) * cos(dec));
            return new Vec3f(x, y, z);
    }
}
