package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class Pipeline_Target_Detect extends OpenCvPipeline {

    // todo: write your code here
    //this is a pipeline for detecting Target and OpenCV
    
    Mat templateMat = new Mat(64, 64, CvType.CV_8UC1);
    //mat input represents image coming from the camera
    //overrides function in OpenCvPipeline class
    private double xPos = -1;
    private int zone = 2;
    
    public Pipeline_Target_Detect() {
        buildTemplate();
    }
    
    //been refactored into a constructor
    private void buildTemplate() {
        //Define the Template (Create with Loops and fill white squares)
        int [][] template = new int[64][64];
        
        for(int r = 0; r< 32; r++) {
            for(int c = 32; c < 64; c++) {
                template[r][c] = 255;
            }
        }
        
        for(int r = 32; r < 64; r++) {
            for(int c = 0; c < 32; c++) {
                template[r][c] = 255;
            }
        }
        
        templateMat = new Mat(64, 64, CvType.CV_8UC1);
        //last parameter means pixel will hold 8-bits instead of integers 
        //since integers take a lot of space
        for(int r = 0; r < 6; r++) {
            for(int c = 0; c < 64; c++) {
                templateMat.put(r, c, template[r][c]);
            }
        }
    }
    
    @Override 
    //overrides default processFrame
    //in order to determine the Xpos and correct zone
    //of the capstone and therefore template
    public Mat processFrame(Mat input) {
        
        //Convert input to black and white
        Mat gray = new Mat();
        Imgproc.cvtColor(input, gray, Imgproc.COLOR_RGB2GRAY);
        
        //Template Match - where iden happens
        Mat heatMap = new Mat();
        int machMethod = Imgproc.TM_CCOEFF;
        Imgproc.matchTemplate(gray, templateMat, heatMap, machMethod);
        
        //Get coordinates of best match
        MinMaxLocResult mmr = Core.minMaxLoc(heatMap);
        Point matchLoc = mmr.maxLoc;
        
        //Draw Rectangle at the best match
        Imgproc.rectangle(input, matchLoc, new Point(matchLoc.x + templateMat.cols(), 
            matchLoc.y + templateMat.rows()), new Scalar(0, 255, 0));
        
        //Write to xPose
        xPos = (double) matchLoc.x;
        //for blue 3 is top, 2 is mid, 1 is bottom
        if(xPos > 175) {
            zone = 3;
        }
        else if(xPos < 75) {
            zone = 1;
        }
        return input;
    }

    
    public double getXPos() {
        return xPos;
    }
    
    public int getZone() {
        return zone;
    } 
    
   
}