package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
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


//REMOVE THIS LINE - it makes it so it doesn't show up on the driver station
@Autonomous(name = "RedFar15", group = "Autonomous") //Change the name here to what you want to show on the driver station
public class RedFar15 extends LinearOpMode {

    //Put initialization of variables here (e.g subsystems)
    private IntakeSubsystem intake;
    private OuttakeSubsystem outtake;
    private TurretSubsystem turret;
    private TrackingSubsystem tracking;
    private RoadRunnerSubsystem roadRunner;
    private RobotConstants.Target goal = RobotConstants.RED_GOAL;

    private ElapsedTime shootingTimer;
    private boolean shooting;

    private AccelConstraint highAccel;

    private boolean track = false;
    private double targetAngle = 23;

    private VelConstraint slow;
    private VelConstraint medium;
    private VelConstraint fast;

    private Pose2d shootingPos = new Pose2d(49, 12, Math.toRadians(110));
    private Pose2d cornerPos = new Pose2d(59, 62, Math.toRadians(80));
    private Pose2d edgePos = new Pose2d(48, 62, Math.toRadians(90));

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d initialPose = new Pose2d(61, 15, Math.toRadians(180)); //Sets the robots starting position
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        outtake = new OuttakeSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap, outtake);
        turret = new TurretSubsystem(hardwareMap);
        roadRunner = new RoadRunnerSubsystem(drive);
        tracking = new TrackingSubsystem(hardwareMap, roadRunner, turret, intake, outtake, goal);

        turret.reset();

        slow = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(25), //inches/s
                new AngularVelConstraint(Math.toRadians(180)) // rad/s
        ));
        medium = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(45), //inches/s
                new AngularVelConstraint(Math.toRadians(180)) // rad/s
        ));
        fast = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(90), //inches/s
                new AngularVelConstraint(Math.toRadians(180)) // rad/s
        ));

        highAccel = new ProfileAccelConstraint(-45, 45); // inches/s^2

        shootingTimer = new ElapsedTime();

        AutoStorage.goal = goal;

        //Create actions here

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

        Action aimTurret = packet -> {
            targetAngle = -50.25;
            return false;
        };

        Action collect = packet -> {
            intake.collect();
            return false;
        };

        Action shoot = packet -> {
            if (!shooting) {
                shootingTimer.reset();
                shooting = true;
            }
            if (shootingTimer.seconds() < 1.775) {
                intake.shoot();
                outtake.increaseSpeed();
                return true;
            } else {
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

        Action updateTurret = packet -> {
            if (track) {
                tracking.fullTracking(packet);
            } else {
                turret.turnTo(turret.degreesToTicks(targetAngle), null);
            }
            return true;
        };

        Action moveToRow = drive.actionBuilder(initialPose)
                .splineTo(new Vector2d(35, 35), Math.toRadians(90), fast, highAccel)
                .strafeToLinearHeading(new Vector2d(35, 59.5), Math.toRadians(90), fast, highAccel)
                .strafeToLinearHeading(shootingPos.position, shootingPos.heading.toDouble(), fast, highAccel)
                .build();

        Action moveToCorner1 = drive.actionBuilder(shootingPos)
                .splineTo(cornerPos.position, cornerPos.heading.toDouble(), fast, highAccel)
                .strafeToLinearHeading(shootingPos.position, shootingPos.heading.toDouble(), fast, highAccel)
                .build();

        Action moveToCorner2 = drive.actionBuilder(shootingPos)
                .splineTo(cornerPos.position, cornerPos.heading.toDouble(), fast, highAccel)
                .strafeToLinearHeading(shootingPos.position, shootingPos.heading.toDouble(), fast, highAccel)
                .build();

        Action moveToCorner3 = drive.actionBuilder(shootingPos)
                .splineTo(cornerPos.position, cornerPos.heading.toDouble(), fast, highAccel)
                .strafeToLinearHeading(shootingPos.position, shootingPos.heading.toDouble(), fast, highAccel)
                .build();

        Action moveToEdge1 = drive.actionBuilder(shootingPos)
                .splineTo(edgePos.position, edgePos.heading.toDouble(), fast, highAccel)
                .strafeToLinearHeading(shootingPos.position, shootingPos.heading.toDouble(), fast, highAccel)
                .build();

        Action moveToEdge2 = drive.actionBuilder(shootingPos)
                .splineTo(edgePos.position, edgePos.heading.toDouble(), fast, highAccel)
                .strafeToLinearHeading(shootingPos.position, shootingPos.heading.toDouble(), fast, highAccel)
                .build();

        Action offLine = drive.actionBuilder(shootingPos)
                .strafeToLinearHeading(new Vector2d(55, 30), Math.toRadians(80), fast, highAccel)
                .build();

        Action initialFire = new SequentialAction(
                startFlywheel,
                new SleepAction(2.55),
                shoot
        );

        Action rowCycle = new SequentialAction(
                collect,
                moveToRow,
                shoot
        );

        Action cornerCycle1 = new SequentialAction(
                collect,
                moveToCorner1,
                shoot
        );

        Action cornerCycle2 = new SequentialAction(
                collect,
                moveToCorner2,
                shoot
        );

        Action cornerCycle3 = new SequentialAction(
                collect,
                moveToCorner3,
                shoot
        );

        Action edgeCycle1 = new SequentialAction(
                collect,
                moveToEdge1,
                shoot
        );

        Action edgeCycle2 = new SequentialAction(
                collect,
                moveToEdge2,
                shoot
        );


        tracking.setScaling(false);
        tracking.setOuttake(56, 1960, 0.49); //1990

        waitForStart();

        if (isStopRequested()) return;

        tracking.setOuttake(56, 1960, 0.49);

        Actions.runBlocking(
                new ParallelAction( //Put actions that run independant of movement, e.g sensing and tracking
                        updateTurret,
                        savePosition,
                        new SequentialAction( //Put ordered actions here, e.g movement, intaking, arm movement
                                initialFire,
                                aimTurret,
                                rowCycle,
                                cornerCycle1,
                                edgeCycle1,
                                cornerCycle2,
                                stopFlywheel,
                                resetTurret,
                                offLine
                        )
                )
        );
    }
}