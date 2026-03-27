package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "IDOLOVEMICROWAVES", group = "Linear Opmode")
public class IDOLOVEMICROWAVES extends LinearOpMode {
    private DcMotor armMotor;
    private RevColorSensorV3 colourSensor;
    private int red;
    private int green;
    private int blue;
    private double distance;
    @Override


    public void runOpMode() throws InterruptedException {
        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");
        colourSensor = hardwareMap.get(RevColorSensorV3.class, "colourSensor");
        distance = colourSensor.getDistance(DistanceUnit.CM);
        red = colourSensor.red();
        if (colourSensor.red() <= 150) {
            blue = colourSensor.blue();
            if (colourSensor.blue() <= 150) {
                green = colourSensor.green();
                if (colourSensor.green() <= 150) {
                }
            }
            waitForStart();

            while (opModeIsActive())


                if (colourSensor.red() < 255) {
                    armMotor.setPower(0.5);
                } else if (colourSensor.blue() < 255) {
                    armMotor.setPower(-0.5);
                } else if (colourSensor.green() < 255) {
                } else if (distance < 20) {
                    armMotor.setPower(0);
                }
            if (distance > 20) {
                if (red < green) {
                    armMotor.setPower(0.5);
                }
            }
            telemetry.addData("distance,", distance);
            telemetry.addData("red,", red);
            telemetry.addData("green,", green);
            telemetry.update();
        }
    }

