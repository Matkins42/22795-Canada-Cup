package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class TurretSubsystem {
    private DcMotor turret;
    private double position;
    private double lastPosition;
    private double error;
    private double integral; //Sum of errors (offsets) - therefore increases over time
    private double derivative; //The rate of change of the error (how fast the motor is moving)
    private double power;
    private ElapsedTime time;
    private double dt;
    private boolean aiming;

    public TurretSubsystem(HardwareMap hardwareMap) {
        turret = hardwareMap.get(DcMotor.class, "turret");
        turret.setDirection(DcMotor.Direction.REVERSE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        time = new ElapsedTime();
    }

    public double getPosition(){
        return turret.getCurrentPosition();
    }

    public void turnTo(double target){  //Issue could be due to changing power, if continues change to be based on angular velocity
        target = Math.max(degreesToTicks(RobotConstants.TURRET_RANGE/-2), Math.min(degreesToTicks(RobotConstants.TURRET_RANGE/2), target));
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        position = turret.getCurrentPosition();
        error = target - position;
        dt = Math.max(time.seconds(), 0.001);

        integral += error * dt;
        derivative = -(position - lastPosition) / dt;

        power = Math.max(-0.5, Math.min(0.5, (RobotConstants.KP * error) + (RobotConstants.KI * integral) + (RobotConstants.KD * derivative))); //Calculates turning power and limits between 1 and -1

        if (Math.abs(error) < RobotConstants.DEADBAND){
            integral = 0;
            turret.setPower(0);
            aiming = true;
        } else{
            turret.setPower(power);
            aiming = false;
        }

        lastPosition = position;
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
