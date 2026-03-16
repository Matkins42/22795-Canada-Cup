package org.firstinspires.ftc.teamcode.TeleOp;

import static java.lang.Math.atan2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.DrivingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.LimeLightSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RoadRunnerSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;

@TeleOp(name = "TurretTracking", group = "Linear Opmode")
public class TurretTracking extends LinearOpMode {

    private LimeLightSubsystem limeLight;
    private RoadRunnerSubsystem roadRunner;
    private TurretSubsystem turret;
    private DrivingSubsystem driveTrain;
    private double targetTicks;
    private String target;

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
               target = "blue";
               limeLight.switchPipeline(0);
           } else if (gamepad1.y){
               target = "red";
               limeLight.switchPipeline(1);
           }

            driveTrain.drive(gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

           //Tracks Tag
           if(limeLight.seesTag()){
               targetTicks = turret.getPosition() - turret.degreesToTicks(limeLight.getXAngle());
           } else{
               targetTicks = turret.degreesToTicks(roadRunner.getEstimatedAngle(target));
           }

           turret.setPosition(targetTicks);
        }
    }
}
