package prototype.utils;

public class Timing {
    //Timing related variables
    public static double FPS = 30; //Target frames per second
    public static double UPS = 30; //Updagtes per second
    public static double timeInc = 1; //Time increment
    public static double realTime_to_simTime = (365 * 3600 * 24) / 730; //Real time to simulation time
    public static double FPS_accum = 0;
    public static double UPS_accum = 0;
    public static double CPS_accum = 0;
    public static int realFPS;
    public static int realUPS;
    public static int realCPS;
    public static double recordPeriod = (365 * 3600 * 24) / 365; //In simulation seconds
    public static double TIME;

    public static String getTimestamp() {
        int days = (int) (TIME / (24 * 3600));
        int hours = (int) ((TIME - (days * 24 * 3600)) / 3600);
        int minutes = (int) ((TIME - (days * 24 * 3600) - (hours * 3600)) / 60);
        double seconds = TIME - (days * 24 * 3600) - (hours * 3600) - (minutes * 60);
        return "Day: " + days + " | Hr: " + hours + " | Min: " + minutes + " | Sec: " + seconds;
    }
}
