package prototype.objects;

import prototype.math.Vec3b;
import prototype.math.Vec3f;

import java.io.Serializable;
import java.math.BigDecimal;

public class RenderObject implements Serializable {
    private Mesh mesh;
    private Vec3f position, rotation;
    private float scale;
    private boolean visible;

    public RenderObject(RenderObject obj) {
        mesh = obj.mesh;
        position = new Vec3f(obj.position);
        scale = obj.scale;
        rotation = new Vec3f(obj.rotation);
        visible = obj.visible;
    }

    public RenderObject(Mesh mesh) {
        this.mesh = mesh;
        position = new Vec3f(0f, 0f, 0f);
        scale = 1;
        rotation = new Vec3f(0f, 0f, 0f);
        visible = true;
    }

    public Vec3f getPosition() { return position; }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setPosition(Vec3f vec3f) {
        position.set(vec3f);
    }

    public void setPosition(Vec3b vec3b) {
        position.set(vec3b);
    }

    public void movePos(Vec3f vec3f) {
        position.add(vec3f);
    }

    public float getScale() { return scale; }

    public void setScale(float scale) { this.scale = scale; }

    public Vec3f getRotation() { return rotation; }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void moveRot(Vec3f vec3f) {
        rotation.add(vec3f);
    }

    public void setRotation(Vec3f vec3f) {
        rotation.set(vec3f);
    }

    public void setRotation(Vec3b vec3b) {
        rotation.set(vec3b);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh.cleanup();
        this.mesh = mesh;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
