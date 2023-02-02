package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotOld;

@TeleOp(name = "TeleOP: October 16", group = "TeleOP")
public class TeleOP extends LinearOpMode {
    RobotOld robotOld = new RobotOld(this);
//    Gamepad gamepad2 = gamepad1; // uncomment for IAN MODE

    boolean risingA = false;
    boolean risingB = false;
    boolean risingX = false;
    boolean risingY = false;

    @Override
    public void runOpMode() {
        robotOld.init();
        telemetry.addData("start", "hello world :>");
        telemetry.update();


        while (opModeIsActive()) {
//             drive control
//            double x = gamepad1.left_stick_x;
//            double y = -gamepad1.left_stick_y;
//            double r = gamepad1.right_stick_x;
//            robot.drivePower(x, y, r);

            // arm control
            if (gamepad2.a && !risingA) {
                robotOld.setArm(RobotOld.ArmState.GROUND);
            } else if (gamepad2.b && !risingB) {
                robotOld.setArm(RobotOld.ArmState.HIGH);
            } else if (gamepad2.x && !risingX) {
                robotOld.setArm(RobotOld.ArmState.LOW);
            } else if (gamepad2.y && !risingY) {
                robotOld.setArm(RobotOld.ArmState.MEDIUM);
            }

            risingA = gamepad2.a;
            risingB = gamepad2.b;
            risingX = gamepad2.x;
            risingY = gamepad2.y;
        }
    }
}

// 14413!

// /\__/\
// (=o.o=)
// |/--\|
// (")-(")
