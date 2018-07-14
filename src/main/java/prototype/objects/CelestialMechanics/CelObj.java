package prototype.objects.CelestialMechanics;

import prototype.math.Vec3d;
import prototype.math.Vec3f;
import prototype.objects.Mesh;
import prototype.objects.RenderObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static prototype.engine.physics.EulerMethod.worldunit_per_meters;
import static prototype.math.Operations.multiply;
import static prototype.utils.GeneralUtils.floatListToArray;

public class CelObj implements Serializable {
    private String name;
    private Vec3d celPos, celVel, celRot;
    private double mass;
    private double radius;
    private Vec3f color;
    private ExplosionObject explosion;
    private boolean terminating;
    private boolean terminated;

    private HashMap<String,RenderObject> renderObjects;
    private RenderObject trail = null;
    private boolean trailUpToDate;
    private ArrayList<Float> positionList;

    public CelObj(Vec3f color, double mass, double radius, String name) {
        this.color = color;
        this.mass = mass;
        this.radius = radius;
        renderObjects = new HashMap<>();
        positionList = new ArrayList<>();
        celPos = new Vec3d();
        celVel = new Vec3d();
        celRot = new Vec3d();
        terminated = false;
        terminating = false;
        this.name = name;
    }

    public CelObj(CelObj celObj) {
        color = celObj.color;
        mass = celObj.mass;
        radius = celObj.radius;
        celPos = celObj.celPos;
        celVel = celObj.celVel;
        celRot = celObj.celRot;
        terminated = celObj.terminated;
        terminating = celObj.terminating;
        renderObjects = celObj.renderObjects;
        positionList = celObj.positionList;
        explosion = celObj.explosion;
        name = celObj.name;
        trail = celObj.trail;
    }

    public void add(String name, RenderObject renderObject) {
        renderObjects.put(name, renderObject);
    }

    public void terminate(Vec3f colorA, Vec3f colorB, float duration, int numParticles) {
        if (!terminating) {
            explosion = new ExplosionObject(duration, numParticles, colorA, colorB);
            explosion.init();
            renderObjects.clear();
            renderObjects.put("Explosion", explosion.getRenderObject());
            terminating = true;
            mass = 0;
            celVel = new Vec3d();
        }
    }

    public void updateExplosion(float interval) {
        if (terminating) {
            if (explosion != null && !explosion.isDurationUp()) {
                explosion.update(interval);
                renderObjects.replace("Explosion", explosion.getRenderObject());
            } else {
                terminating = false;
                terminated = true;
            }
        }
    }

    public ArrayList<RenderObject> getRenderObjects() {
        Collection<RenderObject> collection = renderObjects.values();
        return new ArrayList<>(collection);
    }

    public void recordData() {
        Vec3f pos = new Vec3f(multiply(worldunit_per_meters, celPos));
        positionList.add(pos.x);
        positionList.add(pos.y);
        positionList.add(pos.z);

        trailUpToDate = false;
    }

    public RenderObject getTrail() {
        if (positionList.size() < 1) {
            return null;
        } else if (trail == null) {
            Mesh trailMesh = new Mesh(floatListToArray(positionList), color);
            trailMesh.setPointSize(3f);
            trail = new RenderObject(trailMesh);
            trailUpToDate = true;
        } else if (!trailUpToDate) {
            Mesh trailMesh = new Mesh(floatListToArray(positionList), color);
            trailMesh.setPointSize(3f);
            trail.setMesh(trailMesh);
            trailUpToDate = true;
        }

        return trail;
    }

    public ArrayList<Float> getPositionList() {
        return positionList;
    }

    public Vec3d getCelPos() {
        return celPos;
    }

    public void setCelPos(Vec3d celPos) {
        this.celPos = celPos;
    }

    public Vec3d getCelVel() {
        return celVel;
    }

    public void setCelVel(Vec3d celVel) {
        this.celVel = celVel;
    }

    public Vec3d getCelRot() {
        return celRot;
    }

    public void setCelRot(Vec3d celRot) {
        this.celRot = celRot;
    }

    public void moveCelPos(Vec3d offset) {
        this.celPos.add(offset);
    }

    public void moveCelVel(Vec3d offset) {
        this.celVel.add(offset);
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRadius() { return radius; }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Vec3f getColor() { return color; }

    public boolean isTerminating() {
        return terminating;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
