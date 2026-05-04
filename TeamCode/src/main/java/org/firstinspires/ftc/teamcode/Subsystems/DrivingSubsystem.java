package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DrivingSubsystem {

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;
    private double power = 0.5;

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
        leftFront.setPower((forwards - rotation - strafe) * power);
        rightFront.setPower((forwards + rotation + strafe) * power);
        leftBack.setPower((forwards - rotation + strafe) * power);
        rightBack.setPower((forwards + rotation - strafe) * power);
    }

    public void setPower(double value){
        power = value;
    }
}
