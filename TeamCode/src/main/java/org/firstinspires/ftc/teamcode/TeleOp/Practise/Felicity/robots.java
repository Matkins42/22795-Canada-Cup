package org.firstinspires.ftc.teamcode.TeleOp.Practise.Felicity;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@Disabled
@TeleOp(name = "robots", group = "Linear Opmode")
public class robots extends LinearOpMode {

    private DcMotor motor;

    @Override

    public void runOpMode() throws InterruptedException {
        motor = hardwareMap.get(DcMotor.class, "motor");

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.dpad_down)
                motor.setTargetPosition(0);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(1);


            if (gamepad1.dpad_up)
                motor.setTargetPosition(269);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(1);

            if (gamepad1.dpad_left)
                motor.setTargetPosition(135);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(1);

            if (gamepad1.dpad_right)
                motor.setTargetPosition(404);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(1);


        }
    }
}
                    




