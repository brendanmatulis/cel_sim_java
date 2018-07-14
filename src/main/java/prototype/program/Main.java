package prototype.program;

import prototype.engine.TUI.Commands;
import prototype.engine.graphics.Camera;
import prototype.engine.graphics.Renderer;
import prototype.engine.graphics.Window;
import prototype.engine.graphics.shaders.Shader;
import prototype.engine.graphics.shaders.ShaderProgram;
import prototype.engine.physics.EulerMethod;
import prototype.engine.physics.IPhysics;
import prototype.math.Matrix4f;
import prototype.math.Vec3d;
import prototype.math.Vec3f;
import prototype.objects.CelestialMechanics.CelObj;
import prototype.objects.CelestialMechanics.Constellation;
import prototype.objects.RenderObject;
import prototype.engine.input.FPSMouse;
import prototype.utils.ConsoleHandler;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static prototype.engine.physics.EulerMethod.worldunit_per_meters;
import static prototype.math.GetMatrix.*;
import static prototype.math.Operations.multiply;
import static prototype.math.Operations.negate;
import static prototype.objects.CelestialMechanics.Constellation.constellations;
import static prototype.objects.CelestialMechanics.Constellation.setConstellationVisibility;
import static prototype.objects.CelestialMechanics.GenerateStars.genStars_HYG;
import static prototype.utils.Timing.*;

public class Main {
    //Instances
    public static IPhysics physics;
    private static Window window;
    public static Camera camera;
    private static Renderer renderer;
    private static FPSMouse mouse;

    //Render objects
    private static RenderObject sky = null;
    private static ArrayList<RenderObject> renderObjs;

    public static float scale = 1;

    public static CelObj centerCelObj = null;

    //Shaders
    private static ShaderProgram skyShader;
    private static ShaderProgram objShader;

    //Matrices
    private static Matrix4f proj_mat;
    private static Matrix4f ortho_mat;
    private static Matrix4f vw_rot_mat;
    private static Matrix4f vw_mat;

    //Movement and input
    private static float mouse_sensitivity = 0.3f;
    private static Vec3f offsetPos = new Vec3f();
    private static float offsetYaw, offsetPitch;
    //Speed of strafe (x), up/down (y), and forward/backward (z)
    //in world units per second
    public static float speed = 200f;

    //Switches
    public static boolean run = true;
    public static boolean updatePhysics = false;
    public static boolean render = true;
    public static boolean showTrails = true;
    public static boolean showConstellations = false;

    private static void init() throws Exception {
        float aspectRatio = 16f/9f;
        window = new Window("Prototype 0.5", 1200, aspectRatio, false);
        window.init();

        glPointSize(1.3f);

        camera = new Camera(new Vec3f(0, 0, 3), new Vec3f(0, 0, 0));
        mouse = new FPSMouse(window, mouse_sensitivity);
        mouse.init();
        mouse.setDisableMouseCallback();

        renderer = new Renderer();

        physics = new EulerMethod();
        physics.init();

        renderObjs = new ArrayList<>();

        //Console listener
        Thread consoleThread = new Thread(new ConsoleHandler());
        consoleThread.start();

        //Generate shaders
        Shader color_vert3D = new Shader("/shaders/color_vert3D.glsl", GL_VERTEX_SHADER);
        Shader color_frag3D = new Shader("/shaders/color_frag3D.glsl", GL_FRAGMENT_SHADER);
        objShader = new ShaderProgram("color_shader3D");
        objShader.attach(color_vert3D);
        objShader.attach(color_frag3D);
        objShader.link();

        Shader sky_vert = new Shader("/shaders/sky_vert.glsl", GL_VERTEX_SHADER);
        Shader sky_frag = new Shader("/shaders/sky_frag.glsl", GL_FRAGMENT_SHADER);
        skyShader = new ShaderProgram("sky_shader");
        skyShader.attach(sky_vert);
        skyShader.attach(sky_frag);
        skyShader.link();

        //Initialize projection matrices
        float FOV = 50f;
        float near = 0.1f;
        float far = (float) (1E13 * worldunit_per_meters);
        proj_mat = getPerspective(near, far, window.getAspectRatio(), FOV);

        //Initialize view matrices
        vw_rot_mat = getViewRotation(camera);
        vw_mat = getView(vw_rot_mat, camera);

        //Generate sky
        float sky_radius = far / 10;
        Vec3f star_color = new Vec3f(1, 1, 1);
        sky = genStars_HYG(star_color, "/data/hygdata_v3.csv", sky_radius);
        Constellation.init();
        setConstellationVisibility(true);

        //Initialize commands
        Commands.init();
    }

    private static void input() {
        //Reset offsets to zero
        offsetPos.zero();
        offsetPitch = 0;
        offsetYaw = 0;

        //z-movement
        if (window.isKeyPressed(GLFW_KEY_W))
            offsetPos.z--;
        else if (window.isKeyPressed(GLFW_KEY_S))
            offsetPos.z++;
        //x-movement
        if (window.isKeyPressed(GLFW_KEY_A))
            offsetPos.x--;
        else if (window.isKeyPressed(GLFW_KEY_D))
            offsetPos.x++;
        //y-movement
        if (window.isKeyPressed(GLFW_KEY_X))
            offsetPos.y++;
        else if (window.isKeyPressed(GLFW_KEY_C))
            offsetPos.y--;

        //Enable GLFW and OPENGL features
        if (window.isKeyPressed(GLFW_KEY_O)) {
            glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
        } else if (window.isKeyPressed(GLFW_KEY_P)) {
            glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
        }

        if (window.isKeyPressed(GLFW_KEY_1))
            updatePhysics = true;
        else if (window.isKeyPressed(GLFW_KEY_2))
            updatePhysics = false;
    }

