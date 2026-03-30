package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class TurretSubsystem {
    private DcMotor turret;

    public TurretSubsystem(HardwareMap hardwareMap) {
        turret = hardwareMap.get(DcMotor.class, "turret");
        turret.setDirection(DcMotor.Direction.REVERSE);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public double getPosition(){
        return turret.getCurrentPosition();
    }

    public void setPosition(double targetTicks){
        turret.setTargetPosition(Math.max(degreesToTicks(-135), Math.min(degreesToTicks(135), (int) Math.round(targetTicks))));
        //turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(RobotConstants.ROTATION_POWER);
    }

    public void turnClockwise(double input){
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turret.setPower(input);
    }

    public void turnCounterClockwise(double input){
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turret.setPower(-input);
    }

    public int degreesToTicks(double degrees){
        return (int) Math.round(((degrees/360) * RobotConstants.TICKS_PER_ROTATION) * RobotConstants.GEAR_RATIO);
    }

    public double ticksToDegrees(double ticks){
        return ((ticks/RobotConstants.TICKS_PER_ROTATION) * 360) / RobotConstants.GEAR_RATIO;


    }
}
