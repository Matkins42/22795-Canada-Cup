package org.firstinspires.ftc.teamcode.Subsystems;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.security.acl.Group;

@TeleOp(name = "Intake", group = "LinearOpMode")
public class IntakeTesting extends LinearOpMode {

    @Override
    public void resetRuntime() {
    }

    private IntakeSubsystem intake;

    @Override
    public void runOpMode() throws InterruptedException {

        intake = new IntakeSubsystem(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.right_trigger > 0) {
                intake.spinForwards();
            } else if (gamepad1.left_trigger > 0) {
                intake.spinBackwards();
            } else {
                intake.stop();
            }
        }
    }
}


