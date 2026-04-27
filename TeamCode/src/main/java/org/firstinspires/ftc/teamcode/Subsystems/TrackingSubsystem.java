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

    public void update(){
        track();
        adjustOuttake();
    }

    public void track(){
        roadRunner.update();

        //Tracks tag
        if(limeLight.seesTag()){
            targetTicks = turret.getPosition() + turret.degreesToTicks(limeLight.getXAngle());
            distance = limeLight.getDistanceTrig();
        } else{
            targetTicks = turret.getPosition();
//            targetTicks = turret.degreesToTicks(roadRunner.getEstimatedAngle(target));
//            distance = roadRunner.getDistance();
        }

        turret.turnTo(targetTicks);
    }

    public void adjustOuttake(){
        //Adjusts hood based on distance
        if (limeLight.getYAngle() > 5) {
            outtake.setHoodAngle(RobotConstants.HOOD_ANGLE.lerp(RobotConstants.CLOSE_LIMIT, RobotConstants.FAR_LIMIT, distance));
        }
        //Put speed change
    }

}
