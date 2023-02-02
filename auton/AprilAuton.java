package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.apriltag.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Autonomous(name = "Auton: December 14", group = "Auton")
public class AprilAuton extends LinearOpMode implements RobotConfig {
    Robot robot = new Robot(this);

    @Override
    public void runOpMode() {
        robot.init();
        Camera camera = new Camera(this);

        telemetry.addData("start", "hello world :>");
        telemetry.update();
        waitForStart();

        robot.setClaw(Robot.ClawState.CLOSE);

        int zone = camera.detectZone(3);
        telemetry.addData("detected zone", zone);
        telemetry.update();

        driveTarget(0, 48, 0, 1);
        driveTarget(13, 0, 0, 1);

        robot.setArm(Robot.ArmState.HIGH);

        driveTarget(0, 11, 0, 0.7);
        robot.setClaw(Robot.ClawState.OPEN);
        sleep(100);
        driveTarget(0, -9, 0, 0.7);

        switch (zone) {
            case 1: // zone 1 detected
                driveTarget(12, 0, 0, 1);
                driveTarget(0, -24, 0, 1);
                break;
            case 2: // zone 2 detected
                driveTarget(-12, 0, 0, 1);
                driveTarget(0, -20, 0, 1);
                break;
            case 3: // zone 3 detected
                driveTarget(-39, 0, 0, 1);
                driveTarget(0, -24, 0, 1);
                break;
            default: // problem seeing cone, driving to corner
                driveTarget(-39, 0, 0, 1);
                driveTarget(0, -49, 0, 1);
                break;
        }

        robot.setArm(Robot.ArmState.GROUND);
    }

    // run the motors until it travels a certain distance in inches
    private void driveTarget(double x, double y, double r, double power) {
        y *= -1; // no idea why i need this
        int frontLeftTarget = robot.frontLeft.getCurrentPosition() + (int) ((y + x + r) * COUNTS_PER_INCH);
        int frontRightTarget = robot.frontRight.getCurrentPosition() + (int) ((y - x - r) * COUNTS_PER_INCH);
        int backLeftTarget = robot.backLeft.getCurrentPosition() + (int) ((y - x + r) * COUNTS_PER_INCH);
        int backRightTarget = robot.backRight.getCurrentPosition() + (int) ((y + x - r) * COUNTS_PER_INCH);

        robot.frontLeft.setTargetPosition(frontLeftTarget);
        robot.frontRight.setTargetPosition(frontRightTarget);
        robot.backLeft.setTargetPosition(backLeftTarget);
        robot.backRight.setTargetPosition(backRightTarget);

        robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.drivePower(0, power, 0); // turns on all motors, not necessarily move forward!
        // pause until all motors are done
        while (robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backLeft.isBusy() && robot.backRight.isBusy()) {}

        robot.drivePower(0, 0, 0);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}

class Camera {
    // april tag webcam lenses parameters
    AprilTagDetectionPipeline aprilTag;
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;
    double tagSize = 0.166; // meters

    OpMode opMode;

    public Camera(OpMode mode) {
        opMode = mode;

        String packageName = opMode.hardwareMap.appContext.getPackageName();
        int viewportID = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", packageName);
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(opMode.hardwareMap.get(WebcamName.class, "Webcam 1"), viewportID);
        aprilTag = new AprilTagDetectionPipeline(tagSize, fx, fy, cx, cy);

        camera.setPipeline(aprilTag);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError (int errorCode) {}
        });
    }

    public int detectZone(double delay) {
        double time = opMode.getRuntime() + delay;
        while (opMode.getRuntime() < time) {}

        ArrayList<AprilTagDetection> detections = aprilTag.getLatestDetections();
        if (detections.size() == 0) return 0;
        return aprilTag.getLatestDetections().get(0).id;
    }
}

// 14413!

// /\__/\
// (=o.o=)
// |/--\|
// (")-(")
