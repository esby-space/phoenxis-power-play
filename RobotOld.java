package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class RobotOld implements RobotConfig {
    private final OpMode opMode;

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public DcMotor armExtend;
    public Servo claw;

    public RobotOld(OpMode mode) {
        opMode = mode;
    }

    // get all the motors and servos ready to go!
    public void init() {
        // driving wheels
        frontLeft = opMode.hardwareMap.dcMotor.get("frontLeft");
        frontRight = opMode.hardwareMap.dcMotor.get("frontRight");
        backLeft = opMode.hardwareMap.dcMotor.get("backLeft");
        backRight = opMode.hardwareMap.dcMotor.get("backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // reset the encoders but allow motors to spin
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // arm motors and servos
        armExtend = opMode.hardwareMap.dcMotor.get("armExtend");
        armExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        claw = opMode.hardwareMap.servo.get("claw");
    }

    // set the drive motors to a certain power
    public void drivePower(double x, double y, double r) {
        x *= -MECANUM_SPEED;
        y *= -MECANUM_SPEED;
        r *= -ROTATION_SPEED;
        DriveTargets targets = new DriveTargets(x, y, r, true);

        frontLeft.setPower(targets.frontLeft);
        frontRight.setPower(targets.frontRight);
        backLeft.setPower(targets.backLeft);
        backRight.setPower(targets.backRight);
    }

    // run the motors for a specified amount of time
    private final ElapsedTime driveRuntime = new ElapsedTime();

    public void driveTime(double x, double y, double r, double seconds) {
        drivePower(x, y, r);

        driveRuntime.reset();
        while (driveRuntime.seconds() < seconds) {
            opMode.telemetry.addData("Auton", "moving");
            opMode.telemetry.update();
        }

        drivePower(0, 0, 0);
    }

    // run the motors until it travels a certain distance in inches
    public void driveTarget(double x, double y, double r, double power) {
        DriveTargets targets = new DriveTargets(x, -y, r, false);
        int frontLeftTarget = frontLeft.getCurrentPosition() + (int) (targets.frontLeft * COUNTS_PER_INCH);
        int frontRightTarget = frontRight.getCurrentPosition() + (int) (targets.frontRight * COUNTS_PER_INCH);
        int backLeftTarget = backLeft.getCurrentPosition() + (int) (targets.backLeft * COUNTS_PER_INCH);
        int backRightTarget = backRight.getCurrentPosition() + (int) (targets.backRight * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(frontLeftTarget);
        frontRight.setTargetPosition(frontRightTarget);
        backLeft.setTargetPosition(backLeftTarget);
        backRight.setTargetPosition(backRightTarget);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        drivePower(0, power, 0); // turns on all motors, not necessarily move forward!
        // pause until all motors are done
        while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
            opMode.telemetry.addData("Running to", "%4d : %4d : %4d : %4d", frontLeftTarget, frontRightTarget, backLeftTarget, backRightTarget);
            opMode.telemetry.addData("Currently at", "%4d : %4d : %4d : %4d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(), backLeft.getCurrentPosition(), backRight.getCurrentPosition());
            opMode.telemetry.update();
        }

        drivePower(0, 0, 0);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // arm positions
    public enum ArmState {
        GROUND,
        LOW,
        MEDIUM,
        HIGH
    }

    public void setArm(ArmState state) {
        switch (state) {
            case GROUND:
                extendArm(3);
                break;
            case LOW:
                extendArm(10.0);
                break;
            case MEDIUM:
                extendArm(20.5);
                break;
            case HIGH:
                extendArm(25.0);
                break;
        }
    }

    public void extendArm(double target) {
        target *= ARM_COUNTS_PER_INCH;

        armExtend.setTargetPosition((int) target);
        armExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armExtend.setPower(ARM_SPEED);

        while (armExtend.isBusy()) {}

        armExtend.setPower(0);
        armExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // claw position
    public enum ClawState {
        CLOSE,
        OPEN
    }

    public void setClaw(ClawState state) {
        switch (state) {
            case CLOSE:
                claw.setPosition(0.0);
                break;
            case OPEN:
                claw.setPosition(1.0);
                break;
        }
    }
}

class DriveTargets {
    double frontLeft;
    double frontRight;
    double backLeft;
    double backRight;

    // mecanum math!
    // turns x, y, and z directional distances into values for individual motors
    public DriveTargets(double x, double y, double r, Boolean normalize) {
        double denominator = normalize ? Math.max(Math.abs(y) + Math.abs(x) + Math.abs(r), 1) : 1;
        frontLeft = (y + x + r) / denominator;
        backLeft = (y - x + r) / denominator;
        frontRight = (y - x - r) / denominator;
        backRight = (y + x - r) / denominator;
    }
}

// 14413!

// /\__/\
// (=o.o=)
// |/--\|
// (")-(")
