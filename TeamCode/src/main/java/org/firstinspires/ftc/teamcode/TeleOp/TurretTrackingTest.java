package org.firstinspires.ftc.teamcode.TeleOp;

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

@TeleOp(name = "Turret Tracking", group = "Linear Opmode")
public class TurretTrackingTest extends LinearOpMode {

    private ElapsedTime deltaTime = new ElapsedTime();
    private Limelight3A limeLight;
    private int currentTag;
    private LLResult llData;
    private double xOffset;
    private double prevXOffset;
    private IMU imu;
    RevHubOrientationOnRobot hubOrientation;
    private DcMotor turret;
    private double power;

    private double kp = 0.01;
    private double kd = 0.05;

    @Override
    public void runOpMode() throws InterruptedException {

        limeLight = hardwareMap.get(Limelight3A.class, "limeLight");
        limeLight.pipelineSwitch(0); //Tag24
        currentTag = 24;

        imu = hardwareMap.get(IMU.class, "imu");
        hubOrientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT);
        imu.initialize(new IMU.Parameters(hubOrientation));

        turret = hardwareMap.get(DcMotor.class, "turret");
        turret.setDirection(DcMotorSimple.Direction.REVERSE);

        limeLight.start();

        waitForStart();

        while(opModeIsActive()){

            // Collects Limelight Data
            llData = limeLight.getLatestResult();
            if(llData != null && llData.isValid()){
                Pose3D botPose = llData.getBotpose();
                prevXOffset = xOffset;
                xOffset = llData.getTx();

                //Sets motor power
                if (xOffset < -0.5 || xOffset > 0.5) {
                    power = (kp * xOffset) + (kd * (xOffset - prevXOffset) / deltaTime.seconds()); //PD no integration
                    turret.setPower(power);
                }

                deltaTime.reset();

            } else{
                power = 0;
            }



            //Sets the target tag
            if(gamepad1.a){
                limeLight.pipelineSwitch(0); //Tag24
                currentTag = 24;
            } else if(gamepad1.y){
                limeLight.pipelineSwitch(1); //Tag20
                currentTag = 20;
            }

            telemetry.addData("X Offset", xOffset);
            telemetry.addData("Motor Power", power);
            telemetry.addData("Tracked Tag", currentTag);
            telemetry.update();

        }
    }
}
