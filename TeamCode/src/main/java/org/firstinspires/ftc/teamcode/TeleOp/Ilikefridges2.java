package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name="Ilikefridges2", group ="Linear Opmode")
public class Ilikefridges2 extends LinearOpMode {


    private DcMotor Intake;
    private RevTouchSensor touchSensor;
    private int balls = 0;
    private boolean beenPressed = false;



    @Override

    public void runOpMode() throws InterruptedException {
        Intake = hardwareMap.get(DcMotor.class, "intake");
        touchSensor = hardwareMap.get(RevTouchSensor.class, "touchSensor");

        waitForStart();

        while (opModeIsActive()) {


              if (balls >= 3) {
                  Intake.setPower(-0.5);
              }else{
                   Intake.setPower(0.5);
              }

              if (firstPress()) {
                  balls += 1;

              }
              if (touchSensor.isPressed()) {
                  Intake.setPower(0);
              }

              updateBeenPressed();
              telemetry.addData("sensor", touchSensor.isPressed());
              telemetry.addData("balls", balls);
              telemetry.update();
            }
        }


    private boolean firstPress(){
        if (touchSensor.isPressed() && !beenPressed) {
            return true;
        }else{
            return false;
        }
    }
    private void updateBeenPressed(){
        if(touchSensor.isPressed()){
            beenPressed=true;
        }else
            beenPressed=false;
    }
    }