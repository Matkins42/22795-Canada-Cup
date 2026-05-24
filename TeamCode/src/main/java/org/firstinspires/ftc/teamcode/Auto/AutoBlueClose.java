package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
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


@Autonomous(name = "BlueClose", group = "Autonomous") //Change the name here to what you want to show on the driver station
public class AutoBlueClose extends LinearOpMode {

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
    private boolean shooting = false;
    private ElapsedTime shootingTimer;


    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d initialPose = new Pose2d(-50, -50, Math.toRadians(225)); //Sets the robots starting position
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);
        turret = new TurretSubsystem(hardwareMap);
        roadRunner = new RoadRunnerSubsystem(drive);
        tracking = new TrackingSubsystem(hardwareMap, roadRunner, turret, outtake, RobotConstants.RED_GOAL); //Change this depending on what team we are

        fast = new TranslationalVelConstraint(60);
        medium = new TranslationalVelConstraint(25);
        slow = new TranslationalVelConstraint(17);
        ultrafast = new TranslationalVelConstraint(50);


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


        Action trackTag = packet -> {
            tracking.fullTracking(packet);
            return true;
        };

        //Create trajectories here
        Action movement1 = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(-15, -20), Math.toRadians(270),fast)
                .waitSeconds(0.5)

                .build();



        Action movement2 = drive.actionBuilder(new Pose2d(-15, -20, Math.toRadians (270)))
                .waitSeconds(1.5)
                .strafeToLinearHeading(new Vector2d(16, -30), Math.toRadians(270),fast)


                .build();

        Action movement3 = drive.actionBuilder(new Pose2d(16, -30, Math.toRadians(270)))
                //              .strafeToLinearHeading(new Vector2d(18, 50), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(13, -61), Math.toRadians(270),slow)


                .build();

        Action movement4 = drive.actionBuilder(new Pose2d(13, -61, Math.toRadians(270)))
                .strafeToLinearHeading(new Vector2d(-3, -20), Math.toRadians(300),fast)
//                .strafeToLinearHeading(new Vector2d(5, 30), Math.toRadians(90))
                //               .strafeToLinearHeading(new Vector2d(-15, 20), Math.toRadians(60))

                .build();

        Action movement5_1 = drive.actionBuilder(new Pose2d(-3, -20, Math.toRadians(300)))//-15, 20, Math.toRadians(60)
                .waitSeconds(2)
                .splineTo(new Vector2d(12.5, -62.5), Math.toRadians(250),fast)

                .build();

        Action movement5_2 = drive.actionBuilder(new Pose2d(-3, -20, Math.toRadians(300)))//-15, 20, Math.toRadians(60)
                .waitSeconds(2)
                .splineTo(new Vector2d(12.5, -62.5), Math.toRadians(250),fast)

                .build();

        Action movement6_1 = drive.actionBuilder(new Pose2d(12.5, -62.5, Math.toRadians(250)))
                .strafeToLinearHeading(new Vector2d(26, -66), Math.toRadians(221),fast)
                .waitSeconds(1)

                .build();

        Action movement6_2 = drive.actionBuilder(new Pose2d(12.5, -62.5, Math.toRadians(250)))
                .strafeToLinearHeading(new Vector2d(26, -66), Math.toRadians(221),fast)
                .waitSeconds(1)

                .build();

        Action movement7 = drive.actionBuilder(new Pose2d(26, -66, Math.toRadians(221)))
                .strafeToLinearHeading(new Vector2d(-3, -20), Math.toRadians(300))

                .build();


        Action movement8_1 = drive.actionBuilder(new Pose2d(26, -66, Math.toRadians(221)))
                .strafeToLinearHeading(new Vector2d(-3, -20), Math.toRadians(300),fast)

                .build();

        Action movement8_2 = drive.actionBuilder(new Pose2d(26, -66, Math.toRadians(221)))
                .strafeToLinearHeading(new Vector2d(-3, -20), Math.toRadians(300),fast)

                .build();



        Action movement9 = drive.actionBuilder(new Pose2d(-3, -20, Math.toRadians(300)))
                .waitSeconds(2)
                .strafeToLinearHeading(new Vector2d(-11, -30), Math.toRadians(270))

                .build();

        Action movement10 = drive.actionBuilder(new Pose2d(-11, -30, Math.toRadians(270)))
                .waitSeconds(2)
                .strafeToLinearHeading(new Vector2d(-11, -50), Math.toRadians(270))

                .build();

        Action movement11 = drive.actionBuilder(new Pose2d(-11, -50, Math.toRadians(270)))
                .strafeToLinearHeading(new Vector2d(-2, -20), Math.toRadians(250))

                .build();

        Action movement12 = drive.actionBuilder(new Pose2d(-2, -20, Math.toRadians(270)))
                .waitSeconds(2)
                .strafeToLinearHeading(new Vector2d(-55, -22), Math.toRadians(250),ultrafast)

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
                                movement5_1,
                                collect,
                                movement6_1,
                                stopIntake,
                                movement8_1,
                                shoot,


                                movement5_2,
                                collect,
                                movement6_2,
                                stopIntake,
                                movement8_2,
                                shoot,



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