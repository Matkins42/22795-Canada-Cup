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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OuttakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RoadRunnerSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TrackingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;


//REMOVE THIS LINE - it makes it so it doesn't show up on the driver station
@Autonomous(name = "RedFar", group = "Autonomous") //Change the name here to what you want to show on the driver station
public class FarzoneAutoRed extends LinearOpMode {

   //Put initialization of variables here (e.g subsystems)
   private IntakeSubsystem intake;
   private OuttakeSubsystem outtake;
   private TurretSubsystem turret;
   private TrackingSubsystem tracking;
   private RoadRunnerSubsystem roadRunner;


   private VelConstraint slow;
   private VelConstraint medium;
   private VelConstraint fast;




    @Override
   public void runOpMode() throws InterruptedException {
       Pose2d initialPose = new Pose2d(61, 8 , Math.toRadians(180)); //Sets the robots starting position
       MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

       intake = new IntakeSubsystem(hardwareMap);
       outtake = new OuttakeSubsystem(hardwareMap);
       turret = new TurretSubsystem(hardwareMap);
       roadRunner = new RoadRunnerSubsystem(drive);
       tracking = new TrackingSubsystem(hardwareMap, roadRunner, turret, outtake, RobotConstants.BLUE_GOAL); //Change this depending on what team we are

        slow = new TranslationalVelConstraint(15);
        medium = new TranslationalVelConstraint(30);
        fast = new TranslationalVelConstraint(50);

       //Create actions here


       Action collect = packet -> {
           intake.collect();
           return false;
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
           tracking.fullTracking(packet);
           return true;
       };


        Action moveToRow = drive.actionBuilder(initialPose)
                .splineTo(new Vector2d(33, 35), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(33, 57), Math.toRadians(90),slow)
                .strafeToLinearHeading(new Vector2d(57, 17), Math.toRadians(90))
                .build();


        Action moveToCorner1 = drive.actionBuilder(new Pose2d(57, 17, Math.toRadians(90)))
                .splineTo(new Vector2d(60, -58), Math.toRadians(-80))
                .strafeToLinearHeading(new Vector2d(58, -17), Math.toRadians(-90))
                .build();

        Action moveToCorner2 = drive.actionBuilder(new Pose2d(57, 17, Math.toRadians(90)))
                .splineTo(new Vector2d(60, 58), Math.toRadians(80))
                .strafeToLinearHeading(new Vector2d(58, 17), Math.toRadians(90))
                .build();

        Action initialFire = new SequentialAction(
                startFlywheel,
                new SleepAction(3),
                shoot,
                new SleepAction(2)
                );

        Action rowCycle = new SequentialAction(
                collect,
                moveToRow,
                shoot,
                new SleepAction(3)
        );

        Action cornerCycle1 = new SequentialAction(
                collect,
                moveToCorner1,
                shoot,
                new SleepAction(3)
        );

        Action cornerCycle2 = new SequentialAction(
                collect,
                moveToCorner2,
                shoot,
                new SleepAction(3)
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
                               collect,
                               cornerCycle1,
                               cornerCycle2,
                               stopFlywheel
                       )
               )
       );;
   }
}
