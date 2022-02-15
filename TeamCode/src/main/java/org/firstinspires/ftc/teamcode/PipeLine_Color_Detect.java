package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class PipeLine_Color_Detect extends OpenCvPipeline {

    /* todo:
     *-figure out how to map matches onto xPos
     *-check if code is correct
     */
    // This is a pipeline for detecting color targets

    //Field for x Position for Found Target
    private double xPos = -1; // If target not found, -1
    private int zone = 2;
    private static final Rect LEFT_RECT = new Rect(
            new Point(465, 200), new Point(715, 650)
    );
    private static final Rect MID_RECT = new Rect(
            new Point(920, 200), new Point(1170, 650)
    );
    private static final Rect RIGHT_RECT = new Rect(
            new Point(1475, 200), new Point(1725, 650)
    );
    Scalar lowColor;  //will = (85, 150, 50);
    Scalar highColor; //will = (95,250,255);

    /* Pre - array will not be null and will contain 3 elements
     * Constructor:
     * Takes two int arrays representing the upper and lower bounds of color we need to match
     * Converts them into scalars
     */
    public PipeLine_Color_Detect(int[] lb, int[] hb) {
        lowColor = new Scalar(lb[0], lb[1], lb[2]);
        highColor = new Scalar(hb[0], hb[1], hb[2]);
    }
    @Override
    public Mat processFrame(Mat input) {

        //Convert input frame from RBG to HSV*
        //easy opencv delivers RBG while normal opencv does BGR - account for difference
        Mat hsv = new Mat();
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_BGR2HSV);

        //GaussianBlur to make it simpler
        //Imgproc.GaussianBlur(hsv,hsv,new Size(5,5),0); //what is the size of the frame we get?

        //Masking - Threshold HSV image to get the targetColor
        Mat hold = new Mat();
        Core.inRange(hsv, lowColor, highColor, hsv); //here is where i will need help

        /*Bitwise-AND mask and original image -- look this up
         *bitwise_and parameters
         *src1 - First source array or a scalar.
         *src2 - Second source array or a scalar.
         *dst - Destination array that has the same size and type as the input array(s).
         *mask - (not used) - Optional operation mask, 8-bit single channel array, that specifies elements of the destination array to be changed.
         */
        Mat result = new Mat(input.rows(), input.cols(), CvType.CV_8UC1);
        Core.bitwise_and(input, hsv, result);

        //Old Code for template matching

        //Get coordinates of best match
//        Mat left = result.submat(LEFT_RECT);
//        Mat mid = result.submat(MID_RECT);
//        Mat right = result.submat(RIGHT_RECT);
//
//        double leftSum = Core.sumElems(left).val[0];
//        double midSum = Core.sumElems(mid).val[0];
//        double rightSum = Core.sumElems(right).val[0];

        //ToDo
        /*
        add switch case with enum zone
         */

        return result;
    }
    // Accessor Function
    public double getXPos() {
        return xPos;
    }

    public int getZone() {
        return zone;
    }

}



