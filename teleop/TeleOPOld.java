package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "TeleOP: October 2", group = "TeleOP")
public class TeleOPOld extends LinearOpMode {

    @Override
    public void runOpMode() {
        DcMotor frontLeft = hardwareMap.dcMotor.get("frontLeft");
        DcMotor frontRight = hardwareMap.dcMotor.get("frontRight");
        DcMotor backLeft = hardwareMap.dcMotor.get("backLeft");
        DcMotor backRight = hardwareMap.dcMotor.get("backRight");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        CRServo armRotate = hardwareMap.crservo.get("armRotate");
        CRServo armLower = hardwareMap.crservo.get("armLower");
        DcMotor armExtend = hardwareMap.dcMotor.get("armExtend");
        CRServo armGrab = hardwareMap.crservo.get("armGrab");

        armExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // CONFIG
        double mecanumSpeed = 0.7; // linearly proportional!
        double rotationSpeed = 0.8; // linearly proportional!

       waitForStart();
       telemetry.addData("start", "hello world :>");
       telemetry.update();

        while (opModeIsActive()) {
            // Mecanum drive
            double move_y = gamepad1.right_stick_y * mecanumSpeed;
            double move_x = -gamepad1.right_stick_x * mecanumSpeed;
            double move_rotate = -gamepad1.left_stick_x * rotationSpeed;
            double denominator = Math.max(Math.abs(move_y) + Math.abs(move_x) + Math.abs(move_rotate), 1);

            frontLeft.setPower((move_y + move_x + move_rotate) / denominator);
            backLeft.setPower((move_y - move_x + move_rotate) / denominator);
            frontRight.setPower((move_y - move_x - move_rotate) / denominator);
            backRight.setPower((move_y + move_x - move_rotate) / denominator);

            // Arm control
            armExtend.setPower(-gamepad2.left_stick_y);
            telemetry.addData("arm extension position", armExtend.getCurrentPosition());
            telemetry.update();

            armRotate.setPower(-gamepad2.right_stick_x);
            armLower.setPower(gamepad2.right_stick_y);

            if (gamepad2.left_trigger != 0) {
                armGrab.setPower(gamepad2.left_trigger);
            } else if (gamepad2.right_trigger != 0) {
                armGrab.setPower(-gamepad2.right_trigger);
            } else {
                armGrab.setPower(0);
            }
        }
    }
}
