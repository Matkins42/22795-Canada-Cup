package org.firstinspires.ftc.teamcode.TeleOp.Shaq;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;
import org.firstinspires.ftc.teamcode.Subsystems.DrivingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OuttakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;

@TeleOp(name = "Shaq TeleOp", group = "Linear Opmode")
public class ShaqTeleOp extends LinearOpMode {


    private IntakeSubsystem intake;
    private OuttakeSubsystem outtake;
    private TurretSubsystem turret;
    private DrivingSubsystem driveTrain;


    @Override
    public void runOpMode() throws InterruptedException {

        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);
        turret = new TurretSubsystem(hardwareMap);
        driveTrain = new DrivingSubsystem(hardwareMap);

        waitForStart();

        while(opModeIsActive()){

            driveTrain.drive(gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

            if (gamepad1.right_trigger > 0){
                turret.turnClockwise(gamepad1.right_trigger/2);
            } else if (gamepad1.left_trigger > 0){
                turret.turnCounterClockwise(gamepad1.left_trigger/2);
            }

            if (gamepad1.a){
                intake.stop();
            } else if (gamepad1.x){
                intake.collect();
            } else if (gamepad1.b){
                intake.expel();
            } else if (gamepad1.y) {
                intake.shoot();
            }

            if (gamepad1.right_bumper){
                outtake.startFlywheel();
            } else if (gamepad1.left_bumper){
                outtake.stopFlywheel();
            }

            if(gamepad1.dpad_left){
                outtake.setHoodAngle(RobotConstants.BOTTOM_ANGLE);
            } else if(gamepad1.dpad_up){
                outtake.setHoodAngle(38);
            } else if(gamepad1.dpad_right){
                outtake.setHoodAngle(RobotConstants.TOP_ANGLE);
            }

            if(gamepad2.a){
                outtake.flywheel.setPower(0.25);
            } else if(gamepad2.b){
                outtake.flywheel.setPower(0.5);
            } else if(gamepad2.y){
                outtake.flywheel.setPower(0.75);
            } else if(gamepad2.x){
                outtake.flywheel.setPower(1);
            }

            telemetry.addData("Speed", outtake.getOuttakeVelocity());
            telemetry.update();
        }
    }
}
