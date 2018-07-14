package prototype.math;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import prototype.engine.graphics.Camera;
import prototype.objects.CelestialMechanics.CelObj;
import prototype.objects.RenderObject;

import java.math.BigDecimal;

import static java.lang.Math.*;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.BigDecimal.ZERO;
import static prototype.engine.physics.EulerMethod.worldunit_per_meters;
import static prototype.math.Operations.*;

public class GetMatrix {

    //IDENTITY MATRICES
    public static Matrix4f getIdentity4f() {
        float[] elements = new float[16];
        elements[0 + 0 * 4] = 1;
        elements[1 + 1 * 4] = 1;
        elements[2 + 2 * 4] = 1;
        elements[3 + 3 * 4] = 1;
        return new Matrix4f(elements);
    }

    public static Matrix4d getIdentity4d() {
        double[] elements = new double[16];
        elements[0 + 0 * 4] = 1;
        elements[1 + 1 * 4] = 1;
        elements[2 + 2 * 4] = 1;
        elements[3 + 3 * 4] = 1;
        return new Matrix4d(elements);
    }

    public static Matrix4B getBigIdentity() {
        BigDecimal[] elements = new BigDecimal[16];

        //All elements must be defined.
        //Defining them all is more efficient than
        //creating a new matrix with all entries zero
        //and then changing the zeroes to ones
        elements[0 + 0 * 4] = ONE.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[0 + 1 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[0 + 2 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[0 + 3 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);

        elements[1 + 0 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[1 + 1 * 4] = ONE.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[1 + 2 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[1 + 3 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);

        elements[2 + 0 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[2 + 1 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[2 + 2 * 4] = ONE.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[2 + 3 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);

        elements[3 + 0 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[3 + 1 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[3 + 2 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[3 + 3 * 4] = ONE.setScale(32, BigDecimal.ROUND_FLOOR);

        return new Matrix4B(elements);
    }

    //TRANSLATION MATRICES
    public static Matrix4f getTranslation4f(float x, float y, float z) {
        return getIdentity4f().translate(x, y, z);
    }

    public static Matrix4f getTranslation4f(Vec3f vec) {
        return getIdentity4f().translate(vec);
    }

    public static Matrix4d getTranslation4d(double x, double y, double z) {
        return getIdentity4d().translate(x, y, z);
    }

    public static Matrix4d getTranslation4d(Vec3d vec) {
        return getIdentity4d().translate(vec);
    }

    public static Matrix4B getBigTranslation(BigDecimal x, BigDecimal y, BigDecimal z) {
        BigDecimal[] elements = new BigDecimal[16];
        //All elements must be defined.
        //Defining them all here is more efficient
        elements[0 + 0 * 4] = ONE.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[0 + 1 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[0 + 2 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[0 + 3 * 4] = x.setScale(32, BigDecimal.ROUND_FLOOR);

        elements[1 + 0 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[1 + 1 * 4] = ONE.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[1 + 2 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[1 + 3 * 4] = y.setScale(32, BigDecimal.ROUND_FLOOR);

        elements[2 + 0 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[2 + 1 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[2 + 2 * 4] = ONE.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[2 + 3 * 4] = z.setScale(32, BigDecimal.ROUND_FLOOR);

        elements[3 + 0 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[3 + 1 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[3 + 2 * 4] = ZERO.setScale(32, BigDecimal.ROUND_FLOOR);
        elements[3 + 3 * 4] = ONE.setScale(32, BigDecimal.ROUND_FLOOR);

        return new Matrix4B(elements);
    }

    //ROTATION MATRICES
    //ROTATE X
    public static Matrix4f getRotationX4f(float degrees) {
        float[] elements = new float[16];

        float sin = (float) sin(toRadians(degrees));
        float cos = (float) cos(toRadians(degrees));

        elements[0 + 0 * 4] = 1;

        elements[1 + 1 * 4] = cos;
        elements[1 + 2 * 4] = -sin;

        elements[2 + 1 * 4] = sin;
        elements[2 + 2 * 4] = cos;

        elements[3 + 3 * 4] = 1;

        return new Matrix4f(elements);
    }

    public static Matrix4d getRotationX4d(double degrees) {
        double[] elements = new double[16];

        double sin = sin(toRadians(degrees));
        double cos = cos(toRadians(degrees));

        elements[0 + 0 * 4] = 1;

        elements[1 + 1 * 4] = cos;
        elements[1 + 2 * 4] = -sin;

        elements[2 + 1 * 4] = sin;
        elements[2 + 2 * 4] = cos;

        elements[3 + 3 * 4] = 1;

        return new Matrix4d(elements);
    }

    public static Matrix4B getBigRotationX(BigDecimal radians) {
        BigDecimal[] elements = new BigDecimal[16];

            Apfloat rad_apfloat = new Apfloat(radians, 32);
            Apfloat sin_apfloat = ApfloatMath.sin(rad_apfloat);
            Apfloat cos_apfloat = ApfloatMath.cos(rad_apfloat);

        BigDecimal sin = new BigDecimal(  sin_apfloat.toString() )
                .setScale(32, ROUND_FLOOR);
        BigDecimal cos = new BigDecimal(  cos_apfloat.toString() )
                .setScale(32, ROUND_FLOOR);

        elements[0 + 0 * 4] = ONE;

        elements[1 + 1 * 4] = cos;
        elements[1 + 2 * 4] = sin.negate();

        elements[2 + 1 * 4] = sin;
        elements[2 + 2 * 4] = cos;

        elements[3 + 3 * 4] = ONE;
        return new Matrix4B(elements);
    }

    //ROTATE Y
    public static Matrix4f getRotationY4f(float degrees) {
        float[] elements = new float[16];

        float sin = (float) sin(toRadians(degrees));
        float cos = (float) cos(toRadians(degrees));

        elements[0 + 0 * 4] = cos;
        elements[0 + 2 * 4] = sin;

        elements[1 + 1 * 4] = 1;

        elements[2 + 0 * 4] = -sin;
        elements[2 + 2 * 4] = cos;

        elements[3 + 3 * 4] = 1;

        return new Matrix4f(elements);
    }

    public static Matrix4d getRotationY4d(double degrees) {
        double[] elements = new double[16];

        double sin = sin(toRadians(degrees));
        double cos = cos(toRadians(degrees));

        elements[0 + 0 * 4] = cos;
        elements[0 + 2 * 4] = sin;

        elements[1 + 1 * 4] = 1;

        elements[2 + 0 * 4] = -sin;
        elements[2 + 2 * 4] = cos;

        elements[3 + 3 * 4] = 1;

        return new Matrix4d(elements);
    }

    public static Matrix4B getBigRotationY(BigDecimal radians) {
        BigDecimal[] elements = new BigDecimal[16];

            Apfloat rad_apfloat = new Apfloat(radians, 32);
            Apfloat sin_apfloat = ApfloatMath.sin(rad_apfloat);
            Apfloat cos_apfloat = ApfloatMath.cos(rad_apfloat);

        BigDecimal sin = new BigDecimal(  sin_apfloat.toString() )
                .setScale(32, ROUND_FLOOR);
        BigDecimal cos = new BigDecimal(  cos_apfloat.toString() )
                .setScale(32, ROUND_FLOOR);

        elements[0 + 0 * 4] = cos;
        elements[0 + 2 * 4] = sin;

        elements[1 + 1 * 4] = ONE;

        elements[2 + 0 * 4] = sin.negate();
        elements[2 + 2 * 4] = cos;

        elements[3 + 3 * 4] = ONE;

        return new Matrix4B(elements);
    }

    //ROTATE Z
    public static Matrix4f getRotationZ4f(float degrees) {
        float[] elements = new float[16];

        float sin = (float) sin(toRadians(degrees));
        float cos = (float) cos(toRadians(degrees));

        elements[0 + 0 * 4] = cos;
        elements[0 + 1 * 4] = -sin;

        elements[1 + 0 * 4] = sin;
        elements[1 + 1 * 4] = cos;

        elements[2 + 2 * 4] = 1;

        elements[3 + 3 * 4] = 1;

        return new Matrix4f(elements);
    }

    public static Matrix4d getRotationZ4d(double degrees) {
        double[] elements = new double[16];

        double sin = sin(toRadians(degrees));
        double cos = cos(toRadians(degrees));

        elements[0 + 0 * 4] = cos;
        elements[0 + 1 * 4] = -sin;

        elements[1 + 0 * 4] = sin;
        elements[1 + 1 * 4] = cos;

        elements[2 + 2 * 4] = 1;

        elements[3 + 3 * 4] = 1;

        return new Matrix4d(elements);
    }

    public static Matrix4B getBigRotationZ(BigDecimal radians) {
        BigDecimal[] elements = new BigDecimal[16];

            Apfloat rad_apfloat = new Apfloat(radians, 32);
            Apfloat sin_apfloat = ApfloatMath.sin(rad_apfloat);
            Apfloat cos_apfloat = ApfloatMath.cos(rad_apfloat);

        BigDecimal sin = new BigDecimal(  sin_apfloat.toString() )
                .setScale(32, ROUND_FLOOR);
        BigDecimal cos = new BigDecimal(  cos_apfloat.toString() )
                .setScale(32, ROUND_FLOOR);

        elements[0 + 0 * 4] = cos;
        elements[0 + 1 * 4] = sin.negate();

        elements[1 + 0 * 4] = sin;
        elements[1 + 1 * 4] = cos;

        elements[2 + 2 * 4] = ONE;

        elements[3 + 3 * 4] = ONE;

        return new Matrix4B(elements);
    }

    //ROTATE X, Y, Z
    public static Matrix4f getRotation4f(float deg_x, float deg_y, float deg_z) {
        //Matrix created by manually multiplying for efficiency
        //[Rot X] [Rot Y] [Rot Z] = [Rotation]
        float[] elements = new float[16];

        float sin_x = (float) sin(toRadians(deg_x));
        float cos_x = (float) cos(toRadians(deg_x));

        float sin_y = (float) sin(toRadians(deg_y));
        float cos_y = (float) cos(toRadians(deg_y));

        float sin_z = (float) sin(toRadians(deg_z));
        float cos_z = (float) cos(toRadians(deg_z));

        elements[0 + 0 * 4] = cos_z * cos_y;
        elements[0 + 1 * 4] = (cos_z * sin_y * sin_x) - (sin_z * cos_x);
        elements[0 + 2 * 4] = (cos_z * sin_y * cos_x) + (sin_z * sin_x);

        elements[1 + 0 * 4] = sin_z * cos_y;
        elements[1 + 1 * 4] = (sin_z * sin_y * sin_x) + (cos_z * cos_x);
        elements[1 + 2 * 4] = (sin_z * sin_y * cos_x) - (cos_z * sin_x);

        elements[2 + 0 * 4] = -sin_y;
        elements[2 + 1 * 4] = cos_y * sin_x;
        elements[2 + 2 * 4] = cos_y * cos_x;

        elements[3 + 3 * 4] = 1;

        return new Matrix4f(elements);
    }

    public static Matrix4f getRotation4f(Vec3f vec) {
        return getRotation4f(vec.x, vec.y, vec.z);
    }

    public static Matrix4d getRotation4d(double deg_x, double deg_y, double deg_z) {
        //Matrix created by manually multiplying for efficiency
        //[Rot X] [Rot Y] [Rot Z] = [Rotation]
        double[] elements = new double[16];

        double sin_x = sin(toRadians(deg_x));
        double cos_x = cos(toRadians(deg_x));

        double sin_y = sin(toRadians(deg_y));
        double cos_y = cos(toRadians(deg_y));

        double sin_z = sin(toRadians(deg_z));
        double cos_z = cos(toRadians(deg_z));

        elements[0 + 0 * 4] = cos_z * cos_y;
        elements[0 + 1 * 4] = (cos_z * sin_y * sin_x) - (sin_z * cos_x);
        elements[0 + 2 * 4] = (cos_z * sin_y * cos_x) + (sin_z * sin_x);

        elements[1 + 0 * 4] = sin_z * cos_y;
        elements[1 + 1 * 4] = (sin_z * sin_y * sin_x) + (cos_z * cos_x);
        elements[1 + 2 * 4] = (sin_z * sin_y * cos_x) - (cos_z * sin_x);

        elements[2 + 0 * 4] = -sin_y;
        elements[2 + 1 * 4] = cos_y * sin_x;
        elements[2 + 2 * 4] = cos_y * cos_x;

        elements[3 + 3 * 4] = 1;

        return new Matrix4d(elements);
    }

    public static Matrix4d getRotation4f(Vec3d vec) {
        return getRotation4d(vec.x, vec.y, vec.z);
    }

    public static Matrix4B getBigRotation(BigDecimal rad_x, BigDecimal rad_y, BigDecimal rad_z) {
        //Matrix created by manually multiplying for efficiency
        //[Rot X] [Rot Y] [Rot Z] = [Rotation]
        BigDecimal[] elements = new BigDecimal[16];

        //Rotation x (radians) sin and cos
            Apfloat rad_x_apfloat = new Apfloat(rad_x, 32);
            Apfloat sin_x_apfloat = ApfloatMath.sin(rad_x_apfloat);
            Apfloat cos_x_apfloat = ApfloatMath.cos(rad_x_apfloat);

        BigDecimal sin_x = new BigDecimal(  sin_x_apfloat.toString() )
                .setScale(32, ROUND_FLOOR);
        BigDecimal cos_x = new BigDecimal(  cos_x_apfloat.toString() )
                .setScale(32, ROUND_FLOOR);

        //Rotation y (radians) sin and cos
            Apfloat rad_y_apfloat = new Apfloat(rad_y, 32);
            Apfloat sin_y_apfloat = ApfloatMath.sin(rad_y_apfloat);
            Apfloat cos_y_apfloat = ApfloatMath.cos(rad_y_apfloat);

        BigDecimal sin_y = new BigDecimal(  sin_y_apfloat.toString() )
                .setScale(32, ROUND_FLOOR);
        BigDecimal cos_y = new BigDecimal(  cos_y_apfloat.toString() )
                .setScale(32, ROUND_FLOOR);

        //Rotation z (radians) sin and cos
            Apfloat rad_z_apfloat = new Apfloat(rad_z, 32);
            Apfloat sin_z_apfloat = ApfloatMath.sin(rad_z_apfloat);
            Apfloat cos_z_apfloat = ApfloatMath.cos(rad_z_apfloat);

        BigDecimal sin_z = new BigDecimal(  sin_z_apfloat.toString() )
                .setScale(32, ROUND_FLOOR);
        BigDecimal cos_z = new BigDecimal(  cos_z_apfloat.toString() )
                .setScale(32, ROUND_FLOOR);

        //Row 1
        elements[0 + 0 * 4] = cos_z.multiply(cos_y);
        elements[0 + 1 * 4] = (cos_z.multiply(sin_y).multiply(sin_x))
                                .subtract(sin_z.multiply(cos_x));

        elements[0 + 2 * 4] = (cos_z.multiply(sin_y).multiply(cos_x))
                                .add(sin_z.multiply(sin_x));

        //Row 2
        elements[1 + 0 * 4] = sin_z.multiply(cos_y);
        elements[1 + 1 * 4] = (sin_z.multiply(sin_y).multiply(sin_x))
                                .add(cos_z.multiply(cos_x));
        elements[1 + 2 * 4] = (sin_z.multiply(sin_y).multiply(cos_x))
                                .subtract(cos_z.multiply(sin_x));
        //Row 3
        elements[2 + 0 * 4] = sin_y.negate();
        elements[2 + 1 * 4] = cos_y.multiply(sin_x);
        elements[2 + 2 * 4] = cos_y.multiply(cos_x);

        //Row 4
        elements[3 + 3 * 4] = ONE;

        return new Matrix4B(elements);
    }

    public static Matrix4B getBigRotation(double x, double y, double z) {
        return getBigRotation(
                new BigDecimal(x), new BigDecimal(y), new BigDecimal(z)
        );
    }

    public static Matrix4B getBigRotation(Vec3b vec3B) {
        return getBigRotation(vec3B.x, vec3B.x, vec3B.x);
    }

    //SCALE MATRICES
    public static Matrix4f getScale4f(float scale) {
        return getIdentity4f().scale(scale);
    }

    public static Matrix4f getScale4f(float scaleX, float scaleY, float scaleZ) {
        return getIdentity4f().scale(scaleX, scaleY, scaleZ);
    }

    public static Matrix4f getScale4f(Vec3f vec3F) {
        return getIdentity4f().scale(vec3F);
    }

    //GRAPHICS MATRICES:

    //ORTHOGRAPHIC PROJECTION MATRIX
    public static Matrix4f getOrthographic(float R, float L, float T, float B, float N, float F) {
        //Right R, Left L, Top T, Bottom B, Near N, Far F
        float[] elements = new float[16];
        elements[0 + 0 * 4] = 2 / (R - L);
        elements[0 + 3 * 4] = -(R + L) / (R - L);

        elements[1 + 1 * 4] = 2 / (T - B);
        elements[1 + 3 * 4] = -(T + B) / (T - B);

        elements[2 + 2 * 4] = -2 / (F - N);
        elements[2 + 3 * 4] = -(F + N) / (F - N);

        elements[3 + 3 * 4] = 1;
        return new Matrix4f(elements);
    }

    public static Matrix4f getOrthographic(float N, float F, float aspectRatio, float width) {
        float R = width / 2;
        float L = -R;
        float height = width / aspectRatio;
        float T = height / 2;
        float B = -T;
        return getOrthographic(R, L, T, B, N, F);
    }

    //PERSPECTIVE PROJECTION MATRIX
    public static Matrix4f getPerspective(float R, float L, float T, float B, float N, float F) {
        //Right R, Left L, Top T, Bottom B, Near N, Far F
        float[] elements = new float[16];
        elements[0 + 0 * 4] = (2 * N) / (R - L);
        elements[0 + 2 * 4] = (R + L) / (R - L);

        elements[1 + 1 * 4] = (2 * N) / (T - B);
        elements[1 + 2 * 4] = (T + B) / (T - B);

        elements[2 + 2 * 4] = (N + F) / (N - F);
        elements[2 + 3 * 4] = (2 * N * F) / (N - F);

        elements[3 + 2 * 4] = -1;
        return new Matrix4f(elements);
    }

    public static Matrix4f getPerspective(float N, float F, float aspectRatio, float FOV) {

        //TODO: derive perspective and set it
        float[] elements = new float[16];
        FOV = (float) Math.toRadians(FOV);
        float tan = (float) Math.tan(FOV / 2);
        elements[0 + 0 * 4] = 1 / (aspectRatio * tan);
        elements[1 + 1 * 4] = 1 / tan;
        elements[2 + 2 * 4] = (F + N) / (N - F);
        elements[2 + 3 * 4] = (2 * F * N) / (N - F);
        elements[3 + 2 * 4] = -1;
        return new Matrix4f(elements);
    }

    //REVERSE-PERSPECTIVE PROJECTION MATRIX
    //TODO

    //LOOK-AT / VIEW MATRIX
    public static Matrix4f getViewRotation(Camera camera) {
        //Update camera coordinates
        //(find unit perpendicular unit vectors)
        camera.updateCameraCoords();

        float[] elements = new float[16];

        elements[0 + 0 * 4] = camera.cameraX.x;
        elements[0 + 1 * 4] = camera.cameraX.y;
        elements[0 + 2 * 4] = camera.cameraX.z;

        elements[1 + 0 * 4] = camera.cameraY.x;
        elements[1 + 1 * 4] = camera.cameraY.y;
        elements[1 + 2 * 4] = camera.cameraY.z;

        elements[2 + 0 * 4] = camera.cameraZ.x;
        elements[2 + 1 * 4] = camera.cameraZ.y;
        elements[2 + 2 * 4] = camera.cameraZ.z;

        elements[3 + 3 * 4] = 1;

        //TODO: include rotation from locked celObj if != null

        return new Matrix4f(elements);
    }

    public static Matrix4f getView(Matrix4f vw_rot_mat, Camera camera) {
        Matrix4f vw_mat = multiply(
                vw_rot_mat,
                getTranslation4f(negate(camera.getPosition()))
        );

        CelObj lockedCelObj = camera.getLockedCelObj();
        if (lockedCelObj != null) {
            vw_mat = multiply(
                    vw_mat,
                    getTranslation4f(new Vec3f(
                            negate(
                                    multiply(worldunit_per_meters,
                                            lockedCelObj.getCelPos())
                            ))
                    )
            );
        }

        return vw_mat;
    }

    //MODEL MATRIX
    public static Matrix4f getModel(RenderObject obj) {
        //Scale first, then rotate, then translate
        return getScale4f(obj.getScale())
                .rotate(obj.getRotation())
                .translate(obj.getPosition());
    }

    public static Matrix4f getSkyModel(RenderObject sky) {
        return getScale4f(sky.getScale())
                .rotate(sky.getRotation());
    }

    //MODEL-VIEW MATRIX
    public static Matrix4f getModelView(RenderObject obj, Matrix4f vw_mat) {
        return multiply(vw_mat, getModel(obj));
    }

    public static Matrix4f getSkyModelView(Matrix4f vw_rot_mat, RenderObject sky) {
        return multiply(vw_rot_mat, getSkyModel(sky));
    }
}
