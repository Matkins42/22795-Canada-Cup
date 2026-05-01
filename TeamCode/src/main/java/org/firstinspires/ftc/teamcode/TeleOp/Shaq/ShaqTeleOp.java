package org.firstinspires.ftc.teamcode.TeleOp.Shaq;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;
import org.firstinspires.ftc.teamcode.Subsystems.DrivingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.FeedbackSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OuttakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TrackingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;

@TeleOp(name = "Shaq TeleOp", group = "Linear Opmode")
public class ShaqTeleOp extends LinearOpMode {


    private IntakeSubsystem intake;
    private OuttakeSubsystem outtake;
    private TurretSubsystem turret;
    private DrivingSubsystem driveTrain;
    private TrackingSubsystem tracking;
    private FeedbackSubsystem feedback;

    private String trackingMode = "ll";

    @Override
    public void runOpMode() throws InterruptedException {

        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);
        turret = new TurretSubsystem(hardwareMap);
        driveTrain = new DrivingSubsystem(hardwareMap);
        feedback = new FeedbackSubsystem();

        tracking = new TrackingSubsystem(hardwareMap, turret, outtake, RobotConstants.BLUE_GOAL);

        waitForStart();

        while(opModeIsActive()){

            //Driving code
            driveTrain.drive(gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

            //Set tracking mode
            if(gamepad2.dpad_left){
                trackingMode = "full";
                feedback.setLight(gamepad2, RobotConstants.GREEN);
            } else if(gamepad2.dpad_up){
                trackingMode = "ll";
                feedback.setLight(gamepad2, RobotConstants.YELLOW);
            } else if(gamepad2.dpad_right){
                trackingMode = "manual";
                feedback.setLight(gamepad2, RobotConstants.RED);
            }

            //Automatic tracking
            if(trackingMode == "full"){
                tracking.fullTracking();
            } else if (trackingMode == "ll") {
                tracking.llTracking();
            }

            //Manual turret turning
            turret.turnClockwise(gamepad2.left_stick_x);

            //Intake
            if (gamepad2.a) {
                intake.shoot();
            } else if (gamepad2.left_trigger > 0){
                intake.collect();
            } else if (gamepad2.right_trigger > 0){
                intake.expel();
            } else{
                intake.stop();
            }

            //Outtake
            if (gamepad2.right_bumper){
                outtake.startFlywheel();
            } else if (gamepad2.left_bumper){
                outtake.stopFlywheel();
            }

            outtake.update();

            //Hood control
            if(gamepad2.right_stick_button){
                outtake.setHoodAngle(RobotConstants.HOOD_ANGLE.lerp(-1, 1, -gamepad2.right_stick_y));
            }

            //Vibrates when ready to shoot
            if(tracking.trackingTag() && turret.isAiming() && outtake.reachedSpeed()){
                feedback.rumble(gamepad2);
            }

            telemetry.addData("Speed", outtake.getVelocity());
            telemetry.addData("TargetSpeed", outtake.getTargetVelocity());
            telemetry.addData("distance", tracking.getDistance());
            telemetry.update();
        }
    }
}
