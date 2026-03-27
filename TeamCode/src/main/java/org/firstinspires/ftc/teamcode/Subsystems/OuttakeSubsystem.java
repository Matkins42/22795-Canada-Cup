package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class OuttakeSubsystem {

    private DcMotorEx flywheel;

    private Servo hood;


    public OuttakeSubsystem(HardwareMap hardwareMap) {

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        hood = hardwareMap.get(Servo.class, "hood");
        flywheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //This runs when the subsystem is created, put hardware maps here
    }

    public void setOuttakeVeloicty(){
        flywheel.setVelocity(RobotConstants.OUTTAKE_Velocity);

    }
    public void startOuttake(double setPower){
            flywheel.setPower(RobotConstants.OUTTAKE_POWER);
    }

    public void stopOuttake(int setPower){
        flywheel.setPower(0);
    }

    public void setHood(double hoodPosition){
       hood.setPosition(hoodPosition);
    }

    public void setPostion(double angle){
        hood.setPosition (Math.max(RobotConstants.EXTENDED_SERVO_POSITION, Math.min(0,(angle - RobotConstants.BOTTOM_ANGLE) / (RobotConstants.ANGLE_DIFFERENCE / RobotConstants.EXTENDED_SERVO_POSITION))));
    }

}
