package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class IntakeSubsystem {
    private DcMotor frontIntake;
    private DcMotor backIntake;
    private Servo stopper;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        //This runs when the subsystem is created, put hardware maps here
        frontIntake= hardwareMap. get(DcMotor.class, "Intake");
        backIntake=hardwareMap. get(DcMotor.class, "reverseIntake");
        stopper =hardwareMap. get(Servo.class, "stopIntake");
    }

    //Write any functions of the subsystem

    public void StopperClosed(){
        stopper.setPosition(RobotConstants.CLOSED_INTAKE);
    }
    public void StopperOpen(){
        stopper.setPosition(RobotConstants.OPEN_INTAKE);
    }

    public void spinBackwards(){
        backIntake.setPower(RobotConstants.BACKWARDS_INTAKE_POWER);
        frontIntake.setPower(RobotConstants.BACKWARDS_INTAKE_POWER);
    }
    public void spinForwards(){
        backIntake.setPower(RobotConstants.FORWARDS_INTAKE_POWER);
        frontIntake.setPower(RobotConstants.FORWARDS_INTAKE_POWER);

    }
}

