package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp (name="Ilikefridges", group ="Linear Opmode")
public class Ilikefridges extends LinearOpMode {


    private DcMotor armMotor;
    private RevColorSensorV3 ColourSensor;
    private double distance;
    private int red;
    private int green;
    private int blue;

    @Override

    public void runOpMode() throws InterruptedException {
        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");
        ColourSensor = hardwareMap.get(RevColorSensorV3.class, "ColourSensor");

        waitForStart();

        while (opModeIsActive()) {

            red = ColourSensor.red();

            blue = ColourSensor.blue();

            green = ColourSensor.green();

            distance = (ColourSensor.getDistance(DistanceUnit.CM));

            if (distance <10) {
                if (red*2 < green) {
                    armMotor.setPower(0.5);
                } else  {
                    armMotor.setPower(-0.5);
                }
            }else{
                armMotor.setPower(0);
            }
            telemetry.addData("green", green);
            telemetry.addData("red", red);
            telemetry.addData("distance", distance);

            telemetry.update();
        }
    }
}