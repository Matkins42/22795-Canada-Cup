package org.firstinspires.ftc.teamcode.Storage;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.robotcontroller.external.samples.RobotAutoDriveByEncoder_Linear;
import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class AutoStorage {
    public static Pose2d autoEndPose = new Pose2d(0, 0, Math.toRadians(180));
    public static RobotConstants.Target goal = RobotConstants.BLUE_GOAL;
    public static boolean auto = false;

    public static void reset(){
        autoEndPose = new Pose2d(0, 0, Math.toRadians(180));
        goal = RobotConstants.BLUE_GOAL;
        auto = false;
    }
}
