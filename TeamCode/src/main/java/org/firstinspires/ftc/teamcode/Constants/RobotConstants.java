package org.firstinspires.ftc.teamcode.Constants;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;

@Config
public class RobotConstants {

    //Put any constants (unchanging variables) here

    //Field Constants
    public static final Target BLUE_GOAL = new Target(3, -65, -65); //Inches
    public static final Target RED_GOAL = new Target(2, -65, 65); //Inches
    public static final double TAG_HEIGHT = 757;
    public static double TAG_YAW_CLAMP = 2.5;// degrees
    public static final double TARGET_OFFSET = 300; //mm Distance behind the tag we are aiming at
    public static double NORMAL_ANGLE_OFFSET = 2; // degrees off from normal of the tag
    public static final Pose2d RESET_POSE = new Pose2d(61, 0, Math.toRadians(180));


    //Turret constants
    public static double MANUAL_ROTATION_SPEED = 0.5;
    public static double TURRET_RANGE = 360;
    public static final int TICKS_PER_ROTATION = 146; //538 312rpm
    public static final double GEAR_RATIO = (double) 51 / 24;
    public static double KP = 0.0267; //0.004 for 312 motor
    public static double KI = 0.0005; //0.0005 for 312 motor
    public static double KD = 0.0024; //0.0005 for 312 motor
    public static double DEADBAND = 3;
    public static double LL_HEIGHT = 315;
    public static final double LL_ANGLE = 26;


    //Intake constants
    public static final double FORWARDS_INTAKE_POWER = 1;
    public static final double BACKWARDS_INTAKE_POWER = -1;
    public static double FIRING_POWER = 0.8;


    //Outtake constants
    public static Range OUTTAKE_VELOCITY = new Range(1300, 2228); //Absolute max velocity at full power is 2380 (ticks/s)
    public static final Range HOOD_ANGLE = new Range(31.22, 44.07);
    public static final double BOTTOM_ANGLE = 31.22;
    public static final double TOP_ANGLE = 44.07;
    public static final double EXTENDED_SERVO_POSITION = 0.3;
    public static double INCREASE_RATE = 300; //ticks per second per second
    public static double MAX_INCREASE = 600; //ticks per second
    public static final double CLOSE_OUTTAKE_SPEED = 1400;
    public static final double FAR_OUTTAKE_SPEED = 2250;
    public static double VEL_CLOSE_LIMIT = 1050; //mm 750 without POI
    public static double VEL_FAR_LIMIT = 4100; //mm 3800 without POI
    public static double VEL_GRADIENT = 1.33;
    public static double HOOD_CLOSE_LIMIT = 1050; //mm 750 without POI
    public static double HOOD_FAR_LIMIT = 4100; //mm 3800 without POI
    public static double HOOD_GRADIENT = 0.65;


    //Feedback Constants
    public static final int  RUMBLE_DURATION = 1000;
    public static final double[] BLUE = {0, 0, 255};
    public static final double[] RED = {255, 0, 0};
    public static final double[] YELLOW = {255, 255, 0};
    public static final double[] GREEN = {0, 255, 0};
    public static final double[] PURPLE = {255, 0, 255};

    public static class Target {
        public final int PIPELINE;
        public final double GOAL_X;
        public final double GOAL_Y;

        Target(int pipeline, double goalX, double goalY) {
            this.PIPELINE = pipeline;
            this.GOAL_X = goalX;
            this.GOAL_Y = goalY;
        }
    }

    public static class Range {
        public double MIN;
        public double MAX;

        Range(double min, double max) {
            this.MIN = min;
            this.MAX = max;
        }
        public double lerp(double min, double max, double t){
            t = Math.max(min, Math.min(max, t));
            return ((t-min)/(max-min) * (this.MAX - this.MIN)) + this.MIN;
        }

        public double eerp(double min, double max, double t, double e){
            t = Math.max(min, Math.min(max, t));
            return ((this.MAX-this.MIN)/Math.pow((max-min), e)) * Math.pow(t-min, e) + this.MIN;
        }
    }
}
