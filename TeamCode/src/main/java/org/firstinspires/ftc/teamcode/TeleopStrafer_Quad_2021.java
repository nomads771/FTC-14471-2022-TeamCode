/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Modified by michaudc 2017
Additional modifications by michaudc 2021
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="MaristBot2021: Teleop Strafer 2021", group="Training")
//@Disabled
public class TeleopStrafer_Quad_2021 extends OpMode {

    /* Declare OpMode members. */
    MaristBaseRobot2021_Quad robot   = new MaristBaseRobot2021_Quad(); // use the class created to define a Robot's hardware
                                                         // could also use HardwarePushbotMatrix class.
    double          clawOffset  = 0.5;                  // Servo mid position
    final double    CLAW_SPEED  = 0.02 ;                 // sets rate to move servo
    
    int slidePos = 0;
//    int liftPos = 0;

    boolean toggle = false;
    boolean toggleLast = false;
    boolean intakeToggle = false;
    boolean intakeToggleLast = false;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Robot Ready");    //

        // Set to Run without Encoder for Tele Operated
        robot.leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.leftArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slidePos = robot.leftArm.getCurrentPosition();
        //liftPos = robot.intake.getCurrentPosition();
        
        //robot.rightArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //robot.rightArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //liftPos = robot.rightArm.getCurrentPosition();
        

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        //robot.leftHand.setPosition(0.9);
        //robot.capping.setPosition(1);
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        //TO DO:
        /**
         * fix capping
         * add some code for preset slidePos
         */
        double leftX;
        double leftY;
        double rightX;

        // Strafer Mode
        leftX = gamepad1.left_stick_x;
        leftY = gamepad1.left_stick_y;
        rightX = gamepad1.right_stick_x;
        
        double leftRearPower = leftY + leftX + rightX;
        double leftFrontPower = leftY - leftX + rightX;
        
        //double rightRearPower = 0;
        //double rightFrontPower = 0;
        
        double rightRearPower = leftY - leftX - rightX;
        double rightFrontPower = leftY + leftX - rightX;
        


        if(gamepad1.right_bumper) {
            toggle = !toggle;
        }
        //slow cap code
        if(gamepad1.left_bumper) {
            if(toggle){
                robot.leftFront.setPower((leftFrontPower-2*rightX) * 0.3);
                robot.leftRear.setPower((leftRearPower-2*rightX) * 0.3);
                robot.rightFront.setPower((rightFrontPower+2*rightX) * 0.3);
                robot.rightRear.setPower((rightRearPower+2*rightX) * 0.3);
            }
            else {
                robot.leftFront.setPower(-leftFrontPower * 0.3);
                robot.leftRear.setPower(-leftRearPower * 0.3);
                robot.rightFront.setPower(-rightFrontPower * 0.3);
                robot.rightRear.setPower(-rightRearPower * 0.3);
            }
        }
        else if(toggle) {
            robot.leftFront.setPower(leftFrontPower-2*rightX);
            robot.leftRear.setPower(leftRearPower-2*rightX);
            robot.rightFront.setPower(rightFrontPower+2*rightX);
            robot.rightRear.setPower(rightRearPower+2*rightX);
        }
        else {
            robot.leftFront.setPower(-leftFrontPower);
            robot.leftRear.setPower(-leftRearPower);
            robot.rightFront.setPower(-rightFrontPower);
            robot.rightRear.setPower(-rightRearPower);
        }
        
        // Alternative Method:  Single Paddle on right  (Commented out)
        //left = gamepad1.right_stick_y + gamepad1.right_stick_x;
        //right = gamepad1.right_stick_y - gamepad1.right_stick_x;
        //robot.leftMotor.setPower(left);
        //robot.rightMotor.setPower(right);

        // Use gamepad left & right Bumpers to open and close the claw
        // if (gamepad1.right_bumper)
        //     robot.rightHand.setPosition(0.0);
        // if (gamepad1.left_bumper)
        //     robot.rightHand.setPosition(0.2);
            
        if (gamepad2.right_trigger > 0.5)
            clawOffset+= 0.002;
        if (gamepad2.left_trigger > 0.5)
            clawOffset-= 0.002;
//        if (gamepad2.right_bumper)
//            robot.capping.setPosition(0.6);
//        if (gamepad2.left_bumper)
//            robot.capping.setPosition(1);

