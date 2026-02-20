import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name="bob", group ="Linear Opmode")
 public class bob extends LinearOpMode {
    private DcMotor intakeMotor;

    @Override
    public void runOpMode() {
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        waitForStart();
        while (opModeIsActive()) {;
            if (gamepad1.a) {
                intakeMotor.setPower(0.5);
            } else if (gamepad1.b) {
                intakeMotor.setPower(-0.5);
            } else {
                intakeMotor.setPower(0.0);
            }
        }
    }
}