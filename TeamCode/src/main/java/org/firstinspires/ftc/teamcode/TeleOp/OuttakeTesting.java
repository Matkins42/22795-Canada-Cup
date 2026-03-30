package org.firstinspires.ftc.teamcode.TeleOp;

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

           if(gamepad1.a){
               outtake.startFlywheel();
           } else if(gamepad1.b){
               outtake.stopFlywheel();
           }
           
           if(gamepad1.dpad_left){
               outtake.setHoodAngle(RobotConstants.BOTTOM_ANGLE);
           } else if(gamepad1.dpad_up){
               outtake.setHoodAngle(38);
           } else if(gamepad1.dpad_right){
               outtake.setHoodAngle(RobotConstants.TOP_ANGLE);
           }

        }
    }
}