        // Move both servos to new position.  Assume servos are mirror image of each other.
        clawOffset = Range.clip(clawOffset, 0.0, 0.5);
        robot.capping.setPosition(robot.MID_SERVO + clawOffset);
//        robot.leftHand.setPosition(robot.MID_SERVO + clawOffset);
//        robot.rightHand.setPosition(robot.MID_SERVO - clawOffset);

        // Use gamepad buttons to move the arm up (Y) and down (A)
        
        // if (gamepad2.a)
        //     robot.intake.setPower(0.8);
        //     //liftPos += 2;
        // else if (gamepad2.y)
        //     robot.intake.setPower(-0.8);
        //     //liftPos -= 2;
        // else    
        //     robot.intake.setPower(0.0);
        
        // robot.intake.setTargetPosition(liftPos);
        // robot.intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // robot.intake.setPower(0.7);
        

        // Control Arm with Right and Left Triggers
        slidePos -= gamepad2.left_stick_y * 8;

        if(gamepad2.y) {
            slidePos = 800;
        }
        if(gamepad2.a) {
            slidePos = 200;
        }
        if(gamepad1.y){
            slidePos += 10;
        }
        if(gamepad1.a){
            slidePos -= 10;
        }
        
        robot.leftArm.setTargetPosition(slidePos);
        robot.leftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if(slidePos <= 0) {
            robot.leftArm.setPower(0);
        }
        else {
            robot.leftArm.setPower(0.8);
        }
        //add button

//        double armMotorPower = gamepad2.left_trigger - gamepad2.right_trigger;
//        // Limit Power to -0.4 to 0.4
//        if (armMotorPower > 0.5) {
//            armMotorPower = 0.5;
//        }
//
//        if (armMotorPower < -0.5) {
//            armMotorPower = -0.5;
//        }
//        robot.leftArm.setPower(armMotorPower);
        
        // double intakeMotorPower = gamepad1.left_trigger - gamepad1.right_trigger;
        // // Limit Power to -0.4 to 0.4
        // if (intakeMotorPower > 0.4) {
        //     intakeMotorPower = 0.4;
        //     //slidePos += gamepad2.right_trigger * 8;
        // }

        // if (intakeMotorPower < -0.4) {
        //     intakeMotorPower = -0.4;
        //     //slidePos -= gamepad2.left_trigger * 3;
        // }
        
        
        // if(gamepad2.a) {
        //     armPos = 894;
        // }
        
        // if(gamepad2.y) {
        //     armPos = 1420;
        // }

        //robot.leftArm.setPower(armMotorPower);
        //robot.leftArm.setTargetPosition(slidePos);
        // robot.leftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // robot.leftArm.setPower(armMotorPower);
        // robot.intake.setPower(intakeMotorPower);
        if (gamepad1.left_stick_button & !intakeToggleLast) {
            intakeToggleLast = intakeToggle;
            intakeToggle = !intakeToggle;
        }
        else intakeToggleLast = false;

        if(gamepad2.dpad_up||gamepad1.dpad_up)
            robot.intake.setPower(1.0);
        else if(gamepad2.dpad_down||gamepad1.dpad_down||intakeToggle)
            robot.intake.setPower(-1.0);
        else robot.intake.setPower(0);

            
        // if(gamepad2.dpad_up) 
        //     robot.intake.setPower(0.8);
        // else if(gamepad2.dpad_down) 
        //     robot.intake.setPower(-0.8);
        // else 
        //     robot.intake.setPower(0);
        
        // Spinner 
        if (gamepad1.x)
            robot.spinner.setPower(0.4);
        else if (gamepad1.b)
            robot.spinner.setPower(-0.4);
        else
            robot.spinner.setPower(0);
            
        // Claw presets
        if(gamepad2.right_stick_y == 1 || gamepad1.left_trigger > 0)
            robot.leftHand.setPosition(0.0);
            //clawOffset = 0.1;
        if (gamepad2.right_stick_y == -1 || gamepad1.right_trigger > 0)
            robot.leftHand.setPosition(0.5);
            //clawOffset = -0.3;
        
        if (gamepad2.left_bumper || gamepad1.dpad_left)
            robot.leftHand.setPosition(0.1);
        if (gamepad2.right_bumper)
            robot.leftHand.setPosition(0.4);
        // Send telemetry message to signify robot running;
        telemetry.addData("slidePos",  slidePos);
        telemetry.addData("clawOffset",  clawOffset);
        telemetry.addData("toggle", intakeToggle);
        telemetry.addData("intakeToggleLAst", intakeToggleLast);
//        telemetry.addData("clawOffset", clawOffset);
        telemetry.update();

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
