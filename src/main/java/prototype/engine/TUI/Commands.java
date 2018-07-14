package prototype.engine.TUI;

import prototype.math.Vec3d;
import prototype.objects.CelestialMechanics.CelObj;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static prototype.program.Main.*;
import static prototype.program.Main.showTrails;
import static prototype.utils.Timing.*;

public class Commands {
    // CREDITS
    //
    // NOTE: Code gotten from John Kugelman (https://stackoverflow.com/questions/51201059/what-is-a-good-way-to-organize-commands-for-a-command-line-interface/51201985#51201985)
    //
    //

    private static String tokenNotFound = "[ERROR] Token not recognized...";
    private static String notEnoughInfo = "[ERROR] Not enough info given. Please specify...";
    private static String unableToParse = "[ERROR] Unable to parse number...";

    //Main commands
    private static Map<String, CommandHandler> cmds = new HashMap<>();

    //Sub commands
    private static Map<String, CommandHandler> toggleCmds = new HashMap<>();
    private static Map<String, CommandHandler> getCmds = new HashMap<>();

    public static void init() {
        //Close command
        cmds.put("close", args -> {
            System.out.println("[CONSOLE] Closing...");
        run = false;
        });

        //Toggle commands
        cmds.put("toggle", args -> {
            if (args.size() == 0) {
                System.err.println(notEnoughInfo);
            } else
                process(args, toggleCmds);
        });

        toggleCmds.put("render", args -> {
            render = !render;
            System.out.println("[CONSOLE] Render setting set to " + render);
        });

        toggleCmds.put("physics", args -> {
            updatePhysics = !updatePhysics;
            System.out.println("[CONSOLE] Physics update setting set to " + updatePhysics);
        });

        toggleCmds.put("trails", args -> {
            showTrails = !showTrails;
            System.out.println("[CONSOLE] Show trails setting set to " + showTrails);
        });

        toggleCmds.put("constellations", args -> {
            showConstellations = !showConstellations;
            System.out.println("[CONSOLE] Show constellations setting set to " + showConstellations);
        });

        //Get commands
        cmds.put("get", args -> {
            if (args.size() == 0) {
                System.err.println(notEnoughInfo);
            } else
                process(args, getCmds);
        });

        getCmds.put("fps", args -> System.out.println("FPS: " + realFPS));

        getCmds.put("ups", args -> System.out.println("UPS: " + realUPS));

        getCmds.put("cps", args -> System.out.println("CPS: " + realCPS));

        getCmds.put("performance", args -> System.out.println("FPS: " + realFPS + " UPS: " + realUPS + " CPS: " + realCPS));

        getCmds.put("time", args -> System.out.println(getTimestamp()));

        getCmds.put("celobj", args -> {
            if (args.size() >= 2) {
                CelObj celObj = getCelObj(args);
                if (celObj != null) {
                    //First arg should be celObj name and second arg should be the type of info requested
                    switch (args.get(1)) {
                        case "pos":
                            Vec3d pos = celObj.getCelPos();
                            System.out.println("POSITION: X= " + pos.x + " Y= " + pos.y + " Z= " + pos.z);
                            break;
                        case "vel":
                            //Third arg should be magnitude, if present
                            Vec3d vel = celObj.getCelVel();
                            if (args.size() >= 3 && args.get(2).equals("mag"))
                                System.out.println("VELOCITY: V= " + vel.magnitude());
                            else
                                System.out.println("VELOCITY: X= " + vel.x + " Y= " + vel.y + " Z= " + vel.z);
                            break;
                        case "mass":
                            System.out.println("MASS: M= " + celObj.getMass());
                            break;
                        case "radius":
                            System.out.println("RADIUS: R= " + celObj.getRadius());
                            break;
                        default:
                            System.err.println(tokenNotFound);
                    }
                }
            } else
                System.err.println(notEnoughInfo);
        });

        //Set commands

        //Uncenter command

        //etc...

        //TODO: restart program from scratch but neat
    }

    public static void process(String input) {
        List<String> tokens = Arrays.asList(input.toLowerCase().split("\\s+"));
        process(tokens, cmds);
    }

    private static void process(List<String> tokens, Map<String, CommandHandler> commands) {
        String command = tokens.get(0);
        List<String> args = tokens.subList(1, tokens.size());

        CommandHandler handler = commands.get(command);

        if (handler != null) {
            handler.handle(args);
        } else
            System.err.println(tokenNotFound);
    }

    public static CelObj getCelObj(List<String> args) {
        String name = args.get(0);
        CelObj chosenObj = null;
        for (CelObj celObj : physics.getCelObjs()) {
            if (celObj.getName().toLowerCase().equals(name)) {
                chosenObj = celObj;
            }
        }

        if (chosenObj == null)
            System.err.println(tokenNotFound);

        return chosenObj;
    }
}
