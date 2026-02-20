package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp (name="MecanumDriving", group ="Linear Opmode")
public class mecanum_driving extends LinearOpMode{

  private DcMotor frontLeft;
  private DcMotor frontRight;
  private DcMotor backLeft;
  private DcMotor backRight;

  private double forwards;
    private double rotation;
    private double strafe;


    @Override
    public void runOpMode() throws InterruptedException {

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
       backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);


        waitForStart();

        while(opModeIsActive()){

            // Get controller inputs
            forwards = gamepad1.left_stick_y;
            rotation = gamepad1.right_stick_x;
            strafe= gamepad1.left_stick_x;

            // set motor power based on controler input
            frontLeft.setPower(forwards + rotation + strafe);
            frontRight.setPower(forwards - rotation - strafe);
            backLeft.setPower(forwards + rotation - strafe);
            backRight.setPower(forwards - rotation + strafe);






        }
    }
}
