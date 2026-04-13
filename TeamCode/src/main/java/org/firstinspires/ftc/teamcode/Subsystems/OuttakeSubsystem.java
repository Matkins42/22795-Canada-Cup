package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class OuttakeSubsystem {

    public DcMotorEx flywheel;

    private Servo hood;


    public OuttakeSubsystem(HardwareMap hardwareMap) {

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        hood = hardwareMap.get(Servo.class, "hood");
        flywheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setOuttakeVelocity(){
        flywheel.setVelocity(RobotConstants.OUTTAKE_Velocity);
    }

    public double getOuttakeVelocity(){
        return flywheel.getVelocity();
    }
    public void startFlywheel(){
        flywheel.setPower(RobotConstants.OUTTAKE_POWER);
    }

    public void stopFlywheel(){
        flywheel.setPower(0);
    }

    public void setHood(double hoodPosition){
       hood.setPosition(hoodPosition);
    }

    public void setHoodAngle(double angle){
        hood.setPosition(Math.max(0, Math.min(RobotConstants.EXTENDED_SERVO_POSITION,(angle - RobotConstants.BOTTOM_ANGLE) / ((RobotConstants.TOP_ANGLE - RobotConstants.BOTTOM_ANGLE) / RobotConstants.EXTENDED_SERVO_POSITION))));
    }

}
