package org.firstinspires.ftc.teamcode.TeleOp.Shaq;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;
import org.firstinspires.ftc.teamcode.Subsystems.OuttakeSubsystem;

@TeleOp(name = "OuttakeTesting", group = "Linear Opmode") //Change the name here to what you want to show on the driver station
public class OuttakeTesting extends LinearOpMode {

    private OuttakeSubsystem outtake;

    @Override
    public void runOpMode() throws InterruptedException {

        outtake = new OuttakeSubsystem(hardwareMap);

        waitForStart();


        while(opModeIsActive()){

           if(gamepad1.right_bumper){
               outtake.startFlywheel();
           } else if(gamepad1.left_bumper){
               outtake.stopFlywheel();
           }
           
           if(gamepad1.dpad_left){
               outtake.setHoodAngle(RobotConstants.BOTTOM_ANGLE);
           } else if(gamepad1.dpad_up){
               outtake.setHoodAngle(38);
           } else if(gamepad1.dpad_right){
               outtake.setHoodAngle(RobotConstants.TOP_ANGLE);
           }

            if(gamepad1.a){
                outtake.flywheel.setPower(0.25);
            } else if(gamepad1.b){
                outtake.flywheel.setPower(0.5);
            } else if(gamepad1.y){
                outtake.flywheel.setPower(0.75);
            } else if(gamepad1.x){
                outtake.flywheel.setPower(1);
            }

            telemetry.addData("Speed", outtake.getOuttakeVelocity());
            telemetry.update();
        }
    }
}
