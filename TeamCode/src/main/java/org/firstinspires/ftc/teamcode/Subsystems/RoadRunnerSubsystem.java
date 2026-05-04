package org.firstinspires.ftc.teamcode.Subsystems;

import static java.lang.Math.atan2;

import com.acmerobotics.roadrunner.Pose2d;
import org.firstinspires.ftc.teamcode.Constants.RobotConstants;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

public class RoadRunnerSubsystem {

    private MecanumDrive roadRunner;
    private Pose2d pose;

    //Must be initialised before driving subsystem initialisation
    public RoadRunnerSubsystem(HardwareMap hardwareMap) {
        roadRunner = new MecanumDrive(hardwareMap, new Pose2d(0,0, Math.toRadians(180)));
        roadRunner.updatePoseEstimate();
        pose = roadRunner.localizer.getPose();
    }

    public void update(){
        roadRunner.updatePoseEstimate();
        pose = roadRunner.localizer.getPose();
    }

    public double getEstimatedAngle(RobotConstants.Target target){
        return Math.toDegrees(normaliseAngle(atan2((target.GOAL_Y - pose.position.y),(pose.position.x - target.GOAL_X)) + normaliseAngle(Math.toRadians(180) + pose.heading.toDouble())));
    }

    public double getY(){
        return pose.position.y;
    }

    public double getX(){
        return pose.position.x;
    }
    public double getHeading(){
        return pose.heading.toDouble();
    }

    private double normaliseAngle(double angle){
        angle = angle % (2*Math.PI);
        if (angle > Math.PI){
            angle -= (2*Math.PI);
        } else if(angle < -Math.PI){
            angle += (2*Math.PI);
        }
        return angle;
    }
    public double getDistance(){
        return Math.sqrt((getX() * getX()) + (getY() * getY()));
    }
}
