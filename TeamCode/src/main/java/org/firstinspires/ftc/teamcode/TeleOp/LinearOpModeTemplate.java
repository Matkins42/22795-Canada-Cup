package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@Disabled //REMOVE THIS LINE - it makes it so it doesn't show up on the driver station
@TeleOp(name = "Template", group = "Linear Opmode") //Change the name here to what you want to show on the driver station
public class LinearOpModeTemplate extends LinearOpMode {

    //Put initialization of variables here

    @Override
    public void runOpMode() throws InterruptedException {

        // Code here runs on initialization

        //Insert hardware maps, directions and any other set up code here

        waitForStart();

        //Code here run once when start is pressed

        while(opModeIsActive()){

           //Code here loops after start is pressed, this is where most of your code will go

        }
    }
}
