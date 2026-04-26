package org.firstinspires.ftc.teamcode.Subsystems;

import static java.lang.Math.atan2;

import com.acmerobotics.roadrunner.Pose2d;
import org.firstinspires.ftc.teamcode.Constants.RobotConstants;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

public class RoadRunnerSubsystem {

    private MecanumDrive roadRunner;
    private Pose2d pose;

    public RoadRunnerSubsystem(HardwareMap hardwareMap) {
        roadRunner = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
    }

    public void update(){
        roadRunner.updatePoseEstimate();
        pose = roadRunner.localizer.getPose();
    }

    public double getEstimatedAngle(RobotConstants.Target target){
        return Math.toDegrees(atan2((target.GOAL_X - pose.position.x),(target.GOAL_Y - pose.position.y)) - pose.heading.toDouble());
    }

    public double getY(){
        return -1 * pose.position.y;
    }

    public double getX(){
        return pose.position.x;
    }
    public double getHeading(){
        return -1 * pose.heading.toDouble();
    }
    public double getDistance(){
        return Math.sqrt((getX() * getX()) + (getY() * getY()));
    }
}
