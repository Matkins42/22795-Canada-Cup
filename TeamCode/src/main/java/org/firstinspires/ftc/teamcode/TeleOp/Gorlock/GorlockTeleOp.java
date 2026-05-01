package org.firstinspires.ftc.teamcode.TeleOp.Gorlock;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.DrivingSubsystem;

@TeleOp(name = "Gorlock TeleOp", group = "Linear Opmode")
public class GorlockTeleOp extends LinearOpMode {

    private DrivingSubsystem driveTrain;

    @Override
    public void runOpMode() throws InterruptedException {

        driveTrain = new DrivingSubsystem(hardwareMap);

        waitForStart();

        while(opModeIsActive()){

            driveTrain.drive(gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

        }
    }
}
