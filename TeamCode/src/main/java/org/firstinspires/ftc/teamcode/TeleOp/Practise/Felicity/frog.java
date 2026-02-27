package org.firstinspires.ftc.teamcode.TeleOp.Practise.Felicity;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "frog", group = "Linear Opmode")
public class frog extends LinearOpMode {
    private DcMotor Intake;
    private RevTouchSensor touchSensor;

    private int ball = 0;

    private boolean beenPressed = false;

    @Override

    public void runOpMode() throws InterruptedException {
        Intake = hardwareMap.get(DcMotor.class, "intake");
        touchSensor = hardwareMap.get(RevTouchSensor.class, "touch_sensor");

        waitForStart();

        while (opModeIsActive()) {
             ball = 0;

            if (touchSensor.isPressed()) {
                Intake.setPower(0.5);
                if (touchSensor.isPressed()){
                    ball +=1;
                }
            } else {
                Intake.setPower(0.0);
            }

            if (ball <= 3){
                Intake.setPower(-0.5);
            }
        }
    } private boolean firstPress(){
        if (touchSensor.isPressed() && !beenPressed) {
            return true;
        } else{

            return false;
        }


    }
    private void UpdateBeenPressed(){
        if (beenPressed){

        }
    }
}

