package org.firstinspires.ftc.teamcode.Constants;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConstants {

    //Put any constants (unchanging variables) here

    //Target positions
    public static final Target BLUE_GOAL = new Target(1, 0, 144);
    public static final Target RED_GOAL = new Target(0, 144, 144);
    public static final double TAG_HEIGHT = 757;
    public static double CLOSE_LIMIT = 750; //mm
    public static double FAR_LIMIT = 2000; //mm

    //Turret constants
    public static double ROTATION_POWER = 0.2;
    public static final int TICKS_PER_ROTATION = 146;
    public static final double GEAR_RATIO = (double) 51 / 24;
    public static double KP = 0.03;
    public static double KI = 0.0005;
    public static double KD = 12;
    public static double DEADBAND = 3;
    public static double LL_HEIGHT = 315;
    public static final double LL_ANGLE = 26;

    //Intake constants
    public static final int OPEN_INTAKE = 1;
    public static final int CLOSED_INTAKE = 0;
    public static final double FORWARDS_INTAKE_POWER = 1;
    public static final double BACKWARDS_INTAKE_POWER = -1;

    //Outtake constants
    public static final double OUTTAKE_POWER = 0.9;
    public static Range OUTTAKE_VELOCITY = new Range(1000, 2000); //Absolute max velocity at full power is 2380 (ticks/s)
    public static final Range HOOD_ANGLE = new Range(31.22, 44.07);
    public static final double BOTTOM_ANGLE = 31.22;
    public static final double TOP_ANGLE = 44.07;
    public static final double EXTENDED_SERVO_POSITION = 0.3;

    //Feedback Constants
    public static final int  RUMBLE_DURATION = 1000;
    public static final double[] RED = {255, 0, 0};
    public static final double[] YELLOW = {255, 255, 0};
    public static final double[] GREEN = {0, 255, 0};

    public static class Target {
        public final int PIPELINE;
        public final int GOAL_X;
        public final int GOAL_Y;

        Target(int pipeline, int goalX, int goalY) {
            this.PIPELINE = pipeline;
            this.GOAL_X = goalX;
            this.GOAL_Y = goalY;
        }
    }

    public static class Range {
        public final double MIN;
        public final double MAX;

        Range(double min, double max) {
            this.MIN = min;
            this.MAX = max;
        }
        public double lerp(double min, double max, double t){
            t = Math.max(min, Math.min(max, t));
            return ((t-min)/(max-min) * (this.MAX - this.MIN)) + this.MIN;
        }
    }
}
