package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
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

    public TrackingSubsystem(HardwareMap hardwareMap, RoadRunnerSubsystem roadRunerSubsystem, TurretSubsystem turretSubsystem, OuttakeSubsystem outtakeSubsystem, RobotConstants.Target goal) {
        turret = turretSubsystem;
        outtake = outtakeSubsystem;
        limeLight = new LimeLightSubsystem(hardwareMap);
        roadRunner = roadRunerSubsystem;
        setTarget(goal);
    }

    public void setTarget(RobotConstants.Target goal){
        target = goal;
        limeLight.switchPipeline(target.PIPELINE);
        roadRunner.setTarget(target);
    }

    public double getDistance(){
        return distance;
    }

    public void fullTracking(TelemetryPacket packet){
        roadRunner.update();

        if(limeLight.seesTag()){
            targetTicks = turret.getPosition() + turret.degreesToTicks(limeLight.getXAngle());
            distance = limeLight.getDistanceTrig();
        } else{
            targetTicks = turret.degreesToTicks(roadRunner.getEstimatedAngle());
            distance = roadRunner.getDistance();
        }

        if (packet != null){
            packet.put("limeLight", turret.degreesToTicks(limeLight.getXAngle()));
            packet.put("targetTicks", targetTicks);
        }

        turret.turnTo(targetTicks, packet);
        adjustOuttake();
    }

    public void llTracking(TelemetryPacket packet){
        roadRunner.update();

        if(limeLight.seesTag()){
            targetTicks = turret.getPosition() + turret.degreesToTicks(limeLight.getXAngle());
            distance = limeLight.getDistanceTrig();
        } else{
            targetTicks = turret.getPosition();
        }

        if (packet != null){
            packet.put("limeLight", turret.degreesToTicks(limeLight.getXAngle()));
            packet.put("targetTicks", targetTicks);
        }

        turret.turnTo(targetTicks, packet);
        adjustOuttake();
    }

    public void adjustOuttake(){
        outtake.setHoodAngle(RobotConstants.HOOD_ANGLE.lerp(RobotConstants.HOOD_CLOSE_LIMIT, RobotConstants.HOOD_FAR_LIMIT, distance));
        outtake.setVelocity(RobotConstants.OUTTAKE_VELOCITY.lerp(RobotConstants.VEL_CLOSE_LIMIT, RobotConstants.VEL_FAR_LIMIT, distance));
    }

    public boolean trackingTag(){
        return limeLight.seesTag();
    }

    public double xPos(){
        return roadRunner.getX();
    }
    public double yPos(){
        return roadRunner.getY();
    }
}
