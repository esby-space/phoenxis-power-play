package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot implements RobotConfig {
    private final OpMode opMode;

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public DcMotor armExtend;
    public Servo claw;

    public Robot(OpMode mode) {
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
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(r), 1);

        frontLeft.setPower((y + x + r) / denominator);
        frontRight.setPower((y - x - r) / denominator);
        backLeft.setPower((y - x + r) / denominator);
        backRight.setPower((y + x - r) / denominator);
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
                extendArm(1.5);
                break;
            case LOW:
                extendArm(10);
                break;
            case MEDIUM:
                extendArm(30);
                break;
            case HIGH:
                extendArm(46);
                break;
        }
    }

    public void extendArm(double target) {
        target *= -ARM_COUNTS_PER_INCH;

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

    public ClawState currentClawState = ClawState.CLOSE;

    public void setClaw(ClawState state) {
        currentClawState = state;
        switch (state) {
            case CLOSE:
                claw.setPosition(0.1);
                break;
            case OPEN:
                claw.setPosition(1.0);
                break;
        }
    }

    public void toggleClaw() {
        switch (currentClawState) {
            case CLOSE:
                setClaw(ClawState.OPEN);
                break;
            case OPEN:
                setClaw(ClawState.CLOSE);
                break;
        }
    }
}

// 14413!

// /\__/\
// (=o.o=)
// |/--\|
// (")-(")