    private static void update(float interval) {
        //Update mouse offset
        mouse.update();

        //Calculate offsets based on length of interval,
        //direction (i.e. -1 or +1), and speed
        //
        //e.g. ([change]/[time]) * [time] * [-1, 0, or +1]
        offsetPos = multiply(speed * interval, offsetPos);

        //offsetYaw is multiplied by -1f, because yaw increases counter-clockwise from
        //the negative z-axis, meaning it is in opposite direction of mouse's xpos
        offsetYaw = -mouse.getOffsetX();
        //offsetPitch is multiplied by -1f, because the mouse's ypos increases downwards
        offsetPitch = -mouse.getOffsetY();

        //Apply changes
        camera.moveLookAt(offsetYaw, offsetPitch);
        camera.movePosition(offsetPos);
    }

    private static void cleanup() {
        //Cleanup all render objects and shader programs
        sky.getMesh().cleanup();

        for (RenderObject obj : renderObjs) {
            obj.getMesh().cleanup();
        }

        for (RenderObject obj : constellations) {
            obj.getMesh().cleanup();
        }

        skyShader.cleanup();
        objShader.cleanup();
    }

    public static void main(String[] args) throws Exception {
        init();

        double lastTime = System.nanoTime() / 1E9;
        double currentTime;
        double deltaTime;

        double lastSecond = lastTime;

        double UPS_interval = 1 / UPS;
        double FPS_interval = 1 / FPS;
        double CPS_interval;

        int frames = 0;
        int updates = 0;
        int calcs = 0;

        //Indicates whether the loop performed the task
        //during a run through (i.e. whether to subtract from accumulator)
        //Done in this way so that the simulation is not bogged down by calculations
        boolean updated;
        boolean rendered;
        boolean calculated;

        while (run && !window.shouldClose()) {

            CPS_interval = timeInc / realTime_to_simTime;

            //Reset booleans
            updated = false;
            rendered = false;
            calculated = false;

            currentTime = System.nanoTime() / 1E9;
            deltaTime = currentTime - lastTime;
            lastTime = currentTime;

            FPS_accum += deltaTime;
            UPS_accum += deltaTime;
            if (updatePhysics)
                CPS_accum += deltaTime;

            if (FPS_accum >= FPS_interval) {
                if (updatePhysics) {
                    physics.renderUpdate((float) FPS_interval);
                }

                renderer.clear();

                if (render) {
                    //Update view matrices
                    //Note: must be in render, not update, because physics affects
                    //camera. E.g. if camera is locked. E.g. if vw_mat was
                    //updated in updates but render was called more often, the locked planet
                    //would wobble as opposed to remaining stationary
                    vw_rot_mat = getViewRotation(camera);
                    vw_mat = getView(vw_rot_mat, camera);

                    if (showTrails) {
                        ArrayList<RenderObject> trails = new ArrayList<>();
                        for (CelObj celObj : physics.getCelObjs()) {
                            RenderObject trail = celObj.getTrail();
                            if (trail != null)
                                trails.add(trail);
                        }

                        //TODO: othographic projection for trails? i.e. like Ptolemy model
                        //TODO:... which does not account for distances??
                        renderer.render(trails, vw_mat, proj_mat, objShader);

                    }

                    renderObjs = physics.getRenderObjs(scale);
                    renderer.render(sky, vw_rot_mat, proj_mat, skyShader);
                    renderer.render(renderObjs, vw_mat, proj_mat, objShader);
                    if (showConstellations)
                        renderer.render(constellations, vw_rot_mat, proj_mat, skyShader);

                    frames++;
                }

                window.update();
                rendered = true;
            }

            if (UPS_accum >= UPS_interval) {
                input();
                update((float) UPS_interval);
                updated = true;
                updates++;
            }

            if (updatePhysics && CPS_accum >= CPS_interval) {
                //Update positions (calculate and nothing else)
                physics.update(timeInc);

                if (centerCelObj != null) {
                    Vec3d correctionTranslation = negate(centerCelObj.getCelPos());
                    for (CelObj celObj : physics.getCelObjs()) {
                        celObj.moveCelPos(correctionTranslation);
                    }
                }

                TIME = physics.getSimulationTime();
                if (TIME % recordPeriod == 0) {
                    physics.record();
                }

                calculated = true;
                calcs++;
            }

            for (int i = 0; i < ConsoleHandler.consoleInput.size(); i++) {
                Commands.process(ConsoleHandler.consoleInput.remove());
            }

            if (rendered)
                FPS_accum -= FPS_interval;
            if (updated)
                UPS_accum -= UPS_interval;
            if (calculated)
                CPS_accum -= CPS_interval;

            //Print FPS, UPS, CPS
            if (currentTime - lastSecond >= 1) {
                lastSecond = currentTime;
                realFPS = frames;
                realUPS = updates;
                realCPS = calcs;
                frames = 0;
                updates = 0;
                calcs = 0;
            }
        }

        window.closeWindow();
        cleanup();
        System.exit(0);
    }
}
