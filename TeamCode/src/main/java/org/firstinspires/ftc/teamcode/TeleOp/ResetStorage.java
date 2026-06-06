package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Storage.AutoStorage;

@TeleOp(name = "ResetStorage", group = "Linear Opmode") //Change the name here to what you want to show on the driver station
public class ResetStorage extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        AutoStorage.reset();
    }
}
