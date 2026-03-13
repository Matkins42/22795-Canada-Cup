package org.firstinspires.ftc.teamcode.TeleOp;

import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.round;
import static java.lang.Math.tan;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

@TeleOp(name = "Turret Tracking", group = "Linear Opmode")
public class TurretTrackingTest extends LinearOpMode {
    private MecanumDrive roadRunner;
    private Pose2d pose;
    private double xPosition;
    private double yPosition;
    private double heading;
    private double TARGET_X = 0; //Inches
    private double TARGET_Y = 144; //Inches
    private Limelight3A limeLight;
    private int currentTag;
    private LLResult llData;
    private boolean canSeeTag;
    private double xAngle;
    private DcMotor turret;
    private int TICKS_PER_ROTATION = 538;
    private double TURRET_GEAR_RATIO = 1.42857143;
    private int targetTicks;

    @Override
    public void runOpMode() throws InterruptedException {
        roadRunner = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));

        limeLight = hardwareMap.get(Limelight3A.class, "limeLight");
        limeLight.pipelineSwitch(0); //Tag24
        currentTag = 24;

        turret = hardwareMap.get(DcMotor.class, "turret");
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        limeLight.start();

        waitForStart();

        while(opModeIsActive()){

            //Updates Road Runner
            roadRunner.updatePoseEstimate();
            pose = roadRunner.localizer.getPose();
            xPosition = -1 * pose.position.y;
            yPosition = pose.position.x;
            heading = -1 * pose.heading.toDouble();

            // Collects Limelight Data
            llData = limeLight.getLatestResult();
            canSeeTag = llData != null && llData.isValid();
            if(canSeeTag){
                xAngle = llData.getTx();
                targetTicks = turret.getCurrentPosition() - degreesToTicks(xAngle);
            } else{
                targetTicks = degreesToTicks(Math.toDegrees(atan2((TARGET_X - xPosition),(TARGET_Y - yPosition)) - heading));
            }

            turret.setTargetPosition(Math.max(degreesToTicks(-135), Math.min(degreesToTicks(135), targetTicks)));
            turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            turret.setPower(1);


            //Sets the target tag
            if(gamepad1.a){
                limeLight.pipelineSwitch(0); //Tag24
                currentTag = 24;
            } else if(gamepad1.y){
                limeLight.pipelineSwitch(1); //Tag20
                currentTag = 20;
            }

            telemetry.addData("Current Angle", ticksToDegrees(turret.getCurrentPosition()));
            telemetry.addData("Base Angle", Math.toDegrees(atan2((TARGET_X - xPosition),(TARGET_Y - yPosition))));
            telemetry.addData("Target Ticks", targetTicks);
            telemetry.addData("Sees Tag", canSeeTag);
            telemetry.addData("Tracked Tag", currentTag);
            telemetry.addData("X Offset", xAngle);
            telemetry.addData("X Position", xPosition);
            telemetry.addData("Y Position", yPosition);
            telemetry.addData("Heading", heading);

            telemetry.update();

        }
    }

    private int degreesToTicks(double degrees){
        return (int) Math.round(((degrees/360) * TICKS_PER_ROTATION) * TURRET_GEAR_RATIO);
    }

    private double ticksToDegrees(double ticks){
        return ((ticks/TICKS_PER_ROTATION) * 360) / TURRET_GEAR_RATIO;
    }

}
