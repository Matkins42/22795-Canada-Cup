package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp(name="sensortest", group ="Linear Opmode")
 public class sensortest extends LinearOpMode {
    private RevColorSensorV3 revy;
    private DcMotor spinMotor;
    private double distance;

    private int red;
    private int green;
    private int blue;

    @Override
    public void runOpMode() {
        revy = hardwareMap.get(RevColorSensorV3.class,"Revy");
        spinMotor = hardwareMap.get(DcMotor.class, "spinMotor");
        waitForStart();
        while (opModeIsActive()) {;

            red = revy.red();
            blue = revy.blue();
            green = revy.green();
            distance = (revy.getDistance(DistanceUnit.CM));

            if (distance<=20) {
                if (red > green) {
                    spinMotor.setPower(1);
                } else {
                    spinMotor.setPower(-1);
                }
                spinMotor.setPower(0);



            }  telemetry.addData("Red:", red);
            telemetry.addData("Green:", green);\
            telemetry.addData("Distance:", distance);


            telemetry.update();
        }
    }
}
