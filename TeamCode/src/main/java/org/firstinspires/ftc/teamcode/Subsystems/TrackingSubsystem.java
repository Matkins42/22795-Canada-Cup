package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class TrackingSubsystem {

    private TurretSubsystem turret;
    private OuttakeSubsystem outtake;
    private LimeLightSubsystem limeLight;
    private RoadRunnerSubsystem roadRunner;

    private double targetTicks = 0;
    private RobotConstants.Target target = RobotConstants.BLUE_GOAL;
    private double distance = 0;

    public TrackingSubsystem(HardwareMap hardwareMap, TurretSubsystem turretSubsystem, OuttakeSubsystem outtakeSubsystem, RobotConstants.Target goal) {
        turret = turretSubsystem;
        outtake = outtakeSubsystem;
        limeLight = new LimeLightSubsystem(hardwareMap);
        roadRunner = new RoadRunnerSubsystem(hardwareMap);
        target = goal;
    }

    public void setTarget(RobotConstants.Target goal){
        target = goal;
        limeLight.switchPipeline(target.PIPELINE);
    }

    public double getDistance(){
        return distance;
    }

    public void fullTracking(){
        roadRunner.update();

        if(limeLight.seesTag()){
            targetTicks = turret.getPosition() + turret.degreesToTicks(limeLight.getXAngle());
            distance = limeLight.getDistanceTrig();
        } else{
            targetTicks = turret.degreesToTicks(roadRunner.getEstimatedAngle(target));
            distance = roadRunner.getDistance();
        }

        turret.turnTo(targetTicks);
    }

    public void llTracking(){
        if(limeLight.seesTag()){
            targetTicks = turret.getPosition() + turret.degreesToTicks(limeLight.getXAngle());
            distance = limeLight.getDistanceTrig();
        } else{
            targetTicks = turret.getPosition();
        }

        turret.turnTo(targetTicks);
    }

    public void adjustOuttake(){
        if (limeLight.getYAngle() > 5) {
            outtake.setHoodAngle(RobotConstants.HOOD_ANGLE.lerp(RobotConstants.CLOSE_LIMIT, RobotConstants.FAR_LIMIT, distance));
            outtake.setVelocity(RobotConstants.OUTTAKE_VELOCITY.lerp(RobotConstants.CLOSE_LIMIT, RobotConstants.FAR_LIMIT, distance));
        }
    }

    public boolean trackingTag(){
        return limeLight.seesTag();
    }
}
