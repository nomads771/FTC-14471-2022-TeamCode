package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
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
    private Zone zone;
    private static double PERCENT_COLOR_THRESHOLD = 0.8;
    Mat hsv = new Mat();

    private static final Rect LEFT_RECT = new Rect(
            new Point(0, 60), new Point(90, 220)
    );
    private static final Rect MID_RECT = new Rect(
            new Point(120, 60), new Point(120, 220)
    );
    private static final Rect RIGHT_RECT = new Rect(
            new Point(280, 60), new Point(320, 220)
    );

    Scalar lowHSV;  //will = (85, 150, 50);
    Scalar highHSV; //will = (95,250,255);

    /* Pre - array will not be null and will contain 3 elements
     * Constructor:
     * Takes two int arrays representing the upper and lower bounds of color we need to match
     * Converts them into scalars
     */
    public PipeLine_Color_Detect(int[] lb, int[] hb) {
        lowHSV = new Scalar(lb[0], lb[1], lb[2]);
        highHSV = new Scalar(hb[0], hb[1], hb[2]);
    }

    public PipeLine_Color_Detect() {
        lowHSV  = new Scalar(85, 150, 50);
        highHSV = new Scalar(95,250,255);
    }
    @Override
    public Mat processFrame(Mat input) {

        //Convert input frame from RBG to HSV*
        //easy opencv delivers RBG while normal opencv does BGR - account for difference
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

        //GaussianBlur to make it simpler
        //Imgproc.GaussianBlur(hsv,hsv,new Size(5,5),0); //what is the size of the frame we get?

        //Masking - Threshold HSV image to get the targetColor
        Core.inRange(hsv, lowHSV, highHSV, hsv);

//        Mat result = new Mat(input.rows(), input.cols(), CvType.CV_8UC1);
//        Core.bitwise_and(input, hsv, result);

        //Get coordinates of best match
        Mat left = hsv.submat(LEFT_RECT);
        Mat mid = hsv.submat(MID_RECT);
        Mat right = hsv.submat(RIGHT_RECT);

        double leftSum = Core.sumElems(left).val[0] / LEFT_RECT.area() /255;
        double midSum = Core.sumElems(mid).val[0] / MID_RECT.area() /255;
        double rightSum = Core.sumElems(right).val[0] / RIGHT_RECT.area() /255;

        left.release();
        mid.release();
        right.release();

        boolean zone_ONE = leftSum > PERCENT_COLOR_THRESHOLD;
        boolean zone_TWO = midSum > PERCENT_COLOR_THRESHOLD;
        boolean zone_THREE = rightSum > PERCENT_COLOR_THRESHOLD;

        if(zone_ONE) {
            zone = Zone.ONE;
        }
        else if(zone_TWO) {
            zone = Zone.TWO;
        }
        else if(zone_THREE) {
            zone = Zone.THREE;
        }
        else {
            zone = Zone.THREE;
        }
        Imgproc.cvtColor(hsv, hsv, Imgproc.COLOR_GRAY2RGB);

        Scalar elementColor = new Scalar(0, 255, 0);
        Scalar notElement = new Scalar(255, 0, 0);
        Imgproc.rectangle(input, LEFT_RECT, zone == Zone.ONE ? elementColor : notElement);
        Imgproc.rectangle(input, MID_RECT, zone == Zone.TWO ? notElement : elementColor);
        Imgproc.rectangle(input, RIGHT_RECT, zone == Zone.TWO ? notElement : elementColor);

        return input;
    }
    // Accessor Function
    public double getXPos() {
        return xPos;
    }

    public int getZone() {
        if(zone == Zone.ONE) {
            return 1;
        }
        else if(zone == Zone.TWO) {
            return 2;
        }
        else {
            return 3;
        }
    }

}



