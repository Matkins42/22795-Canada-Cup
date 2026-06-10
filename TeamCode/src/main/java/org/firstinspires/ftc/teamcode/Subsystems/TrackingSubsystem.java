package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class TrackingSubsystem {
    private TurretSubsystem turret;
    private IntakeSubsystem intake;
    private OuttakeSubsystem outtake;
    private LimeLightSubsystem limeLight;
    private RoadRunnerSubsystem roadRunner;

    private double targetTicks = 0;
    private RobotConstants.Target target = RobotConstants.BLUE_GOAL;
    private double distance = 0;
    private double minDistance = RobotConstants.DISTANCE_NO_CLAMP.MIN;
    private double maxDistance = RobotConstants.DISTANCE_NO_CLAMP.MAX;

    private boolean dynamicScaling = true;

    //private ElapsedTime timer = new ElapsedTime();

    public TrackingSubsystem(HardwareMap hardwareMap, RoadRunnerSubsystem roadRunerSubsystem, TurretSubsystem turretSubsystem, IntakeSubsystem intakeSubsystem, OuttakeSubsystem outtakeSubsystem, RobotConstants.Target goal) {
        turret = turretSubsystem;
        intake = intakeSubsystem;
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

    public void setScaling(boolean scaling){
        dynamicScaling = scaling;
    }

    public double getDistance(){
        return distance;
    }

    public void fullTracking(TelemetryPacket packet){
        roadRunner.update();

        if(limeLight.seesTag()){
            targetTicks = turret.getPosition() + turret.degreesToTicks(limeLight.getXAngle());
            distance = limeLight.getDistanceTrig();
            //timer.reset();
        } else{ //if(timer.seconds() >= RobotConstants.LL_BUFFER_TIME)
            targetTicks = turret.degreesToTicks(roadRunner.getEstimatedAngle());
            distance = roadRunner.getDistance();
        }

        distance = Math.min(maxDistance, Math.max(distance, minDistance));

        if (packet != null){
            packet.put("limeLight", turret.degreesToTicks(limeLight.getXAngle()));
            packet.put("targetTicks", targetTicks);
            packet.put("distance", distance);
            packet.put("targetOuttakeSpeed", outtake.getTargetVelocity());
            packet.put("outtakeSpeed", outtake.getVelocity());
            packet.put("offsetAngle", Math.toDegrees(limeLight.getOffsetAngle()));
            packet.put("tagYaw", limeLight.getTagAngle());
        }

        turret.turnTo(targetTicks, packet);
        if (dynamicScaling) {
            adjustOuttake();
        }
        outtake.update();
    }

    public void llTracking(TelemetryPacket packet){
        roadRunner.update();

        if(limeLight.seesTag()){
            targetTicks = turret.getPosition() + turret.degreesToTicks(limeLight.getXAngle());
            distance = limeLight.getDistanceTrig();
        } else{
            targetTicks = turret.getPosition();
        }

        distance = Math.min(maxDistance, Math.max(distance, minDistance));

        if (packet != null){
            packet.put("limeLight", turret.degreesToTicks(limeLight.getXAngle()));
            packet.put("targetTicks", targetTicks);
        }

        turret.turnTo(targetTicks, packet);
        if (dynamicScaling) {
            adjustOuttake();
        }
        outtake.update();
    }

    public void rrTracking(TelemetryPacket packet){
        roadRunner.update();

        targetTicks = turret.degreesToTicks(roadRunner.getEstimatedAngle());
        distance = roadRunner.getDistance();

        distance = Math.min(maxDistance, Math.max(distance, minDistance));

        if (packet != null){
            packet.put("targetTicks", targetTicks);
        }

        turret.turnTo(targetTicks, packet);
        if (dynamicScaling) {
            adjustOuttake();
        }
        outtake.update();
    }

    public void adjustOuttake(){
        outtake.setHoodAngle(RobotConstants.HOOD_ANGLE.eerp(RobotConstants.HOOD_CLOSE_LIMIT, RobotConstants.HOOD_FAR_LIMIT, distance, RobotConstants.HOOD_GRADIENT));
        outtake.setVelocity(RobotConstants.OUTTAKE_VELOCITY.eerp(RobotConstants.VEL_CLOSE_LIMIT, RobotConstants.VEL_FAR_LIMIT, distance, RobotConstants.VEL_GRADIENT));
        intake.setSpacingTime(RobotConstants.SPACING_TIME.eerp(RobotConstants.VEL_CLOSE_LIMIT, RobotConstants.VEL_FAR_LIMIT, distance, RobotConstants.SPACING_GRADIENT));
    }

    public void setOuttake(double angle, double velocity, double spacing){
        outtake.setHoodAngle(angle);
        outtake.setVelocity(velocity);
        intake.setSpacingTime(spacing);
    }

    public double xPos(){
        return roadRunner.getX();
    }
    public double yPos(){
        return roadRunner.getY();
    }
    public double getPoseDistance(){
        return limeLight.getDistancePose();
    }

    public double getTagAngle(){
        return limeLight.getTagAngle();
    }

    public boolean seesTag(){
        return limeLight.seesTag();
    }

    public void setDistanceClamps(double min, double max){
        minDistance = min;
        maxDistance = max;
    }
}
