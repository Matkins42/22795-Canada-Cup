package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
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


 //REMOVE THIS LINE - it makes it so it doesn't show up on the driver station
@Autonomous(name = "FarzoneAuto", group = "Autonomous") //Change the name here to what you want to show on the driver station
public class FarzoneAuto extends LinearOpMode {

    //Put initialization of variables here (e.g subsystems)
    private IntakeSubsystem intake;
    private OuttakeSubsystem outtake;
    private TurretSubsystem turret;
    private TrackingSubsystem tracking;
    private RoadRunnerSubsystem roadRunner;

    private ElapsedTime shootingTimer;
    private boolean shooting;


    private VelConstraint slow;
    private VelConstraint medium;
    private VelConstraint fast;
     



     @Override
    public void runOpMode() throws InterruptedException {
        Pose2d initialPose = new Pose2d(61, -8 , Math.toRadians(180)); //Sets the robots starting position
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        outtake = new OuttakeSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap, outtake);
        turret = new TurretSubsystem(hardwareMap);
        roadRunner = new RoadRunnerSubsystem(drive);
        tracking = new TrackingSubsystem(hardwareMap, roadRunner, turret, intake, outtake, RobotConstants.BLUE_GOAL); //Change this depending on what team we are

        slow = new TranslationalVelConstraint(35);
        medium = new TranslationalVelConstraint(55);
        fast = new TranslationalVelConstraint(80);

        shootingTimer = new ElapsedTime();

        //Create actions here


        Action collect = packet -> {
            intake.collect();
            return false;
        };

         Action startTimer = packet -> {
             shootingTimer.reset();
             return false;
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

        Action resetShooting = packet -> {
           outtake.resetIncrease();
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
            tracking.fullTracking(packet);
            return true;
        };


         Action moveToRow = drive.actionBuilder(initialPose)
                 .splineTo(new Vector2d(33, -35), Math.toRadians(-90),fast)
                 .strafeToLinearHeading(new Vector2d(33, -57), Math.toRadians(-90),slow)
                 .splineTo(new Vector2d(59,-10), Math.toRadians(-90),fast)

                // .strafeToLinearHeading(new Vector2d(59, -10), Math.toRadians(-90),fast)
                 .build();


         Action moveToCorner1 = drive.actionBuilder(new Pose2d(59, -10, Math.toRadians(-90)))
                 .splineTo(new Vector2d(61, -59), Math.toRadians(-80),fast)
                 .splineTo(new Vector2d(59, -10), Math.toRadians(-80),fast)
                 //.strafeToLinearHeading(new Vector2d(59, -10), Math.toRadians(-90),fast)
                 .build();

         Action moveToCorner2 = drive.actionBuilder(new Pose2d(59, -10, Math.toRadians(-90)))
                 .splineTo(new Vector2d(61, -59), Math.toRadians(-80),fast)
                 .splineTo(new Vector2d(59, -10), Math.toRadians(-80),fast)
                // .strafeToLinearHeading(new Vector2d(59, -10), Math.toRadians(-90),fast)
                 .build();

         Action moveToCorner3 = drive.actionBuilder(new Pose2d(59, -10, Math.toRadians(-90)))
                 .splineTo(new Vector2d(61, -59), Math.toRadians(-80),fast)
                 .splineTo(new Vector2d(59, -10), Math.toRadians(-80),fast)
                // .strafeToLinearHeading(new Vector2d(59, -10), Math.toRadians(-90),fast)
                 .build();

         Action initialFire = new SequentialAction(
                 startFlywheel,
                 new SleepAction(3),
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

        // Code here runs on initialization

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new ParallelAction( //Put actions that run independant of movement, e.g sensing and tracking
                        trackTag,
                        new SequentialAction( //Put ordered actions here, e.g movement, intaking, arm movement
                                initialFire,
                                rowCycle,
                                cornerCycle1,
                                cornerCycle2,
                                cornerCycle3,
                                stopFlywheel
                        )
                )
        );;
    }
}
