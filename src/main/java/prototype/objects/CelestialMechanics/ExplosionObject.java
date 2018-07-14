package prototype.objects.CelestialMechanics;

import prototype.math.Vec3f;
import prototype.objects.Mesh;
import prototype.objects.RenderObject;

import java.util.ArrayList;

import static java.lang.Math.random;
import static prototype.math.Operations.multiply;

public class ExplosionObject {
    //1 world unit per second
    public static final float DEFAULT_MAX_VELOCITY = 1;
    private float maxVelocity;

    private Mesh mesh;

    private ArrayList<Vec3f> particleVelocities;
    private ArrayList<Vec3f> particlePositions;
    private ArrayList<Vec3f> colors;

    private float startTime;
    private float duration;

    private Vec3f position;

    private Vec3f colorA, colorB;

    boolean initiated;

    private int numParticles;

    public ExplosionObject(float duration, int numParticles, Vec3f colorA, Vec3f colorB) {
        this.duration = duration;
        this.colorA = colorA;
        this.colorB = colorB;
        this.numParticles = numParticles;
        maxVelocity = DEFAULT_MAX_VELOCITY;
        particleVelocities = new ArrayList<>();
        particlePositions = new ArrayList<>();
        colors = new ArrayList<>();
        initiated = false;
    }

    public void init() {
        //Set start time
        startTime = (float) (System.nanoTime() / 1E9);
        initiated = true;

        //Get velocity vectors for particles
        for (int i = 0; i < numParticles; i++) {
            Vec3f vel = new Vec3f(
                    (float) random() * (random() < 0.5 ? -1 : 1),
                    (float) random() * (random() < 0.5 ? -1 : 1),
                    (float) random() * (random() < 0.5 ? -1 : 1)
            );

            Vec3f color = (random() > 0.5 ? colorA : colorB);
            colors.add(color);

            //Ensure that direction of vector does not affect magnitude
            vel.normalize().multiply((float) random() * maxVelocity);

            particleVelocities.add(vel);

            //Add initial positions at the mesh origin
            particlePositions.add(new Vec3f(0, 0, 0));
        }
        update(0);
    }

    public void update(float interval) {
        updateMesh(interval);
    }

    public void updateMesh(float interval) {

        if ((System.nanoTime() / 1E9) - startTime >= duration) {
            int particlesToDestroy = (int) (Math.random() * numParticles / 10);

            for (int i = 0; i < particlesToDestroy; i++) {
                if (particlePositions.size() > 0) {
                    particlePositions.remove(particlePositions.size() - 1);
                    colors.remove(colors.size() - 1);
                }
            }

        }

        float[] posArr = new float[particlePositions.size() * 3];
        float[] colorArr = new float[posArr.length];

        int i = 0;
        for (Vec3f pos : particlePositions) {
            //Update position
            pos.add(multiply(interval, particleVelocities.get(i)));

            posArr[3 * i] = pos.x;
            posArr[3 * i + 1] = pos.y;
            posArr[3 * i + 2] = pos.z;

            Vec3f color = colors.get(i);
            colorArr[3 * i] = color.x;
            colorArr[3 * i + 1] = color.y;
            colorArr[3 * i + 2] = color.z;

            i++;
        }

        mesh = new Mesh(posArr, colorArr);
    }

    public RenderObject getRenderObject() {
        if (initiated) {
            return new RenderObject(mesh);
        } else {
            //In case the mesh is accidentally renderer before its first update and
            //it is not initiated in program
            update(0.0f);
            return new RenderObject(mesh);
        }
    }

    public boolean isDurationUp() {
        return (System.nanoTime() / 1E9) - startTime >= duration;
    }

    public boolean destroyExplosionObj() {
        return (System.nanoTime() / 1E9) - startTime >= 3 * duration;
    }
}
