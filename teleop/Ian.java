package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.RobotConfig;

// ask ian
@TeleOp(name = "IAN MODE", group = "TeleOP")
public class Ian extends LinearOpMode implements RobotConfig {
    Robot robot = new Robot(this);

    boolean risingA = false;
    boolean risingB = false;
    boolean risingX = false;
    boolean risingY = false;

    boolean risingLeft = false;

    @Override
    public void runOpMode() {
        robot.init();
        telemetry.addData("start", "hello world :>");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // drive train
            double x = gamepad1.left_stick_x * (1 - gamepad1.left_trigger);
            double y = -gamepad1.left_stick_y * (1 - gamepad1.left_trigger);
            double r = gamepad1.right_stick_x * (1 - gamepad1.left_trigger);
            robot.drivePower(x, y, r);

            // arm control
            if (gamepad1.a && !risingA) {
                robot.setArm(Robot.ArmState.GROUND);
            } else if (gamepad1.b && !risingB) {
                robot.setArm(Robot.ArmState.HIGH);
            } else if (gamepad1.x && !risingX) {
                robot.setArm(Robot.ArmState.LOW);
            } else if (gamepad1.y && !risingY) {
                robot.setArm(Robot.ArmState.MEDIUM);
            } else {
                robot.armExtend.setPower(gamepad1.right_stick_y / 2);
            }

            telemetry.addData("arm position", robot.armExtend.getCurrentPosition());
            telemetry.addData("arm inches", robot.armExtend.getCurrentPosition() / ARM_COUNTS_PER_INCH);
            telemetry.addData("arm power", robot.armExtend.getPower());
            telemetry.update();

            risingA = gamepad1.a;
            risingB = gamepad1.b;
            risingX = gamepad1.x;
            risingY = gamepad1.y;

            // claw control
            if (gamepad1.left_bumper && !risingLeft) {
                robot.toggleClaw();
            }

            risingLeft = gamepad1.left_bumper;
        }
    }
}

// 14413!

// /\__/\
// (=o.o=)
// |/--\|
// (")-(")
