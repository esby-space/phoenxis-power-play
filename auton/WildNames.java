package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotOld;
import org.firstinspires.ftc.teamcode.RobotConfig;

@Autonomous(name = "Auton: November 14", group = "Auton")
public class WildNames extends LinearOpMode implements RobotConfig {
    RobotOld robotOld = new RobotOld(this);

    @Override
    public void runOpMode() {
        robotOld.init();

        telemetry.addData("start", "hello world :>");
        telemetry.update();

        waitForStart();

        driveTarget(-25,0, 0, 0.5);
        driveTarget(0, 45, 0, 0.5);
        driveTarget(35, 0, 0,  0.5);
        driveTarget(0, 10, 0, 0.5);

        robotOld.setArm(RobotOld.ArmState.MEDIUM);
    }

    // run the motors until it travels a certain distance in inches
    public void driveTarget(double x, double y, double r, double power) {
        y *= -1; // idk why we need to do this
        int frontLeftTarget = (int) ((y + x + r) * COUNTS_PER_INCH);
        int frontRightTarget = (int) ((y - x  - r) * COUNTS_PER_INCH);
        int backLeftTarget = (int) ((y - x + r) * COUNTS_PER_INCH);
        int backRightTarget = (int) ((y + x - r) * COUNTS_PER_INCH);

        frontLeftTarget += robotOld.frontLeft.getCurrentPosition();
        frontRightTarget += robotOld.frontRight.getCurrentPosition();
        backLeftTarget += robotOld.backLeft.getCurrentPosition();
        backRightTarget += robotOld.backRight.getCurrentPosition();

        robotOld.frontLeft.setTargetPosition(frontLeftTarget);
        robotOld.frontRight.setTargetPosition(frontRightTarget);
        robotOld.backLeft.setTargetPosition(backLeftTarget);
        robotOld.backRight.setTargetPosition(backRightTarget);

        robotOld.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotOld.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotOld.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotOld.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robotOld.frontLeft.setPower(power);
        robotOld.frontRight.setPower(power);
        robotOld.backLeft.setPower(power);
        robotOld.backRight.setPower(power);

        // pause until all motors are done
        while (robotOld.frontLeft.isBusy() && robotOld.frontRight.isBusy() && robotOld.backLeft.isBusy() && robotOld.backRight.isBusy()) {}
        robotOld.frontLeft.setPower(0);
        robotOld.frontRight.setPower(0);
        robotOld.backLeft.setPower(0);
        robotOld.backRight.setPower(0);

        robotOld.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotOld.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotOld.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotOld.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}

// 14413!

// /\__/\
// (=o.o=)
// |/--\|
// (")-(")
