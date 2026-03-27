package org.firstinspires.ftc.teamcode.TeleOp;

import android.icu.text.Transliterator;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name="Encoders", group ="Linear opMode")
public class Encoders extends LinearOpMode {
private int degreestoticks (double degrees){
        ((degrees/360)*Ticksperrotation)*GearRatio
    }
    private int inputTicks;
private int Ticksperrotation= 538;
private double GearRatio =1.42857143;
private void turntoposition(int target){
    turret.setTargetPosition(target);
    turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    turret. setPower(1);
}
    private DcMotor turret;

private int revolutions =0;
revolutions=(int) Math.round(turret.getcurrentPosition()/Ticksperrotation)
if ((current-(Ticksperrotation* revolutions)) - > current-((Ticksperrotation + 1)* revolutions) -target){
    @Override

    public void runOpMode() throws InterruptedException {
        turret = hardwareMap.get(DcMotor.class, "turret");
        turret. setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret. setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret. setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.dpad_up){
                turret. setPower(0);
                turret. setPower(1);
                turret. setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }

            if (gamepad1.dpad_right){
               turret. setTargetPosition(193);
               turret. setMode(DcMotor.RunMode.RUN_TO_POSITION);
               turret. setPower(1);
            }
            if (gamepad1.dpad_down) {
                turret.setPower(1);
                turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                turret.setTargetPosition(384);
            }
            if (gamepad1.dpad_left){
                turret.setPower(1);
                turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                turret.setTargetPosition(577);
while(inputTicks >Ticksperrotation){
    inputTicks - Ticksperrotation}
}

            }
        }
    }

    public int getInputTicks() {
        return inputTicks;
    }
}