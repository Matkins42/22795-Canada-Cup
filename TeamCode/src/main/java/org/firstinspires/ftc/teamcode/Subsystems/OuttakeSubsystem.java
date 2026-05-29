package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class OuttakeSubsystem {

    public DcMotorEx flywheel;
    private Servo hood;
    private double targetSpeed = RobotConstants.OUTTAKE_VELOCITY.MIN;
    private boolean on = false;

    private double increase = 0;
    private ElapsedTime dt = new ElapsedTime();


    public OuttakeSubsystem(HardwareMap hardwareMap) {

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);
        hood = hardwareMap.get(Servo.class, "hood");
        flywheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void update(){
        if (on) {
            flywheel.setVelocity(targetSpeed);
        }
    }

    public void increaseSpeed(){
        increase += RobotConstants.INCREASE_RATE * dt.seconds();
        increase = Math.max(0, Math.min(RobotConstants.MAX_INCREASE, increase));
        dt.reset();
    }

    public void resetIncrease(){
        increase = 0;
        dt.reset();
    }

    public void setVelocity(double speed){
        if (getVelocity() < speed - 20)
            targetSpeed = speed + increase;
        else{
            resetIncrease();
            targetSpeed = speed;
        }
    }

    public double getVelocity(){
        return flywheel.getVelocity();
    }

    public double getTargetVelocity(){
        return targetSpeed;
    }
    public void startFlywheel(){
        flywheel.setVelocity(targetSpeed);
        on = true;
    }

    public void stopFlywheel(){
        flywheel.setVelocity(0);
        on = false;
    }

    public void setHood(double hoodPosition){
       hood.setPosition(hoodPosition);
    }

    public void setHoodAngle(double angle){
        double newPos = (Math.max(0, Math.min(RobotConstants.EXTENDED_SERVO_POSITION,(angle - RobotConstants.HOOD_ANGLE.MIN) / ((RobotConstants.HOOD_ANGLE.MAX - RobotConstants.HOOD_ANGLE.MIN) / RobotConstants.EXTENDED_SERVO_POSITION))));
        if(Math.abs(newPos - hood.getPosition()) > RobotConstants.HOOD_DAMPENING){
            hood.setPosition(newPos);
        }
    }

    public boolean reachedSpeed(){
        return (flywheel.getVelocity() >= targetSpeed - RobotConstants.SPEED_TARGET_RANGE && flywheel.getVelocity() <= targetSpeed + RobotConstants.SPEED_TARGET_RANGE);
    }

    public double getHoodPosition(){
        return hood.getPosition();
    }

}
