package prototype.engine.physics;

import prototype.math.Vec3d;
import prototype.math.Vec3f;
import prototype.objects.CelestialMechanics.CelObj;
import prototype.objects.RenderObject;

import java.util.ArrayList;

import static prototype.math.Operations.*;
import static prototype.utils.ParseSetups.loadPlanet_d;

public class EulerMethod implements IPhysics {
    private ArrayList<CelObj> celObjs;
    private ArrayList<CelObj> processedObjs;
    private ArrayList<CelObj> terminatedObjs;

    private double simulationTime;


    //Note: SI units used throughout unless otherwise stated
    //e.g. radians, kg, N, m
    public static final double G = 6.67408E-11;
    public static final double EarthMass = 5.9722E24;
    public static final double EarthRadius = 6.371E6;
    public static final float worldunit_per_meters = 1f / 1E7f;

    @Override
    public void init() {
        celObjs = new ArrayList<>();
        processedObjs = new ArrayList<>();
        terminatedObjs = new ArrayList<>();

        simulationTime = 0;
        loadObjects();
    }

    @Override
    public ArrayList<RenderObject> getRenderObjs(float scale) {
        ArrayList<RenderObject> renderObjs = new ArrayList<>();

        for (CelObj celObj : celObjs) {
            Vec3f world_pos = multiply(worldunit_per_meters, new Vec3f(celObj.getCelPos()));

            Vec3f rot3f = new Vec3f(celObj.getCelRot());

            for (RenderObject renderObj : celObj.getRenderObjects()) {
                RenderObject copy = new RenderObject(renderObj);
                copy.setScale((float) (celObj.getRadius() * worldunit_per_meters) * scale);
                copy.movePos(world_pos);
                copy.moveRot(rot3f);
                renderObjs.add(copy);
            }
        }

        return renderObjs;
    }

    @Override
    public void update(double timeInc) {
        //Euler method
        for (int i = 0; i < celObjs.size(); i++) {
            CelObj celObj1 = celObjs.get(i);
            if (!celObj1.isTerminating() && !celObj1.isTerminated()) {
                //Get total acceleration of celObj1
                Vec3d net_accel = new Vec3d();
                for (int j = 0; j < celObjs.size(); j++) {
                    if (i != j) {
                        CelObj celObj2 = celObjs.get(j);
                        double m2 = celObj2.getMass();

                        //Radial displacement
                        Vec3d disp = subtract(celObj2.getCelPos(), celObj1.getCelPos());

                        Vec3d accel = multiply(
                                m2 / dot(disp, disp),
                                disp.normalize()
                        );

                        net_accel.add(accel);
                    }
                }
                net_accel.multiply(G);

                //Create copy of old celObj1 that will be added to processedCelObjs
                CelObj processedCelObj = new CelObj(celObj1);

                //Calculate change in velocity and position of celObj1 using kinematic equations
                Vec3d deltaVel = multiply(timeInc, net_accel);
                processedCelObj.moveCelVel(deltaVel);

                //avgVel    = (1/2)(v0 + v1)
                //deltaPos = avgVel * timeInc
                //         = (1/2)(v0 + v1) * timeInc
                Vec3d deltaPos = multiply(
                        0.5 * timeInc,
                        add(celObj1.getCelVel(), processedCelObj.getCelVel())
                );
                processedCelObj.moveCelPos(deltaPos);

                processedObjs.add(processedCelObj);
            } else {
                //If terminated or terminating, do nothing and add to processed
                processedObjs.add(celObj1);
            }
        }

        //Reset celObjs
        celObjs.clear();

        //Collision and termination handling

        for (int i = processedObjs.size() - 1; i >= 0; i--) {
            CelObj celObj1 = processedObjs.get(i);
            if (celObj1.isTerminated()) {
                //If terminated, destroy it and move it to "graveyard"
                processedObjs.remove(i);
                //Now that the obj is removed, size decreases by 1 and so does index
                terminatedObjs.add(celObj1);
            } else if (!celObj1.isTerminating()) {
                for (int j = 0; j < processedObjs.size(); j++) {
                    if (i != j) {
                        CelObj celObj2 = processedObjs.get(j);

                        if (!celObj2.isTerminated() || !celObj2.isTerminating()) {
                            //Distance between the two objects
                            double distance = subtract(
                                    celObj1.getCelPos(), celObj2.getCelPos()
                            ).magnitude();

                            //If collision, make one bounce and terminate the other of lesser mass
                            if (distance <= celObj1.getRadius() || distance <= celObj2.getRadius()) {
                                collide(celObj1, celObj2);
                            }
                        }
                    }
                }

                //Remove from processedObjs and add it back to celObjs
                celObjs.add(celObj1);
                processedObjs.remove(i);
            } else {
                //If celObj1 is terminating, simply add to
                celObjs.add(celObj1);
                processedObjs.remove(i);
            }
        }

        simulationTime += timeInc;
    }

    @Override
    public void renderUpdate(float interval) {
        for (CelObj celObj : celObjs) {
            if (celObj.isTerminating()) {
                //Update explosion graphics
                celObj.updateExplosion(interval);
            }
        }
    }

