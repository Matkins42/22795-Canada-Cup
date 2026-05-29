package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OuttakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RoadRunnerSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TrackingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;


@Autonomous(name = "RedClose", group = "Autonomous") //Change the name here to what you want to show on the driver station
public class AutoRedClose extends LinearOpMode {

    //Put initialization of variables here (e.g subsystems)
    private IntakeSubsystem intake;
    private OuttakeSubsystem outtake;
    private TurretSubsystem turret;
    private TrackingSubsystem tracking;
    private RoadRunnerSubsystem roadRunner;
    private VelConstraint ultrafast;
    private VelConstraint fast;
    private VelConstraint medium;
    private VelConstraint slow;
    private AccelConstraint highAccel;
    private boolean shooting = false;
    private ElapsedTime shootingTimer;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d initialPose = new Pose2d(-50, 50, Math.toRadians(135)); //Sets the robots starting position
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        outtake = new OuttakeSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap, outtake);
        turret = new TurretSubsystem(hardwareMap);
        roadRunner = new RoadRunnerSubsystem(drive);
        tracking = new TrackingSubsystem(hardwareMap, roadRunner, turret, intake, outtake, RobotConstants.RED_GOAL); //Change this depending on what team we are

        fast = new TranslationalVelConstraint(1000);
        medium = new TranslationalVelConstraint(25);
        slow = new TranslationalVelConstraint(8);
        ultrafast = new TranslationalVelConstraint(95);

        highAccel = new ProfileAccelConstraint(-50, 50); // inches/s^2

        shootingTimer = new ElapsedTime();

        //Create actions here
        Action collect = packet -> {
            intake.collect();

            return false; //False means action runs once, true loops the action
        };

        Action shoot = packet -> {
            if (!shooting){
                shootingTimer.reset();
                shooting = true;
            }
            if (shootingTimer.seconds() < 2){
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

        Action trackTag = packet -> {
            tracking.fullTracking(packet);
            return true;
        };

        //Create trajectories here
        Action moveTo1stShoot = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(-15, 20), Math.toRadians(90),fast, highAccel)
                .waitSeconds(0.5)
                .build();

        Action moveToRow = drive.actionBuilder(new Pose2d(-15, 20, Math.toRadians (90)))
                .strafeToLinearHeading(new Vector2d(16, 30), Math.toRadians(90),fast, highAccel)
                .build();

        Action rowPickUp = drive.actionBuilder(new Pose2d(16, 30, Math.toRadians(90)))
  //              .strafeToLinearHeading(new Vector2d(18, 50), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(12, 61), Math.toRadians(90),slow, highAccel)
                .build();

        Action moveToRowShoot = drive.actionBuilder(new Pose2d(13, 61, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-3, 20), Math.toRadians(60),fast, highAccel)
//                .strafeToLinearHeading(new Vector2d(5, 30), Math.toRadians(90))
 //               .strafeToLinearHeading(new Vector2d(-15, 20), Math.toRadians(60))
                .build();

        Action openGate1 = drive.actionBuilder(new Pose2d(-3, 20, Math.toRadians(60)))//-15, 20, Math.toRadians(60)
                .splineTo(new Vector2d(12.5, 63.2), Math.toRadians(110),fast, highAccel)
                .build();

        Action openGate2 = drive.actionBuilder(new Pose2d(-3, 20, Math.toRadians(60)))//-15, 20, Math.toRadians(60)
                .splineTo(new Vector2d(12.5, 63.2), Math.toRadians(110),fast, highAccel)
                .build();

        Action gatePickUp1 = drive.actionBuilder(new Pose2d(12.5, 63.5, Math.toRadians(110)))
                .strafeToLinearHeading(new Vector2d(26, 66), Math.toRadians(145),fast, highAccel)
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(15, 66), Math.toRadians(145),fast, highAccel)
                .waitSeconds(1)
                .build();

        Action gatePickUp2 = drive.actionBuilder(new Pose2d(12.5, 63.5, Math.toRadians(110)))
                .strafeToLinearHeading(new Vector2d(26, 66), Math.toRadians(145),fast, highAccel)
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(15, 66), Math.toRadians(145),fast, highAccel)
                .waitSeconds(1)
                .build();

        Action moveToGateShoot1 = drive.actionBuilder(new Pose2d(15, 66, Math.toRadians(139)))
                .strafeToLinearHeading(new Vector2d(0, 20), Math.toRadians(60),fast, highAccel)
                .build();

        Action moveToGateShoot2 = drive.actionBuilder(new Pose2d(15, 66, Math.toRadians(139)))
                .strafeToLinearHeading(new Vector2d(0, 20), Math.toRadians(60),fast, highAccel)
                .build();

        Action movement9 = drive.actionBuilder(new Pose2d(0, 20, Math.toRadians(60)))
                .strafeToLinearHeading(new Vector2d(-11, 30), Math.toRadians(90),fast, highAccel)
                .build();

        Action movement10 = drive.actionBuilder(new Pose2d(-11, 30, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-11, 50), Math.toRadians(90),slow, highAccel)
                .build();

        Action movement11 = drive.actionBuilder(new Pose2d(-11, 50, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-2, 20), Math.toRadians(110),fast, highAccel)
                .build();

        Action movement12 = drive.actionBuilder(new Pose2d(-2, 20, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-55, 22), Math.toRadians(110),ultrafast, highAccel)
                .build();

        Action initialFire = new SequentialAction(
                startFlywheel,
                moveTo1stShoot,
                shoot
        );

        Action rowCycle = new SequentialAction(
                moveToRow,
                collect,
                rowPickUp,
                stopIntake,
                moveToRowShoot,
                shoot
        );

        Action gateCycle1 = new SequentialAction(
                openGate1,
                collect,
                gatePickUp1,
                stopIntake,
                moveToGateShoot1,
                shoot
        );

        Action gateCycle2 = new SequentialAction(
                openGate2,
                collect,
                gatePickUp2,
                stopIntake,
                moveToGateShoot2,
                shoot
        );

        tracking.setScaling(false);
        tracking.setOuttake(45, 1400, 0.3);

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new ParallelAction( //Put actions that run independant of movement, e.g sensing and tracking
                        trackTag,
                        new SequentialAction( //Put ordered actions here, e.g movement, intaking, arm movement
                                initialFire,
                                rowCycle,
                                gateCycle1,
                                gateCycle2,


//                                movement9,
 //                               collect,
 //                               movement10,
 //                               stopIntake,
 //                               movement11,
 //                               shoot,

                                movement12,
                                stopFlywheel,
                                stopIntake
                        )
                )
        );
    }
}