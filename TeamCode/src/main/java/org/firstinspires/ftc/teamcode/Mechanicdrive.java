package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "driving", group= "Linear Opmode")
public class Mechanicdrive extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private double forwards;
    private double rotation;
    private double strafe;;
    @Override
    public void runOpMode() throws InterruptedException {

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);


        frontLeft = hardwareMap. get(DcMotor.class,"front Left");
        frontRight = hardwareMap. get(DcMotor.class,"front right");
        backLeft = hardwareMap. get(DcMotor.class,"back Left");
        backRight = hardwareMap. get(DcMotor.class,"back Right");
        waitForStart();

        while(opModeIsActive()){
            //Get controller input
            forwards = gamepad1.left_stick_y;
            rotation = gamepad1.right_stick_x;
            strafe = gamepad1. left_stick_x;
            frontLeft.setPower(forwards + rotation + strafe);
            frontRight. setPower(forwards - rotation - strafe);
            backLeft. setPower(forwards+ rotation - strafe);
            backRight. setPower(forwards - rotation + strafe);



        }
    }
}
