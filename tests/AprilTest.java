package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.apriltag.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Autonomous(name = "April Tag Camera Test", group = "Tests")
public class AprilTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        CameraIDK camera = new CameraIDK(this);
        telemetry.addData("start", "hello world :>");
        telemetry.update();
        waitForStart();

        int zone = camera.detectZone(5);
        telemetry.addData("zone detected", zone);
        telemetry.update();

        while (opModeIsActive()) { sleep(100); }
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
        int cameraMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(opMode.hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
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
