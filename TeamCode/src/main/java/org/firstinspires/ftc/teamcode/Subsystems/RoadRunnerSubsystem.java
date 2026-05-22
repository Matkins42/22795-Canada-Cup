package org.firstinspires.ftc.teamcode.Subsystems;

import static java.lang.Math.atan2;

import com.acmerobotics.roadrunner.Pose2d;
import org.firstinspires.ftc.teamcode.Constants.RobotConstants;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

public class RoadRunnerSubsystem {

    private MecanumDrive roadRunner;
    private Pose2d pose;
    private RobotConstants.Target target = RobotConstants.BLUE_GOAL;

    //Must be initialised before driving subsystem initialisation
    public RoadRunnerSubsystem(MecanumDrive drive) {
        roadRunner = drive;
        roadRunner.updatePoseEstimate();
        pose = roadRunner.localizer.getPose();
    }

    public void setPose(Pose2d pose){
        roadRunner.localizer.setPose(pose);
    }

    public void update(){
        roadRunner.updatePoseEstimate();
        pose = roadRunner.localizer.getPose();
    }

    public void setTarget(RobotConstants.Target newTarget){
        target = newTarget;
    }

    public double getEstimatedAngle(){
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
        return 25.4 * Math.sqrt(Math.pow(target.GOAL_Y - pose.position.y, 2) + Math.pow(pose.position.x - target.GOAL_X, 2));
    }
}
