package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class IntakeSubsystem {
    private DcMotor frontIntake;
    private DcMotor backIntake;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        frontIntake = hardwareMap. get(DcMotor.class, "intakeFront");
        backIntake = hardwareMap. get(DcMotor.class, "intakeBack");

        frontIntake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void stop(){
        backIntake.setPower(0);
        frontIntake.setPower(0);
    }
    public void expel(){
        frontIntake.setPower(RobotConstants.BACKWARDS_INTAKE_POWER);
        backIntake.setPower(RobotConstants.BACKWARDS_INTAKE_POWER);
    }
    public void collect(){
        frontIntake.setPower(RobotConstants.FORWARDS_INTAKE_POWER);
        backIntake.setPower(RobotConstants.BACKWARDS_INTAKE_POWER);
    }
    public void shoot(){
        frontIntake.setPower(RobotConstants.FORWARDS_INTAKE_POWER);
        backIntake.setPower(RobotConstants.FORWARDS_INTAKE_POWER);
    }
}

