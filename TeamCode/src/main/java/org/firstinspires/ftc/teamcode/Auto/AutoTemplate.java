package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.VelConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Storage.AutoStorage;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OuttakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RoadRunnerSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TrackingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;

import java.util.Arrays;


@Disabled //REMOVE THIS LINE - it makes it so it doesn't show up on the driver station
@Autonomous(name = "Template", group = "Autonomous") //Change the name here to what you want to show on the driver station
public class AutoTemplate extends LinearOpMode {

    //Put initialization of variables here (e.g subsystems)
    private IntakeSubsystem intake;
    private OuttakeSubsystem outtake;
    private TurretSubsystem turret;
    private TrackingSubsystem tracking;
    private RoadRunnerSubsystem roadRunner;

    private RobotConstants.Target goal = RobotConstants.BLUE_GOAL; //Change this depending on what team we are

    private boolean shooting = false;
    private ElapsedTime shootingTimer;
    private boolean track = true;
    private double targetAngle = 0;

    private VelConstraint speedExample;
    private AccelConstraint accelExample;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d initialPose = new Pose2d(0, 0, Math.toRadians(0)); //Sets the robots starting position
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        outtake = new OuttakeSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap, outtake);
        turret = new TurretSubsystem(hardwareMap);
        roadRunner = new RoadRunnerSubsystem(drive);
        tracking = new TrackingSubsystem(hardwareMap, roadRunner, turret, intake, outtake, goal);

        //Sets roadrunner movement parameters
        speedExample = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(30), //inches/s
                new AngularVelConstraint(Math.toRadians(180)) // rad/s
        ));

        accelExample = new ProfileAccelConstraint(-30, 30); // inches/s^2

        shootingTimer = new ElapsedTime();

        AutoStorage.goal = goal;
        AutoStorage.auto = true;

        //Create actions here
        Action exampleAction = packet -> {
            //Put action code here
            return false; //False means action runs once, true loops the action
        };

        Action savePosition = packet -> {
            AutoStorage.autoEndPose = drive.localizer.getPose();
            return true;
        };

        Action trackingOn = packet -> {
            track = true;
            return false;
        };

        Action trackingOff = packet -> {
            track = false;
            return false;
        };

        Action resetTurret = packet -> {
            track = false;
            targetAngle = 0;
            return false;
        };

        Action collect = packet -> {
            intake.collect();
            return false;
        };

        Action shoot = packet -> {
            if (!shooting){
                shootingTimer.reset();
                shooting = true;
            }
            if (shootingTimer.seconds() < 3){
                intake.shoot();
                outtake.increaseSpeed();
                return true;
            } else{
                shooting = false;
                outtake.resetIncrease();
                intake.stop();
                return false;
            }
        };

        Action stopIntake = packet -> {
            intake.stop();
            return false;
        };

        Action startFlywheel = packet -> {
            outtake.startFlywheel();
            return false;
        };

        Action stopFlywheel = packet -> {
            outtake.stopFlywheel();
            return false;
        };

        Action updateTurret = packet -> {
            if(track){
                tracking.fullTracking(packet);
            } else{
                turret.turnTo(turret.degreesToTicks(targetAngle), null);
            }
            return true;
        };

        //Create trajectories here
        Action exampleTrajectory = drive.actionBuilder(initialPose)
//                    Put trajectory code here
//                    e.g
                    .lineToYSplineHeading(33, Math.toRadians(0))
                    .strafeTo(new Vector2d(44.5, 30), speedExample, accelExample) // -------  set a custom speed and acceleration at the end of each movement function
                    .turn(Math.toRadians(180))
                    .waitSeconds(3)
                    .build();

        tracking.setScaling(false);
        tracking.setOuttake(45, 1400, 0.35);

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new ParallelAction( //Put actions that run independant of movement, e.g sensing and tracking
                        updateTurret,
                        savePosition,
                        new SequentialAction( //Put ordered actions here, e.g movement, intaking, arm movement
                                exampleTrajectory,
                                exampleAction,
                                trackingOff,
                                resetTurret
                        )
                )
        );
    }
}
