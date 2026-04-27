package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class TurretSubsystem {
    private DcMotor turret;
    private double error;
    private double lastError;
    private double integral; //Sum of errors (offsets) - therefore increases over time
    private double derivative; //The rate of change of the error (how fast the motor is moving)
    private double power;
    private ElapsedTime time;
    private boolean aiming;

    public TurretSubsystem(HardwareMap hardwareMap) {
        turret = hardwareMap.get(DcMotor.class, "turret");
        turret.setDirection(DcMotor.Direction.REVERSE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public double getPosition(){
        return turret.getCurrentPosition();
    }

    public void turnTo(double target){
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        error = target - turret.getCurrentPosition();

        integral += error;
        derivative = time.seconds()*(error - lastError);

        power = Math.max(-1, Math.min(1, (RobotConstants.KP * error) + (RobotConstants.KI * integral) + (RobotConstants.KD * derivative))); //Calculates turning power and limits between 1 and -1

        if (Math.abs(error) < RobotConstants.DEADBAND){
            integral = 0;
            turret.setPower(0);
            aiming = true;
        } else{
            turret.setPower(power);
            aiming = false;
        }

        lastError = error;
        time.reset();
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

    public boolean isAiming(){
        return aiming;
    }
}
