package org.firstinspires.ftc.teamcode.Subsystems;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.security.acl.Group;

@TeleOp(name = "Servo", group = "LinearOpMode")
public class ServoTest extends LinearOpMode {

    @Override
    public void resetRuntime() {
    }

    private IntakeSubsystem intake;

    @Override
    public void runOpMode() throws InterruptedException {

        intake = new IntakeSubsystem(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a)
intake.StopperOpen();
intake.StopperClosed();
        }
    }
}