    private void loadObjects() {
        //Sun
        celObjs.add(loadPlanet_d("/data/setups/CelObj.setup", "Sun", "Sun"));

        //Mercury
        celObjs.add(loadPlanet_d("/data/setups/CelObj.setup", "Mercury", "Mercury"));

        //Venus
        celObjs.add(loadPlanet_d("/data/setups/CelObj.setup", "Venus", "Venus"));

        //Earth
        celObjs.add(loadPlanet_d("/data/setups/CelObj.setup", "Earth", "Earth"));

        //Mars
        celObjs.add(loadPlanet_d("/data/setups/CelObj.setup", "Mars", "Mars"));

        //Jupiter
        celObjs.add(loadPlanet_d("/data/setups/CelObj.setup", "Jupiter", "Jupiter"));

        //Saturn
        celObjs.add(loadPlanet_d("/data/setups/CelObj.setup", "Saturn", "Saturn"));

        //Uranus
        celObjs.add(loadPlanet_d("/data/setups/CelObj.setup", "Uranus", "Uranus"));

        //Neptune
        celObjs.add(loadPlanet_d("/data/setups/CelObj.setup", "Neptune", "Neptune"));


//        CelObj mars2 = loadPlanet_d("/data/setups/CelObj.setup", "Mars", "Mars2");
//        mars2.setCelPos(new Vec3d(3E7, 0, 0));
//        mars2.setCelVel(new Vec3d(0, 0, 3600));
//        celObjs.add(mars2);

//        CelObj mars3 = loadPlanet_d("/data/setups/CelObj.setup", "Mars", "Mars3");
//        mars3.setCelPos(new Vec3d(2E3, 5E8, 6E5));
//        celObjs.add(mars3);
//
//        CelObj mars4 = loadPlanet_d("/data/setups/CelObj.setup", "Mars");
//        mars4.setCelPos(new Vec3d(0, -5E3, -5E7));
//        celObjs.add(mars4);
//
//        CelObj mars5 = loadPlanet_d("/data/setups/CelObj.setup", "Mars");
//        mars5.setCelPos(new Vec3d(3E6, 8E3, 10E7));
//        celObjs.add(mars5);
//
//        CelObj mars6 = loadPlanet_d("/data/setups/CelObj.setup", "Mars");
//        mars6.setCelPos(new Vec3d(-3E7, 5E3, 5E7));
//        celObjs.add(mars6);
//
//        CelObj mars7 = loadPlanet_d("/data/setups/CelObj.setup", "Mars");
//        mars7.setCelPos(new Vec3d(-3E7, -2E8, 5E7));
//        celObjs.add(mars7);
//
//        CelObj mars8 = loadPlanet_d("/data/setups/CelObj.setup", "Mars");
//        mars8.setCelPos(new Vec3d(-3E7, -2E8, -5E7));
//        celObjs.add(mars8);
//
//        CelObj mars9 = loadPlanet_d("/data/setups/CelObj.setup", "Mars");
//        mars9.setCelPos(new Vec3d(-3E7, 5E8, -5E7));
//        celObjs.add(mars9);
//
//        CelObj mars10 = loadPlanet_d("/data/setups/CelObj.setup", "Mars");
//        mars10.setCelPos(new Vec3d(3E7, -5E8, 5E7));
//        celObjs.add(mars10);
//
//        CelObj mars11 = loadPlanet_d("/data/setups/CelObj.setup", "Mars");
//        mars11.setCelPos(new Vec3d(3E7, 2E8, 5E7));
//        celObjs.add(mars11);
    }

    private static void collide(CelObj A, CelObj B) {
        double mA = A.getMass();
        double mB = B.getMass();

        //Assuming colliding objects form one new object
        if (mA >= mB) {
            double radius = ((mA + mB) / mA) * A.getRadius();
            A.setRadius(radius);
            A.setMass(mA + mB);

            //Using conservation of momentum law and the fact that the
            //new object has combined mass
            Vec3d velA = add(
                    multiply(mA, A.getCelVel()),
                    multiply(mB, B.getCelVel()))
                    .multiply(1 / (mA + mB));
            A.setCelVel(velA);

            B.terminate(
                    A.getColor(),
                    B.getColor(),
                    8,
                    10000
            );
        } else {
            double radius = ((mA + mB) / mB) * B.getRadius();
            B.setRadius(radius);
            B.setMass(mA + mB);

            //Using conservation of momentum law and the fact that the
            //new object has combined mass
            Vec3d velB = add(
                    multiply(mA, A.getCelVel()),
                    multiply(mB, B.getCelVel()))
                    .multiply(1 / (mA + mB));
            B.setCelVel(velB);


            A.terminate(
                    A.getColor(),
                    B.getColor(),
                    8,
                    10000
            );
        }
    }

    @Override
    public void record() {
        for (CelObj celObj : celObjs) {
            celObj.recordData();
        }
    }

    @Override
    public double getSimulationTime() {
        return simulationTime;
    }

    @Override
    public ArrayList<CelObj> getCelObjs() {
        ArrayList<CelObj> celList = new ArrayList<>(celObjs);
        if (terminatedObjs != null) {
            celList.addAll(terminatedObjs);
        }
        return celList;
    }
}
