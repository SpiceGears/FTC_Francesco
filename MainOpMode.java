/*
PORT MAP:

motor_left_drive - 0
motor_right_drive - 1
motor_intake - 2

servo_left_chute - 0
servo_right_chute - 1
*/



package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Basic: Linear OpMode", group="Linear Opmode")

public class MainOpMode extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor intakeMotor = null;
    
    private Servo servoLeftChute = null;
    private Servo servoRightChute = null;
    

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive  = hardwareMap.get(DcMotor.class, "motor_left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "motor_right_drive");
        intakeMotor = hardwareMap.get(DcMotor.class, "motor_intake");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        
        intakeMotor.setDirection(DcMotor.Direction.REVERSE);
        
        servoLeftChute = hardwareMap.get(Servo.class, "servo_left_chute");
        servoRightChute = hardwareMap.get(Servo.class, "servo_right_chute");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        
        double leftPower, rightPower;
        double intakeMotorSpeed = 0;
        
        boolean isChuteOpen = false;
        

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double i = 0;
            // Setup a variable for each drive wheel to save power level for telemetry
            

            double drive = -gamepad1.left_stick_y;
            double turn  = -gamepad1.right_stick_x;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0);
            rightPower   = Range.clip(drive - turn, -1.0, 1.0);
            
            
            if(gamepad1.a) {
                isChuteOpen = false;
                servoLeftChute.setPosition(0.20);
                servoRightChute.setPosition(0.8);
            }else if (gamepad1.y){
                isChuteOpen = true;
                servoLeftChute.setPosition(0.6);
                servoRightChute.setPosition(0.4);
            }
            
             if(gamepad1.right_bumper) {
                intakeMotorSpeed = 1;
             }else if (gamepad1.left_bumper){
                intakeMotorSpeed = 0;
             }
            //  else{
            //     intakeMotorSpeed = 0;
            //  }


            // Tank Mode uses one stick to control each wheel. 
            // - This requires no math, but it is hard to drive forward slowly and keep straight.

            // Send calculated power to wheels
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);
            
            intakeMotor.setPower(intakeMotorSpeed);
            

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Chute", "isChuteOpen (%b)", isChuteOpen);
            telemetry.addData("Motors", "left (%.2f), right (%.2f), intake (%.2f)", leftPower, rightPower, intakeMotorSpeed);
            telemetry.addData("Turning","turn (%.2f)",turn);
            telemetry.update();
        }
    }
}