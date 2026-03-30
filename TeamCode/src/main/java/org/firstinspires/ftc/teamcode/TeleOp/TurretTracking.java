package org.firstinspires.ftc.teamcode.TeleOp;

import static java.lang.Math.atan2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.DrivingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.LimeLightSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RoadRunnerSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

@TeleOp(name = "TurretTracking", group = "Linear Opmode")
public class TurretTracking extends LinearOpMode {

    private LimeLightSubsystem limeLight;
    private RoadRunnerSubsystem roadRunner;
    private TurretSubsystem turret;
    private DrivingSubsystem driveTrain;
    private double targetTicks = 0;
    private RobotConstants.Target target = RobotConstants.BLUE_GOAL;

    @Override
    public void runOpMode() throws InterruptedException {

        limeLight = new LimeLightSubsystem(hardwareMap);
        roadRunner = new RoadRunnerSubsystem(hardwareMap);
        turret = new TurretSubsystem(hardwareMap);
        driveTrain = new DrivingSubsystem(hardwareMap);

        waitForStart();

        //Code here run once when start is pressed

        while(opModeIsActive()){

           roadRunner.update();

           //Sets the target
           if(gamepad1.a){
               target = RobotConstants.BLUE_GOAL;
               limeLight.switchPipeline(target.PIPELINE);
           } else if (gamepad1.y){
               target = RobotConstants.RED_GOAL;
               limeLight.switchPipeline(target.PIPELINE);
           }

           //driveTrain.drive(gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

           //Tracks Tag
           if(limeLight.seesTag()){
               targetTicks = turret.getPosition() + turret.degreesToTicks(limeLight.getXAngle());
               telemetry.addData("Target Angle", limeLight.getXAngle());
           } else{
               targetTicks = turret.getPosition();
               //targetTicks = turret.degreesToTicks(roadRunner.getEstimatedAngle(target));
           }

           turret.setPosition(targetTicks);

           telemetry.addData("Target Ticks", targetTicks);
           telemetry.addData("Sees Tag", limeLight.seesTag());
           telemetry.addData("Distance", limeLight.getDistance());
           telemetry.update();
        }
    }
}
