package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name="Vision_Auto_Red_Left_2", group="Training")
//@Disabled
public class Vision_Auto_Red_Left_2 extends LinearOpMode {

    /* Declare OpMode members. */
    MaristBaseRobot2021_Quad robot   = new MaristBaseRobot2021_Quad();   
    private ElapsedTime runtime = new ElapsedTime();
    
    // Variables to control Speed
    double velocity = 0.5; // Default velocity
    
    //Camera fields
    WebcamName webcamName;
    OpenCvCamera camera;
    
    //Pipeline
    Pipeline_Target_Detect myPipeline;
    
    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        
        //Test of Webcam
        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName);
        myPipeline = new Pipeline_Target_Detect();
        
       
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();
        
                //Asynchronously Open Camera (In new Thread)
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
           public void onOpened() {
               //Start streaming from camera
               camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
               
                //Init Pipeline
                camera.setPipeline(myPipeline);
           } 
           @Override
           public void onError(int errorCode) {
               //Called if not working
           }
        });
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        double xPos = -1;
        int zone = 2;
        
        runtime.reset();
        while(opModeIsActive() && (runtime.seconds() < 2)) {
            xPos = myPipeline.getXPos();
            zone = myPipeline.getZone();
            telemetry.addData("XPos", xPos);
            telemetry.addData("Zone", zone);
            telemetry.update();
        }
        
        // Autonomous Finished]
        double dist = Constants.chooseRedX(zone);
        double deg = Constants.chooseDeg(zone);
        telemetry.addData("XPos", xPos);
        telemetry.addData("Zone", zone);
        telemetry.update();
        
        delay(1);
        //Stop the Camera
        camera.stopStreaming();
        camera.closeCameraDevice();
        //sleep(1000);

        robot.moveDistance(-5, 0.5);
        delay(0.3);
        robot.strafeInches(0.5, 7, 5);
        delay(0.2);
        robot.turnAngle(220, 0.5);
        delay(0.3);
        robot.moveDistance(dist, 0.5);
        delay(0.3);
        //slide decision
        robot.leftArmMotorDeg(0.5, -deg, 5);
        delay(0.5);
        robot.leftHand.setPosition(0.4);
        delay(1.5);
        robot.moveDistance(-dist, 1.0);
        delay(0.2);
        robot.leftHand.setPosition(0);
        delay(0.2);
        robot.turnAngle(80, 0.5);
        delay(0.3);
        robot.leftArmMotorDeg(0.5, deg, 5);
        delay(0.2);
        //spin thing
        robot.moveDistance(-15, 1.0);
        delay(0.2);
        robot.turnAngle(45, 0.5);
        delay(0.2);
        robot.strafeInches(0.5, -20, 5);
        delay(0.2);
        robot.spinner.setPower(-0.5);
        delay(2.5);
        robot.spinner.setPower(0);
        delay(0.2);
        //park
        robot.strafeInches(0.5, 2, 5);
        delay(0.2);
        robot.turnAngle(-45, 0.6);
        delay(0.2);
        robot.moveDistance(-10, 0.8);
        delay(0.2);
        robot.strafeInches(0.5, 12, 5);
        delay(0.2);
        robot.moveDistance(-90, 0.9);
        
        // Autonomous Finished
        telemetry.addData("Path", "Complete");
        telemetry.update();
        //sleep(1000);
        
    }

    // Functions for REACH 2019 based on Python Turtles
    public void forward(double inches)
    {
        robot.driveStraightInches(velocity, inches, 10);
    }
    
    public void right(double degrees)
    {
        robot.pointTurnDegrees(velocity, degrees, 10);
    }
    
    public void left(double degrees)
    {
        degrees = degrees * -1;
        robot.pointTurnDegrees(velocity, degrees, 10);
    }
    
    public void speed(int speed)
    {
        double newSpeed = (double)speed / 10.0;
        velocity = newSpeed;
    }
    
    // Sample Delay Code
    public void delay(double t) { // Imitates the Arduino delay function
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < t)) {
            // telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            // telemetry.update();
        }
    }
}
