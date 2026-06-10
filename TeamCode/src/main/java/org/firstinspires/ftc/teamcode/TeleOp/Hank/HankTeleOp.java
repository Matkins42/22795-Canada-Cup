package org.firstinspires.ftc.teamcode.TeleOp.Hank;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Storage.AutoStorage;
import org.firstinspires.ftc.teamcode.Subsystems.DrivingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.FeedbackSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OuttakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RoadRunnerSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TrackingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;

@TeleOp(name = "Hank TeleOp", group = "Linear Opmode")
public class HankTeleOp extends LinearOpMode {

    private IntakeSubsystem intake;
    private OuttakeSubsystem outtake;
    private TurretSubsystem turret;
    private DrivingSubsystem driveTrain;
    private TrackingSubsystem tracking;
    private RoadRunnerSubsystem roadRunner;
    private FeedbackSubsystem feedback;

    private String trackingMode = "full";
    private double manualOuttakeSpeed = RobotConstants.CLOSE_OUTTAKE_SPEED;

    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        TelemetryPacket packet = new TelemetryPacket();

        outtake = new OuttakeSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap, outtake);
        turret = new TurretSubsystem(hardwareMap);
        feedback = new FeedbackSubsystem();

        roadRunner = new RoadRunnerSubsystem(new MecanumDrive(hardwareMap, AutoStorage.autoEndPose));
        tracking = new TrackingSubsystem(hardwareMap, roadRunner, turret, intake, outtake, AutoStorage.goal);
        //NOTE: Driving subsystem must be initialised after Roadrunner/Tracking subsystem
        //else controller scheme is messed up
        driveTrain = new DrivingSubsystem(hardwareMap);

        if(!AutoStorage.auto){ //No auto has been run
            turret.reset();
        }

        //Starting lights
        feedback.setLight(gamepad1, RobotConstants.PINK);
        feedback.setLight(gamepad2, RobotConstants.GREEN);

        AutoStorage.reset();

        waitForStart();

        while(opModeIsActive()){

            //Set target
            if(gamepad1.left_trigger > 0 && gamepad1.right_trigger > 0){
                if(gamepad1.x){
                    tracking.setTarget(RobotConstants.BLUE_GOAL);
                } else if (gamepad1.b) {
                    tracking.setTarget(RobotConstants.RED_GOAL);
                }
            }

            if(gamepad1.right_bumper && gamepad1.left_bumper) {
                if (gamepad1.a) {
                    roadRunner.update();
                    roadRunner.setPose(RobotConstants.RESET_POSE); //Resets the entire roadrunner
                } else if (gamepad1.y) {
                    roadRunner.update();
                    roadRunner.setPose(new Pose2d(roadRunner.getX(), roadRunner.getY(), Math.toRadians(180))); //Resets the roadrunner heading
                }
            }

            if(gamepad1.dpad_right){
                tracking.setDistanceClamps(RobotConstants.DISTANCE_CLOSE_CLAMP.MIN, RobotConstants.DISTANCE_CLOSE_CLAMP.MAX);
                feedback.setLight(gamepad1, RobotConstants.GREEN);
            }else if(gamepad1.dpad_down){
                tracking.setDistanceClamps(RobotConstants.DISTANCE_MEDIUM_CLAMP.MIN, RobotConstants.DISTANCE_MEDIUM_CLAMP.MAX);
                feedback.setLight(gamepad1, RobotConstants.PINK);
            }else if(gamepad1.dpad_left){
                tracking.setDistanceClamps(RobotConstants.DISTANCE_FAR_CLAMP.MIN, RobotConstants.DISTANCE_FAR_CLAMP.MAX);
                feedback.setLight(gamepad1, RobotConstants.ORANGE);
            }else if(gamepad1.dpad_up) {
                tracking.setDistanceClamps(RobotConstants.DISTANCE_NO_CLAMP.MIN, RobotConstants.DISTANCE_NO_CLAMP.MAX);
                feedback.setLight(gamepad1, RobotConstants.BLUE);
            }

            //Driving code
            driveTrain.normalisedDrive(gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

            //Set tracking mode
            if(gamepad2.dpad_left){
                trackingMode = "full";
                feedback.setLight(gamepad2, RobotConstants.GREEN);
            } else if(gamepad2.dpad_up){
                trackingMode = "ll";
                feedback.setLight(gamepad2, RobotConstants.YELLOW);
            } else if(gamepad2.dpad_down){
                trackingMode = "rr";
                feedback.setLight(gamepad2, RobotConstants.PURPLE);
            } else if(gamepad2.dpad_right){
                trackingMode = "manual";
                feedback.setLight(gamepad2, RobotConstants.RED);
            }

            if(gamepad2.share){
                turret.reset();
            }

            //Automatic tracking
            if(Math.abs(gamepad2.left_stick_x) > 0.1){
                turret.turnClockwise(gamepad2.left_stick_x);
            } else if(trackingMode.equals("full")){
                tracking.fullTracking(packet);
            } else if(trackingMode.equals("ll")) {
                tracking.llTracking(packet);
            } else if(trackingMode.equals("rr")) {
                tracking.rrTracking(packet);
            } else{
                turret.turnClockwise(0);
            }

            //Setting Manual Outtake Speed
            if(trackingMode.equals("manual")){
                if(gamepad2.x) {
                    manualOuttakeSpeed = (RobotConstants.CLOSE_OUTTAKE_SPEED);
                    intake.setSpacingTime(0.4);
                }else if(gamepad2.b){
                    manualOuttakeSpeed = (RobotConstants.FAR_OUTTAKE_SPEED);
                    intake.setSpacingTime(0.6);
                }
                outtake.setVelocity(manualOuttakeSpeed);
                outtake.update();
            }

            //Intake
            if(gamepad2.a) {
                intake.shoot();
                //outtake.increaseSpeed();
            } else{
                //outtake.resetIncrease();
                intake.resetStates();
                if(gamepad2.left_trigger > 0){
                    intake.collect();
                } else if(gamepad2.right_trigger > 0){
                    intake.expel();
                } else{
                    intake.stop();
                }
            }

            //Outtake
            if(gamepad2.right_bumper){
                outtake.startFlywheel();
            } else if(gamepad2.left_bumper){
                outtake.stopFlywheel();
            }

            //Hood control
            if(gamepad2.right_stick_button){
                outtake.setHoodAngle(RobotConstants.HOOD_ANGLE.lerp(-1, 1, -gamepad2.right_stick_y));
            }

            //Vibrates when ready to shoot
            if(tracking.seesTag() && turret.isAiming() && outtake.reachedSpeed()){
                feedback.rumble(gamepad1);
                feedback.rumble(gamepad2);
            }

            telemetry.addData("SeesTag", tracking.seesTag());
            telemetry.addData("Speed", outtake.getVelocity());
            telemetry.addData("TargetSpeed", outtake.getTargetVelocity());
            telemetry.addData("Distance", tracking.getDistance());
            telemetry.addData("Hood", outtake.getHoodPosition());
            telemetry.addData("x", tracking.xPos());
            telemetry.addData("y", tracking.yPos());
            telemetry.update();

            dashboard.sendTelemetryPacket(packet);
        }
    }
}
