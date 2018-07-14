package prototype.engine.graphics;

import prototype.math.Vec3f;
import prototype.objects.CelestialMechanics.CelObj;

import static java.lang.Math.*;
import static prototype.math.Operations.*;

public class Camera {
    private Vec3f position;

    //A vector from the camera's origin to what it's looking at
    private Vec3f lookAt;
    public Vec3f cameraX, cameraY, cameraZ;
    private Vec3f up = new Vec3f(0, 1, 0);

    private float maxPitch = (float) toRadians(89);
    private float yaw, pitch;

    private boolean freeCam = false;

    private CelObj lockedCelObj = null;

    public Camera(Vec3f position, Vec3f targetPos) {
        this.position = position;
        lookAt = subtract(targetPos, position);
        updateCameraCoords();
    }

    public Vec3f getPosition() { return position; }

    public void movePosition(Vec3f offset) {
        //Right / Left (x)
        if (offset.x != 0)
            position.add(multiply(offset.x, cameraX));


        //Up / Down (y)
        if (offset.y != 0) {
            if (freeCam)
                position.add(multiply(offset.y, cameraY));
            else
                position.y += offset.y;
        }

        //Forward / Backward (z)
        //NOTE: cameraZ is negative, so scalar is expected to be
        //positive when moving forwards
        if (offset.z != 0) {
            if (freeCam)
                position.add(multiply(offset.z, cameraZ));
            else {
                //Ignoring component of z out of x-z plane in world coordinates
                //(A new vector is created with only the component of cameraZ
                //in the world-coordinate x-y plane and then it is normalized
                //so that orientation does not affect magnitude)
                position.add(multiply(offset.z,
                        new Vec3f(cameraZ.x, 0, cameraZ.z).normalize())
                );
            }

        }

    }

    public void moveLookAt(float delta_yaw, float delta_pitch) {
        //Assume yaw = 0 and pitch = 0 when lookAt == world_z
        yaw += (float) toRadians(delta_yaw);
        pitch += (float) toRadians(delta_pitch);

        if (yaw > 2 * PI || yaw < -2 * PI) {
            yaw = 0;
        }
        if (pitch > maxPitch) {
            pitch = maxPitch;
        } else if (pitch < -maxPitch) {
            pitch = -maxPitch;
        }

        float cos_pitch = (float) cos(pitch);

        lookAt.x = (float) (cos_pitch * -sin(yaw));
        lookAt.y = (float) sin(pitch);
        lookAt.z = (float) (cos_pitch * -cos(yaw));
        //TODO: make it add to lookAt, not recalculate it
    }

    public void updateCameraCoords() {
        cameraZ = negate(lookAt.normalize());
        cameraX = cross(up, cameraZ).normalize();
        cameraY = cross(cameraZ, cameraX).normalize();
    }

    public void setMaxPitch(float degrees) {
        maxPitch = (float) toRadians(degrees);
    }

    public void setPosition(Vec3f position) {
        this.position = position;
    }

    public void setLookAt(Vec3f targetPos) {
        lookAt = subtract(targetPos, position);
        updateCameraCoords();
    }

    public CelObj getLockedCelObj() {
        return lockedCelObj;
    }

    public void setLockedCelObj(CelObj lockedCelObj) {
        this.lockedCelObj = lockedCelObj;
    }
}
