import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name="turtle", group ="Linear Opmode")
public class turtle extends LinearOpMode {
    private DcMotor spinnyMotor;
    private double power = 0.5;
    private int direction = 1;
    private boolean motorOn = false;

    @Override
    public void runOpMode() {

        // 1️⃣ Connect motor from configuration
        spinnyMotor = hardwareMap.get(DcMotor.class, "spinnyMotor");
        waitForStart();
        while (opModeIsActive()) {

            motorOn = getMotor(motorOn);

            if (motorOn) {
                direction = getDirectionInput(direction);
                power = getPowerInput(power);
            } else {
                power = 0;
            }


            spinnyMotor.setPower(power * direction);

            telemetry.addData("Power:", power);
            telemetry.addData("Direction:", direction);

            telemetry.update();

        }
    }

    private int getDirectionInput(int currentDirection) {
        if (gamepad1.dpad_down) {
            return -1;
        } else if (gamepad1.dpad_up) {
            return 1;
        } else {
            return direction;
        }
    }

    private double getPowerInput(double currentPower) {
        if (gamepad1.a) {
            return 1.0;
        } else if (gamepad1.b) {
            return 0.75;
        } else if (gamepad1.y) {
            return 0.1;
        } else {
            return currentPower;
        }
    }

    private boolean getMotor(boolean currentState) {
        if (gamepad1.left_bumper) {
            return true;
        } else if (gamepad1.right_bumper) {
            return false;
        } else{
            return currentState;
        }
    }
}

