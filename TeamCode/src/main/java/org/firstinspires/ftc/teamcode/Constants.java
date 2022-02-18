package org.firstinspires.ftc.teamcode;

public class Constants {

    private static double[] xDist = new double[] {9, 6, 6}; //zone 1,2, 3
    private static double[] redXDist = new double[] {10, 8, 6}; //zone 1,2, 3
    private static double[] armDeg = new double[] {160, 500, 800}; //zone 1,2,3
    public static int[] lowCyanBounds = new int[] {106, 187, 50};
    public static int[] highCyanBounds = new int[] {118,225,255};
    public static int test = 0;

    public static double chooseX(int zone) {
        if(zone == 1) return xDist[0];
        else if(zone == 2) return xDist[1];
        else return xDist[2];
    }

    public static double chooseRedX(int zone) {
        if(zone == 1) return redXDist[0];
        else if(zone == 2) return redXDist[1];
        else return redXDist[2];
    }

    public static double chooseDeg(int zone) {
        if(zone == 1) return armDeg[0];
        else if(zone == 2) return armDeg[1];
        else return armDeg[2];
    }
}
