package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotOld;
import org.firstinspires.ftc.teamcode.RobotConfig;

@TeleOp(name = "TeleOP: November 9", group = "TeleOP")
public class New extends LinearOpMode implements RobotConfig {
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
                setArm(ArmState.GROUND);
            } else if (gamepad2.b && !risingB) {
                setArm(ArmState.HIGH);
            } else if (gamepad2.x && !risingX) {
                setArm(ArmState.LOW);
            } else if (gamepad2.y && !risingY) {
                setArm(ArmState.MEDIUM);
            }

            robotOld.armExtend.setPower(gamepad2.left_stick_y);

            risingA = gamepad2.a;
            risingB = gamepad2.b;
            risingX = gamepad2.x;
            risingY = gamepad2.y;

            telemetry.addData("claw position", robotOld.claw.getPosition());
            telemetry.update();
            if (gamepad2.left_bumper && !risingLeft) {
                robotOld.claw.setPosition(0.8);
            } else if (gamepad2.right_bumper && !risingRight) {
                robotOld.claw.setPosition(0);
            }

            risingLeft = gamepad2.left_bumper;
            risingRight = gamepad2.right_bumper;
        }
    }

    public void armExtend(double target) {
        target *= ARM_COUNTS_PER_INCH;

        robotOld.armExtend.setTargetPosition((int) target);
        robotOld.armExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotOld.armExtend.setPower(ARM_SPEED);

        while (robotOld.armExtend.isBusy()) {}
        robotOld.armExtend.setPower(0);
        robotOld.armExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public enum ArmState {
        GROUND,
        LOW,
        MEDIUM,
        HIGH
    }

    public void setArm(ArmState state) {
        switch (state) {
            case GROUND:
                armExtend(0);
                break;
            case LOW:
                armExtend(10.0);
                break;
            case MEDIUM:
                armExtend(20.5);
                break;
            case HIGH:
                armExtend(25.0);
                break;
        }
    }
}
