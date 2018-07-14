package prototype.engine.physics;

import prototype.objects.CelestialMechanics.CelObj;
import prototype.objects.RenderObject;

import java.util.ArrayList;

public interface IPhysics {
    void init();
    ArrayList<RenderObject> getRenderObjs(float scale);
    void update(double timeInc);
    void renderUpdate(float interval);
    void record();
    double getSimulationTime();
    ArrayList<CelObj> getCelObjs();
}
