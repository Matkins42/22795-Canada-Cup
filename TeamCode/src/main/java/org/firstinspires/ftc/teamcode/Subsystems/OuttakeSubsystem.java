package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class OuttakeSubsystem {

    public DcMotorEx flywheel;
    private Servo hood;
    private double targetSpeed = RobotConstants.OUTTAKE_VELOCITY.MIN;
    private boolean firing = false;


    public OuttakeSubsystem(HardwareMap hardwareMap) {

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);
        hood = hardwareMap.get(Servo.class, "hood");
        flywheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void update(){
        if (firing) {
            flywheel.setVelocity(targetSpeed);
        }
    }

    public void setVelocity(double speed){
        targetSpeed = speed;
    }

    public double getVelocity(){
        return flywheel.getVelocity();
    }

    public double getTargetVelocity(){
        return targetSpeed;
    }
    public void startFlywheel(){
        flywheel.setVelocity(targetSpeed);
        firing = true;
    }

    public void stopFlywheel(){
        flywheel.setVelocity(0);
        firing = false;
    }

    public void setHood(double hoodPosition){
       hood.setPosition(hoodPosition);
    }

    public void setHoodAngle(double angle){
        hood.setPosition(Math.max(0, Math.min(RobotConstants.EXTENDED_SERVO_POSITION,(angle - RobotConstants.HOOD_ANGLE.MIN) / ((RobotConstants.HOOD_ANGLE.MAX - RobotConstants.HOOD_ANGLE.MIN) / RobotConstants.EXTENDED_SERVO_POSITION))));
    }

    public boolean reachedSpeed(){
        return (flywheel.getVelocity() >= targetSpeed);
    }

}
