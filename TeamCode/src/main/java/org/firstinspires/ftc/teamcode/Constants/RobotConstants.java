package org.firstinspires.ftc.teamcode.Constants;

public class RobotConstants {

    //Put any constants (unchanging variables) here

    //Target positions
    public static final Target BLUE_GOAL = new Target(0, 0, 144);
    public static final Target RED_GOAL = new Target(1, 144, 144);

    //Turret constants
    public static final double ROTATION_POWER = 1;
    public static final int TICKS_PER_ROTATION = 538;
    public static final double GEAR_RATIO = (double) 51/42; // 17/14

    //Intake Constants
    public static final int OPEN_INTAKE = 1;
    public static final int CLOSED_INTAKE = 0;
    public static final double FORWARDS_INTAKE_POWER = 1;
    public static final double BACKWARDS_INTAKE_POWER = -1;
    // Outtake constants
    public static final double OUTTAKE_POWER = 0.9;
    public static final double OUTTAKE_Velocity = 1000;
    public static final double BOTTOM_ANGLE = 31.22;
    public static final double TOP_ANGLE = 44.07;
    public static final double EXTENDED_SERVO_POSITION = 0.3;

    public static class Target{
        public final int PIPELINE;
        public final int GOAL_X;
        public final int GOAL_Y;

        Target(int pipeline, int goalX, int goalY){
            this.PIPELINE = pipeline;
            this.GOAL_X = goalX;
            this.GOAL_Y = goalY;
        }
    }
}
