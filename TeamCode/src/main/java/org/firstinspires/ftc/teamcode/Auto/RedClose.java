package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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


@Autonomous(name = "RedClose", group = "Autonomous") //Change the name here to what you want to show on the driver station
public class RedClose extends LinearOpMode {

    //Put initialization of variables here (e.g subsystems)
    private IntakeSubsystem intake;
    private OuttakeSubsystem outtake;
    private TurretSubsystem turret;
    private TrackingSubsystem tracking;
    private RoadRunnerSubsystem roadRunner;

    private RobotConstants.Target goal = RobotConstants.RED_GOAL;

    private VelConstraint ultrafast;
    private VelConstraint fast;
    private VelConstraint medium;
    private VelConstraint slow;
    private AccelConstraint highAccel;
    private boolean shooting = false;
    private ElapsedTime shootingTimer;
    private boolean track = true;
    private double targetAngle = 0;
    private Pose2d shootingPos = new Pose2d(-10.5, 14, Math.toRadians(60));

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d initialPose = new Pose2d(-52.5, 47.5, Math.toRadians(126.5)); // -50, 50, Math.toRadians(135)
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        outtake = new OuttakeSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap, outtake);
        turret = new TurretSubsystem(hardwareMap);
        roadRunner = new RoadRunnerSubsystem(drive);
        tracking = new TrackingSubsystem(hardwareMap, roadRunner, turret, intake, outtake, goal);

        turret.reset();

        fast = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(75), //inches/s
                new AngularVelConstraint(Math.toRadians(270)) // rad/s
        ));
        medium = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(30), //inches/s
                new AngularVelConstraint(Math.toRadians(180)) // rad/s
        ));
        slow = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(20), //inches/s
                new AngularVelConstraint(Math.toRadians(90)) // rad/s
        ));
        ultrafast = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(95), //inches/s
                new AngularVelConstraint(Math.toRadians(360)) // rad/s
        ));

        highAccel = new ProfileAccelConstraint(-60, 60); // inches/s^2

        shootingTimer = new ElapsedTime();

        AutoStorage.goal = goal;
        AutoStorage.auto = true;

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
            if (shootingTimer.seconds() < 1.37){
                intake.shoot();
                outtake.increaseSpeed();
                return true;
            } else{
                shooting = false;
                outtake.resetIncrease();
                intake.stop();
                intake.resetStates();
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

        Action setMediumShooting = packet -> {
            tracking.setOuttake(39.25, 1510, 0.32);
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
        Action moveTo1stShoot = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(-17.5, 17.5), Math.toRadians(90), fast, highAccel)
                .build();

        Action moveToMidRow = drive.actionBuilder(new Pose2d(-17.5, 17.5, Math.toRadians (90)))
                .strafeToLinearHeading(new Vector2d(14.5, 17.5), Math.toRadians(90), fast, highAccel)
                .build();

        Action midRowPickUp = drive.actionBuilder(new Pose2d(14.5, 17.5, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(11.5, 50.5), Math.toRadians(90), slow, highAccel)
                .build();

        Action moveToMidRowShoot = drive.actionBuilder(new Pose2d(11.5, 50.5, Math.toRadians(90)))
                .strafeToLinearHeading(shootingPos.position, shootingPos.heading.toDouble(), fast, highAccel)
                .build();

        Action openGate1 = drive.actionBuilder(shootingPos)
                .setReversed(false)
                .splineTo(new Vector2d(9, 52.5), Math.toRadians(110), fast, highAccel)
                .build();

        Action openGate2 = drive.actionBuilder(shootingPos)
                .setReversed(false)
                .splineTo(new Vector2d(9, 52.5), Math.toRadians(110), fast, highAccel)
                .build();

        Action gatePickUp1 = drive.actionBuilder(new Pose2d(9, 52.5, Math.toRadians(110)))
                .strafeToLinearHeading(new Vector2d(27, 63.5), Math.toRadians(145), fast, highAccel)
                .strafeToLinearHeading(new Vector2d(13, 63.5), Math.toRadians(145), fast, highAccel)
                .waitSeconds(0.05)
                .build();

        Action gatePickUp2 = drive.actionBuilder(new Pose2d(9, 52.5, Math.toRadians(110)))
                .strafeToLinearHeading(new Vector2d(27, 63.5), Math.toRadians(145), fast, highAccel)
                .strafeToLinearHeading(new Vector2d(13, 63.5), Math.toRadians(145), fast, highAccel)
                .waitSeconds(0.05)
                .build();

        Action moveToGateShoot1 = drive.actionBuilder(new Pose2d(13, 63.5, Math.toRadians(145)))
                .setReversed(true)
                .splineTo(shootingPos.position, (shootingPos.heading.toDouble() + Math.PI), fast, highAccel)
                .build();

        Action moveToGateShoot2 = drive.actionBuilder(new Pose2d(13, 63.5, Math.toRadians(145)))
                .setReversed(true)
                .splineTo(shootingPos.position, (shootingPos.heading.toDouble() + Math.PI), fast, highAccel)
                .build();

        Action moveToFrontRow = drive.actionBuilder(shootingPos)
                .setReversed(false)
                .strafeToLinearHeading(new Vector2d(-10.5, 27.5), Math.toRadians(90), fast, highAccel)
                .build();

        Action frontRowPickUp = drive.actionBuilder(shootingPos)
                .splineTo(new Vector2d(-10.5, 52), Math.toRadians(90), fast, highAccel)
                .build();

        Action moveToFrontRowShoot = drive.actionBuilder(new Pose2d(-10.5, 52, Math.toRadians(90)))
                .strafeToLinearHeading(shootingPos.position, shootingPos.heading.toDouble(), fast, highAccel)
                .build();

        Action finalMove = drive.actionBuilder(shootingPos)
                .strafeTo(new Vector2d(-10.5, 30), ultrafast, highAccel)
                .build();

        Action initialFire = new SequentialAction(
                startFlywheel,
                moveTo1stShoot,
                shoot
        );

        Action midRowCycle = new SequentialAction(
                moveToMidRow,
                collect,
                midRowPickUp,
                moveToMidRowShoot,
                shoot
        );

        Action gateCycle1 = new SequentialAction(
                openGate1,
                collect,
                gatePickUp1,
                moveToGateShoot1,
                shoot
        );

        Action gateCycle2 = new SequentialAction(
                openGate2,
                collect,
                gatePickUp2,
                moveToGateShoot2,
                shoot
        );

        Action frontRowCycle = new SequentialAction(
                //moveToFrontRow,
                collect,
                frontRowPickUp,
                moveToFrontRowShoot,
                shoot
        );

        Action finish = new SequentialAction(
                resetTurret,
                stopIntake,
                stopFlywheel,
                finalMove
        );

        tracking.setScaling(false);
        tracking.setOuttake(35.25, 1255, 0.35);

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new ParallelAction(
                        updateTurret,
                        savePosition,
                        new SequentialAction(
                                initialFire,
                                setMediumShooting,
                                midRowCycle,
                                gateCycle1,
                                gateCycle2,
                                frontRowCycle,
                                finish
                        )
                )
        );
    }
}