package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OuttakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TrackingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;


@Autonomous(name = "RedClose", group = "Autonomous") //Change the name here to what you want to show on the driver station
public class AutoRedClose extends LinearOpMode {

    //Put initialization of variables here (e.g subsystems)
    private IntakeSubsystem intake;
    private OuttakeSubsystem outtake;
    private TurretSubsystem turret;
    private TrackingSubsystem tracking;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d initialPose = new Pose2d(-50, 50, Math.toRadians(135)); //Sets the robots starting position
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);
        turret = new TurretSubsystem(hardwareMap);
        tracking = new TrackingSubsystem(hardwareMap, turret, outtake, RobotConstants.RED_GOAL, initialPose); //Change this depending on what team we are

        //Create actions here
        Action collect = packet -> {
            intake.collect();

            return false; //False means action runs once, true loops the action
        };

        Action shoot = packet -> {
            intake.shoot();
            return false;
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
            tracking.fullTracking();
            return true;
        };

        //Create trajectories here
        Action movement1 = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(-15, 20), Math.toRadians(90))
                .waitSeconds(0.5)

                .build();



        Action movement2 = drive.actionBuilder(new Pose2d(-15, 20, Math.toRadians (90)))
                .waitSeconds(2)
                .strafeToLinearHeading(new Vector2d(18, 30), Math.toRadians(90))


                .build();

        Action movement3 = drive.actionBuilder(new Pose2d(18, 30, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(18, 50), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(8.5, 61), Math.toRadians(90))


                .build();

        Action movement4 = drive.actionBuilder(new Pose2d(8.5, 61, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(5, 30), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(-15, 20), Math.toRadians(60))

                .build();

        Action movement5 = drive.actionBuilder(new Pose2d(-15, 20, Math.toRadians(60)))
                .waitSeconds(2)
                .splineTo(new Vector2d(13, 61), Math.toRadians(110))

                .build();

        Action movement6 = drive.actionBuilder(new Pose2d(13, 61, Math.toRadians(110)))
                .strafeToLinearHeading(new Vector2d(26, 66), Math.toRadians(139))
                .waitSeconds(1)

                .build();

        Action movement7 = drive.actionBuilder(new Pose2d(26, 66, Math.toRadians(139)))
                .strafeToLinearHeading(new Vector2d(-15, 20), Math.toRadians(60))

                .build();


        Action movement8 = drive.actionBuilder(new Pose2d(26, 66, Math.toRadians(139)))
                .strafeToLinearHeading(new Vector2d(-2, 20), Math.toRadians(110))

                .build();

        Action movement9 = drive.actionBuilder(new Pose2d(-2, 20, Math.toRadians(110)))
                .waitSeconds(2)
                .strafeToLinearHeading(new Vector2d(-11, 30), Math.toRadians(90))

                .build();

        Action movement10 = drive.actionBuilder(new Pose2d(-11, 30, Math.toRadians(90)))
                .waitSeconds(2)
                .strafeToLinearHeading(new Vector2d(-11, 50), Math.toRadians(90))

                .build();

        Action movement11 = drive.actionBuilder(new Pose2d(-11, 50, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-2, 20), Math.toRadians(110))

                .build();

        Action movement12 = drive.actionBuilder(new Pose2d(-2, 20, Math.toRadians(90)))
                .waitSeconds(2)
                .strafeToLinearHeading(new Vector2d(-55, 22), Math.toRadians(110))

                .build();



        // Code here runs on initialization

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new ParallelAction( //Put actions that run independant of movement, e.g sensing and tracking
                        trackTag,

                        new SequentialAction( //Put ordered actions here, e.g movement, intaking, arm movement
                                startFlywheel,
                                movement1,
                                shoot,
                                movement2,
                                collect,
                                movement3,
                                stopIntake,
                                movement4,
                                shoot,
                                movement5,
                                collect,
                                movement6,
                                stopIntake,

 //                               movement7,
 //                               shoot,
 //                               movement5,
 //                               collect,
 //                               movement6,
 //15ball                               stopIntake,

                                movement8,
                                shoot,

                                movement9,
                                collect,
                                movement10,
                                stopIntake,
                                movement11,
                                shoot,

                                movement12,
                                stopFlywheel,
                                stopIntake
                        )
                )
        );
    }
}