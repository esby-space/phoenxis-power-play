package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotOld;
import org.firstinspires.ftc.teamcode.RobotConfig;

@TeleOp(name = "TeleOP: November 12", group = "TeleOP")
public class TeleOPNew extends LinearOpMode implements RobotConfig {
    RobotOld robotOld = new RobotOld(this);

    boolean risingA = false;
    boolean risingB = false;
    boolean risingX = false;
    boolean risingY = false;

    boolean risingLeft = false;
    boolean risingRight = false;

    @Override
    public void runOpMode() {
        robotOld.init();
        telemetry.addData("start", "hello world :>");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {
            double x = -gamepad1.left_stick_x * MECANUM_SPEED;
            double y = gamepad1.left_stick_y * MECANUM_SPEED;
            double r = -gamepad1.right_stick_x * ROTATION_SPEED;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(r), 1);
            robotOld.frontLeft.setPower((y + x + r) / denominator);
            robotOld.frontRight.setPower((y - x - r) / denominator);
            robotOld.backLeft.setPower((y - x + r) / denominator);
            robotOld.backRight.setPower((y + x - r) / denominator);

            if (gamepad2.a && !risingA) {
                robotOld.setArm(RobotOld.ArmState.GROUND);
            } else if (gamepad2.b && !risingB) {
                robotOld.setArm(RobotOld.ArmState.HIGH);
            } else if (gamepad2.x && !risingX) {
                robotOld.setArm(RobotOld.ArmState.LOW);
            } else if (gamepad2.y && !risingY) {
                robotOld.setArm(RobotOld.ArmState.MEDIUM);
            }

            robotOld.armExtend.setPower(gamepad2.left_stick_y);
            telemetry.addData("arm position", robotOld.armExtend.getCurrentPosition());
            telemetry.addData("arm inches", robotOld.armExtend.getCurrentPosition() / ARM_COUNTS_PER_INCH);
            telemetry.update();

            risingA = gamepad2.a;
            risingB = gamepad2.b;
            risingX = gamepad2.x;
            risingY = gamepad2.y;

            if (gamepad2.left_bumper && !risingLeft) {
                robotOld.setClaw(RobotOld.ClawState.CLOSE);
            } else if (gamepad2.right_bumper && !risingRight) {
                robotOld.setClaw(RobotOld.ClawState.OPEN);
            }

            risingLeft = gamepad2.left_bumper;
            risingRight = gamepad2.right_bumper;
        }
    }
}
