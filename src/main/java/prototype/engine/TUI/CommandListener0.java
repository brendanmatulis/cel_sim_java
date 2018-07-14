package prototype.engine.TUI;

import prototype.math.Vec3d;
import prototype.math.Vec3f;
import prototype.objects.CelestialMechanics.CelObj;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static prototype.engine.physics.EulerMethod.worldunit_per_meters;
import static prototype.math.Operations.add;
import static prototype.math.Operations.multiply;
import static prototype.program.Main.*;
import static prototype.utils.Timing.*;

public class CommandListener0 {

    public static void process(String cmd) {
        String tokenNotFound = "Token not recognized...";
        String notEnoughInfo = "Not enough info given. Please specify...";
        String unableToParse = "Unable to parse number...";

        String[] tokens = cmd.toLowerCase().split("\\s+");
        switch (tokens[0]) {
            case "close":
                run = false;
                break;
            case "toggle":
                if (tokens.length >= 2) {
                    switch (tokens[1]) {
                        case "render":
                            render = !render;
                            System.out.println("Render setting set to " + render);
                            break;
                        case "physics":
                            updatePhysics = !updatePhysics;
                            System.out.println("Physics update setting set to " + updatePhysics);
                            break;
                        case "trails":
                            showTrails = !showTrails;
                            System.out.println("Show trails setting set to " + showTrails);
                            break;
                        case "constellations":
                            showConstellations = !showConstellations;
                            System.out.println("Show constellations setting set to " + showConstellations);
                            break;
                        default:
                            System.err.println(tokenNotFound);
                    }
                } else
                    System.err.println(notEnoughInfo);
                break;
            case "get":
                if (tokens.length >= 2) {
                    switch (tokens[1]) {
                        case "fps":
                            System.out.println("FPS: " + realFPS);
                            break;
                        case "ups":
                            System.out.println("UPS: " + realUPS);
                            break;
                        case "cps":
                            System.out.println("CPS: " + realCPS);
                            break;
                        case "performance":
                            System.out.println("FPS: " + realFPS + " UPS: " + realUPS + " CPS: " + realCPS);
                            break;
                        case "time":
                            System.out.println(getTimestamp());
                            break;
                        case "celobj":
                            if (tokens.length >= 3) {
                                boolean objFound = false;
                                CelObj chosenObj = null;
                                for (CelObj celObj : physics.getCelObjs()) {
                                    if (celObj.getName().toLowerCase().equals(tokens[2])) {
                                        objFound = true;
                                        chosenObj = celObj;
                                    }
                                }

                                if (objFound) {
                                    if (tokens.length >= 4) {
                                        switch (tokens[3]) {
                                            case "pos":
                                                Vec3d pos = chosenObj.getCelPos();
                                                System.out.println("POSITION: X= " + pos.x + " Y= " + pos.y + " Z= " + pos.z);
                                                break;
                                            case "vel":
                                                Vec3d vel = chosenObj.getCelVel();
                                                if (tokens.length >= 5 && tokens[4].equals("mag"))
                                                    System.out.println("VELOCITY: V= " + vel.magnitude());
                                                else
                                                    System.out.println("VELOCITY: X= " + vel.x + " Y= " + vel.y + " Z= " + vel.z);
                                                break;
                                            case "mass":
                                                System.out.println("MASS: M= " + chosenObj.getMass());
                                                break;
                                            case "radius":
                                                System.out.println("RADIUS: R= " + chosenObj.getRadius());
                                                break;
                                            default:
                                                System.err.println(notEnoughInfo);
                                        }
                                    } else
                                        System.err.println(notEnoughInfo);
                                } else
                                    System.err.println(tokenNotFound);
                            } else {
                                //Print list of celObjs
                                StringBuilder celObjNames = new StringBuilder("Celestial Objects: \n");
                                for (CelObj celObj : physics.getCelObjs()) {
                                    celObjNames.append('\t').append(celObj.getName()).append('\n');
                                }
                                System.out.println(celObjNames.toString());
                            }
                            break;
                        default:
                            System.err.println(tokenNotFound);
                    }
                } else
                    System.err.println(notEnoughInfo);
                break;
            case "set":
                if (tokens.length >= 2) {
                    switch (tokens[1]) {
                        case "cps":
                            if (tokens.length >= 3) {
                                try {
                                    int newCPS = parseInt(tokens[2]);
                                    realTime_to_simTime = newCPS * timeInc;
                                    System.out.println("Target CPS set to " + newCPS);
                                    System.out.println("The simulation time is " + realTime_to_simTime + " times the speed of real time");
                                } catch (Exception e) {
                                    System.err.println(unableToParse);
                                }
                            } else
                                System.err.println(notEnoughInfo);
                            break;
                        case "scale":
                            if (tokens.length >= 3) {
                                try {
                                    scale = parseFloat(tokens[2]);
                                    System.out.println("Render object scale is now set to " + scale);
                                } catch (Exception e) {
                                    System.err.println(unableToParse);
                                }
                            } else
                                System.err.println(notEnoughInfo);
                            break;
                        case "speed":
                            if (tokens.length >= 3) {
                                try {
                                    speed = parseFloat(tokens[2]);
                                    System.out.println("Speed is now set to " + speed);
                                } catch (Exception e) {
                                    System.err.println(unableToParse);
                                }
                            } else
                                System.err.println(notEnoughInfo);
                            break;
                        case "record":
                            if (tokens.length >= 4) {
                                if (tokens[3].equals("period")) {
                                    try {
                                        int newCPS = parseInt(tokens[2]);
                                        realTime_to_simTime = newCPS * timeInc;
                                        System.out.println("Target CPS set to " + newCPS);
                                        System.out.println("The recording period is now every " + realTime_to_simTime + " seconds");
                                    } catch (Exception e) {
                                        System.err.println(unableToParse);
                                    }
                                } else
                                    System.err.println(tokenNotFound);

                            } else
                                System.err.println(notEnoughInfo);
                            break;
                        case "center":
                            if (tokens.length >= 3) {
                                boolean objFound = false;
                                CelObj chosenObj = null;
                                for (CelObj celObj : physics.getCelObjs()) {
                                    if (celObj.getName().toLowerCase().equals(tokens[2])) {
                                        objFound = true;
                                        chosenObj = celObj;
                                    }
                                }

                                if (objFound) {
                                    centerCelObj = chosenObj;
                                    System.out.println(chosenObj.getName() + " has been set as the center");
                                } else
                                    System.err.println(tokenNotFound);
                            } else
                                System.err.println(notEnoughInfo);
                            break;
                        default:
                            System.err.println(tokenNotFound);
                    }
                } else
                    System.err.println(notEnoughInfo);
                break;
            case "create":
                //TODO:
                break;
            case "uncenter":
                centerCelObj = null;
                System.out.println("There is currently no center object");
                break;
            case "tele":
                if (tokens.length >= 2) {
                    switch (tokens[1]) {
                        case "pos":
                            if (tokens.length >= 5) {
                                try {
                                    double x = parseDouble(tokens[2]);
                                    double y = parseDouble(tokens[3]);
                                    double z = parseDouble(tokens[4]);

                                    Vec3f cameraPos = new Vec3f((float) x, (float) y, (float) z);

                                    //If camera is locked to an object, then translating the camera will only
                                    //do so with respect to that planet
                                    //Hence, the camera is translated back to world coordinates by translating it
                                    //the negative of its locked celObj position vector
                                    if (camera.getLockedCelObj() != null) {
                                        cameraPos.translate(
                                                new Vec3f(
                                                        camera.getLockedCelObj().getCelPos()
                                                ).negate()
                                        );
                                    }

                                    camera.setPosition(multiply(worldunit_per_meters, cameraPos));
                                    System.out.println("The camera position has been set to X= " + x + " Y= " + y + " Z= " + z);
                                } catch (Exception e) {
                                    System.err.println(unableToParse);
                                }
                            } else
                                System.err.println(notEnoughInfo);
                            break;
                        case "celobj":
                            if (tokens.length >= 3) {
                                boolean objFound = false;
                                CelObj chosenObj = null;
                                for (CelObj celObj : physics.getCelObjs()) {
                                    if (celObj.getName().toLowerCase().equals(tokens[2])) {
                                        objFound = true;
                                        chosenObj = celObj;
                                    }
                                }

                                if (objFound) {
                                    Vec3f celObjPos = new Vec3f(chosenObj.getCelPos());
                                    Vec3f cameraPos = add(celObjPos, new Vec3f(0, (float) chosenObj.getRadius() * 2, 0));

                                    //If camera is locked to an object, then translating the camera will only
                                    //do so with respect to that planet
                                    //Hence, the camera is translated back to world coordinates by translating it
                                    //the negative of its locked celObj position vector
                                    if (camera.getLockedCelObj() != null) {
                                        cameraPos.translate(
                                                new Vec3f(
                                                        camera.getLockedCelObj().getCelPos()
                                                ).negate()
                                        );
                                    }

                                    //Make player 1 planet radius away from surface
                                    camera.setPosition(multiply(worldunit_per_meters, cameraPos));
                                    camera.setLookAt(multiply(worldunit_per_meters, celObjPos));

                                    System.out.println("The camera position has been set to X= " + cameraPos.x + " Y= " + cameraPos.y + " Z= " + cameraPos.z);
                                } else
                                    System.err.println(tokenNotFound);
                            } else
                                System.err.println(notEnoughInfo);
                            break;
                        default:
                            System.err.println(tokenNotFound);
                    }
                } else
                    System.err.println(notEnoughInfo);
                break;
            case "lock":
                if (tokens.length >= 2) {
                    boolean objFound = false;
                    CelObj chosenObj = null;
                    for (CelObj celObj : physics.getCelObjs()) {
                        if (celObj.getName().toLowerCase().equals(tokens[1])) {
                            objFound = true;
                            chosenObj = celObj;
                        }
                    }

                    if (objFound) {
                        camera.setLockedCelObj(chosenObj);
                        camera.setPosition(new Vec3f(0, 0, 0));
                        System.out.println("The camera has been locked to " + chosenObj.getName());
                        System.out.println("Type 'unlock' to revert back to unlocked status");
                    } else
                        System.err.println(tokenNotFound);
                } else
                    System.err.println(notEnoughInfo);
                break;
            case "unlock":
                String celObjName = camera.getLockedCelObj().getName();
                //If camera is locked to an object, then translating the camera will only
                //do so with respect to that planet
                //Hence, the camera is translated back to world equivalent of where it is in
                //that celObj's space by translating it the celObj's position
                camera.setPosition(
                        add(
                                multiply(worldunit_per_meters,
                                        (new Vec3f(camera.getLockedCelObj().getCelPos()))),
                                camera.getPosition()
                        )
                );
                camera.setLockedCelObj(null);
                System.out.println("The camera has been unlocked from " + celObjName);
                Vec3f pos = camera.getPosition();
                System.out.println("The camera position has been set to X= " + pos.x + " Y= " + pos.y + " Z= " + pos.z);
                break;
            case "lookat":
                if (tokens.length >= 3) {
                    switch (tokens[1]) {
                        case "celobj":
                            boolean objFound = false;
                            CelObj chosenObj = null;
                            for (CelObj celObj : physics.getCelObjs()) {
                                if (celObj.getName().toLowerCase().equals(tokens[2])) {
                                    objFound = true;
                                    chosenObj = celObj;
                                }
                            }

                            if (objFound) {
                                camera.setLookAt(new Vec3f(multiply(worldunit_per_meters, chosenObj.getCelPos())));
                                System.out.println("The camera is now looking at " + chosenObj.getName());
                            } else
                                System.err.println(tokenNotFound);
                            break;
                        case "star":
                            //TODO
                            break;
                        default:
                            System.err.println(tokenNotFound);
                    }
                } else
                    System.err.println(notEnoughInfo);
                break;
            default:
                System.err.println(tokenNotFound);
        }
    }
}
