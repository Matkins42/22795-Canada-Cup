package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Template", group = "Linear Opmode") //Change the name here to what you want to show on the driver station
public class CodeTemplate extends LinearOpMode {

    //Put initilization of variables here

    @Override
    public void runOpMode() throws InterruptedException {

        // Code here runs on initilizatiom

        //Insert hardware maps, directions and any other set up code here

        waitForStart();

        //Code here run once when start is pressed

        while(opModeIsActive()){

           //Code here loops after start is pressed, this is where most of your code will go

        }
    }
}
