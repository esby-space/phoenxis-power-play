package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotConfig;

@TeleOp(name = "Encoder Test", group = "Tests")
public class EncoderTest extends OpMode implements RobotConfig {
    DcMotor motor = hardwareMap.dcMotor.get("motor");

    @Override
    public void init() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        motor.setPower(gamepad1.left_trigger);

        double position = motor.getCurrentPosition();
        telemetry.addData("position", position);
        telemetry.addData("rotations", position / COUNTS_PER_REV);
    }
}
