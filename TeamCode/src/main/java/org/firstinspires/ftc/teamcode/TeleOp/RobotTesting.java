package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Subsystems.DrivingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;

@TeleOp(name = "Robot Testing", group = "Linear Opmode") //Change the name here to what you want to show on the driver station
public class RobotTesting extends LinearOpMode {

    private DcMotorEx flywheel;
    private Servo hood;
    private IntakeSubsystem intake;
    private TurretSubsystem turret;
    private DrivingSubsystem driveTrain;


    @Override
    public void runOpMode() throws InterruptedException {

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        hood = hardwareMap.get(Servo.class, "hood");

        intake = new IntakeSubsystem(hardwareMap);
        turret = new TurretSubsystem(hardwareMap);
        driveTrain = new DrivingSubsystem(hardwareMap);

        waitForStart();

        while(opModeIsActive()){

            driveTrain.drive(gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

            if (gamepad1.right_trigger > 0){
                turret.turnClockwise(gamepad1.right_trigger/3);
            } else if (gamepad1.left_trigger > 0){
                turret.turnCounterClockwise(gamepad1.left_trigger/3);
            }

            if (gamepad1.x){
                intake.spinForwards();
            } else if (gamepad1.b){
                intake.spinBackwards();
            } else if(gamepad1.a){
                intake.stop();
            }

            if (gamepad1.right_bumper){
                flywheel.setPower(1);
            } else if (gamepad1.left_bumper){
                flywheel.setPower(0);
            }

            if(gamepad1.dpad_left){
                hood.setPosition(0);
            } else if(gamepad1.dpad_right){
                hood.setPosition(0.3);
            }
        }
    }
}
