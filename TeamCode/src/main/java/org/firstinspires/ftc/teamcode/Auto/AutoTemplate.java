package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
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

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OuttakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TrackingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;


@Disabled //REMOVE THIS LINE - it makes it so it doesn't show up on the driver station
@Autonomous(name = "Template", group = "Autonomous") //Change the name here to what you want to show on the driver station
public class AutoTemplate extends LinearOpMode {

    //Put initialization of variables here (e.g subsystems)
    private IntakeSubsystem intake;
    private OuttakeSubsystem outtake;
    private TurretSubsystem turret;
    private TrackingSubsystem tracking;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d initialPose = new Pose2d(0, 0, Math.toRadians(0)); //Sets the robots starting position
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);
        turret = new TurretSubsystem(hardwareMap);
        tracking = new TrackingSubsystem(hardwareMap, turret, outtake, RobotConstants.BLUE_GOAL); //Change this depending on what team we are

        //Create actions here
        Action exampleAction = packet -> {
            //Put action code here
            return false; //False means action runs once, true loops the action
        };

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
            tracking.fullTracking();
            return true;
        };

        //Create trajectories here
        Action exampleTrajectory = drive.actionBuilder(initialPose)
//                    Put trajectory code here

//                    e.g
//                    .lineToYSplineHeading(33, Math.toRadians(0))
//                    .waitSeconds(2)
//                    .setTangent(Math.toRadians(90))
//                    .lineToY(48)
//                    .setTangent(Math.toRadians(0))
//                    .lineToX(32)
//                    .strafeTo(new Vector2d(44.5, 30))
//                    .turn(Math.toRadians(180))
//                    .lineToX(47.5)
//                    .waitSeconds(3)

                    .build();

        // Code here runs on initialization

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new ParallelAction( //Put actions that run independant of movement, e.g sensing and tracking
                        trackTag,
                        new SequentialAction( //Put ordered actions here, e.g movement, intaking, arm movement
                                exampleTrajectory,
                                exampleAction
                        )
                )
        );
    }
}
