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
    public static final double TAG_YAW_CLAMP = 2.5;// degrees
    public static final double TARGET_OFFSET = 300; //mm Distance behind the tag we are aiming at
    public static final double NORMAL_ANGLE_OFFSET = 2; // degrees off from normal of the tag
    public static final Pose2d RESET_POSE = new Pose2d(61, 0, Math.toRadians(180));
    public static final Range DISTANCE_CLOSE_CLAMP = new Range(0, 1800);
    public static final Range DISTANCE_MEDIUM_CLAMP = new Range(1450, 2900);
    public static final Range DISTANCE_FAR_CLAMP = new Range(3200, 5000);
    public static final Range DISTANCE_NO_CLAMP = new Range(0, 5000);
    public static final double RR_DISTANCE_OFFSET = 200;

    //Driving constants
    public static double DRIVING_SPEED = 0.9;

    //Turret constants
    public static double MANUAL_ROTATION_SPEED = 0.5;
    public static double TURRET_RANGE = 340;
    public static final int TICKS_PER_ROTATION = 146; //538 312rpm
    public static final double GEAR_RATIO = (double) 51 / 24;
    public static double KP = 0.0268; //0.004 for 312 motor
    public static double KI = 0.0005; //0.0005 for 312 motor
    public static double KD = 0.0029; //0.0005 for 312 motor
    public static final double MAX_I = 0.2;
    public static double DEADBAND = 5;
    public static final double LL_HEIGHT = 315;
    public static final double LL_ANGLE = 26;
    //public static double LL_BUFFER_TIME = 0.01;


    //Intake constants
    public static final double FORWARDS_INTAKE_POWER = 1;
    public static final double BACKWARDS_INTAKE_POWER = -1;
    public static double FRONT_FIRING_POWER = 0.85;
    public static double BACK_FIRING_POWER_1 = 0.75;
    public static double BACK_FIRING_POWER_2 = -0.75;
    public static double SHOOTING_STATE_1_TIME = 0.1;
    public static double SHOOTING_STATE_2_MIN_TIME = 0.3; //0.4 for short, 0.65 for long, need to do dynamic
    public static Range SPACING_TIME = new Range(0.375, 0.62);
    public static double SPACING_GRADIENT = 0.82;
    public static boolean SET_SPACING_TIME = true;

    //Outtake constants
    public static Range OUTTAKE_VELOCITY = new Range(1295, 2210); //Absolute max velocity at full power is 2380 (ticks/s)  Old values (1300, 2228)
    public static final Range HOOD_ANGLE = new Range(31.22, 44.07);
    public static final double EXTENDED_SERVO_POSITION = 0.3;
    public static double HOOD_DAMPENING = 0.01;
    public static double INCREASE_RATE = 300; //ticks per second per second
    public static double MAX_INCREASE = 0; //ticks per second
    public static final double CLOSE_OUTTAKE_SPEED = 1400;
    public static final double FAR_OUTTAKE_SPEED = 2250;
    public static double VEL_CLOSE_LIMIT = 1050; //mm 750 without POI
    public static double VEL_FAR_LIMIT = 3800; //mm 3800 without POI
    public static double VEL_GRADIENT = 1.26; //was 1.33
    public static double HOOD_CLOSE_LIMIT = 1050; //mm 750 without POI
    public static double HOOD_FAR_LIMIT = 3800; //mm 3800 without POI
    public static double HOOD_GRADIENT = 0.63;
    public static double SPEED_TARGET_RANGE = 40;


    //Feedback Constants
    public static final int  RUMBLE_DURATION = 1000;
    public static final double[] BLUE = {0, 0, 255};
    public static final double[] RED = {255, 0, 0};
    public static final double[] YELLOW = {255, 255, 0};
    public static final double[] GREEN = {0, 255, 0};
    public static final double[] PURPLE = {255, 0, 255};
    public static final double[] PINK = {255, 105, 180};
    public static final double[] ORANGE = {255, 165, 0};
    public static final double[] LIGHT_BLUE = {173, 216, 230};

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
        public double lerp(double min, double max, double t){ // Max must be larger than min
            t = Math.max(min, Math.min(max, t));
            return ((t-min)/(max-min) * (this.MAX - this.MIN)) + this.MIN;
        }

        public double eerp(double min, double max, double t, double e){ // Max must be larger than min
            t = Math.max(min, Math.min(max, t));
            return ((this.MAX-this.MIN)/Math.pow((max-min), e)) * Math.pow(t-min, e) + this.MIN;
        }
    }
}
