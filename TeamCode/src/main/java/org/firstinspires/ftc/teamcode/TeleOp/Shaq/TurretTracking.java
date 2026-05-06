package org.firstinspires.ftc.teamcode.TeleOp.Shaq;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.DrivingSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.LimeLightSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OuttakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RoadRunnerSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

@TeleOp(name = "TurretTracking", group = "Linear Opmode")
public class TurretTracking extends LinearOpMode {

    private LimeLightSubsystem limeLight;
    private RoadRunnerSubsystem roadRunner;
    private TurretSubsystem turret;
    private DrivingSubsystem driveTrain;
    private OuttakeSubsystem outtake;
    private double targetTicks = 0;
    private RobotConstants.Target target = RobotConstants.BLUE_GOAL;
    private double distance = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        limeLight = new LimeLightSubsystem(hardwareMap);
        roadRunner = new RoadRunnerSubsystem(hardwareMap, new Pose2d(0, 0, Math.toRadians(180)));
        turret = new TurretSubsystem(hardwareMap);
        driveTrain = new DrivingSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);

        waitForStart();

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

           //Drives the robot
           driveTrain.drive(gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

           //Tracks Tag
           if(limeLight.seesTag()){
               targetTicks = turret.getPosition() + turret.degreesToTicks(limeLight.getXAngle());
               distance = limeLight.getDistanceTrig();
               telemetry.addData("Horizontal Angle", limeLight.getXAngle());
               telemetry.addData("Vertical Angle", limeLight.getYAngle());
               //telemetry.addData("PoseDistance", limeLight.getDistancePose());
           } else{
               targetTicks = turret.getPosition();
               //targetTicks = turret.degreesToTicks(roadRunner.getEstimatedAngle(target));
           }

           turret.turnTo(targetTicks);

           if (limeLight.getYAngle() > 5) {
               outtake.setHoodAngle(RobotConstants.HOOD_ANGLE.lerp(RobotConstants.CLOSE_LIMIT, RobotConstants.FAR_LIMIT, distance));
           }

           telemetry.addData("Target Ticks", targetTicks);
           telemetry.addData("Position", turret.getPosition());
           telemetry.addData("Sees Tag", limeLight.seesTag());
            telemetry.addData("TrigDistance", distance);
           telemetry.update();
        }
    }
}
