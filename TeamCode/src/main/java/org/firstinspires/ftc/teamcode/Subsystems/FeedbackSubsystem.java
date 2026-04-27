package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class FeedbackSubsystem {

    public void rumble(Gamepad gamepad){
        gamepad.rumble(RobotConstants.RUMBLE_DURATION);
    }

    public void setLight(Gamepad gamepad, double[] color){
        gamepad.setLedColor(color[0], color[1], color[2], Gamepad.LED_DURATION_CONTINUOUS);
    }
}
