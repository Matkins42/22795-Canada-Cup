package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class IntakeSubsystem {
    private DcMotor frontIntake;
    private DcMotor backIntake;

    private ElapsedTime timer = new ElapsedTime();
    private int shootingState = 1;

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
    public void shoot() {
        updateTimer();
        frontIntake.setPower(RobotConstants.FRONT_FIRING_POWER);
        if (shootingState == 1) {
            backIntake.setPower(RobotConstants.BACK_FIRING_POWER_1);
        } else {
            backIntake.setPower(RobotConstants.BACK_FIRING_POWER_2);
        }
    }

    public void resetStates(){
        shootingState = 1;
        timer.reset();
    }

    public void updateTimer(){
        if(shootingState == 1){
            if(timer.seconds() > RobotConstants.SHOOTING_STATE_1_TIME) {
                if (RobotConstants.SHOOTING_STATE_2_TIME > 0) {
                    shootingState = 2;
                }
                timer.reset();
            }
        } else if (shootingState == 2){
            if(timer.seconds() > RobotConstants.SHOOTING_STATE_2_TIME) {
                if (RobotConstants.SHOOTING_STATE_1_TIME > 0) {
                    shootingState = 1;
                }
                timer.reset();
            }
        }

    }

}


