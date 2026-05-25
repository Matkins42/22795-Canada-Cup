package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class DrivingSubsystem {

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;

    //Must be initialised after roadrunner subsystem initialisation
    public DrivingSubsystem(HardwareMap hardwareMap) {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        leftFront.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
    }

    public void drive(double forwards, double rotation, double strafe){
        leftFront.setPower((forwards - rotation - strafe) * RobotConstants.DRIVING_SPEED);
        rightFront.setPower((forwards + rotation + strafe) * RobotConstants.DRIVING_SPEED);
        leftBack.setPower((forwards - rotation + strafe) * RobotConstants.DRIVING_SPEED);
        rightBack.setPower((forwards + rotation - strafe) * RobotConstants.DRIVING_SPEED);
    }

    public void normalisedDrive(double forwards, double rotation, double strafe){
        double leftFrontPower = ((forwards - rotation - strafe) * RobotConstants.DRIVING_SPEED);
        double rightFrontPower = ((forwards + rotation + strafe) * RobotConstants.DRIVING_SPEED);
        double leftBackPower = ((forwards - rotation + strafe) * RobotConstants.DRIVING_SPEED);
        double rightBackPower = ((forwards + rotation - strafe) * RobotConstants.DRIVING_SPEED);

        double max = Math.max(1, Math.max(Math.abs(leftFrontPower), Math.max(Math.abs(rightFrontPower), Math.max(Math.abs(leftBackPower), Math.abs(rightBackPower)))));

        leftFrontPower /= max;
        rightFrontPower /= max;
        leftBackPower /= max;
        rightBackPower /= max;

        leftFront.setPower(leftFrontPower);
        rightFront.setPower(rightFrontPower);
        leftBack.setPower(leftBackPower);
        rightBack.setPower(rightBackPower);
    }
}
