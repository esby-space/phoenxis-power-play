package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.RobotOld;

import java.util.List;

@Autonomous(name = "Auton: October 16", group = "Auton")
public class Auton extends LinearOpMode {
    RobotOld robotOld = new RobotOld(this);

    @Override
    public void runOpMode() {
        robotOld.init();

        telemetry.addData("start", "hello world :>");
        telemetry.update();

        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0 / 9.0);
        }

        waitForStart();

        robotOld.driveTarget(-25,0, 0, 0.75);
        robotOld.driveTarget(0, 45, 0, 0.75);
        robotOld.driveTarget(35, 0, 0,  0.75);
        robotOld.driveTarget(0, 10, 0, 0.75);

        robotOld.setArm(RobotOld.ArmState.MEDIUM);
        robotOld.setClaw(RobotOld.ClawState.OPEN);

        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("# Objects Detected", updatedRecognitions.size());

            for (Recognition recognition : updatedRecognitions) {
                double x = (recognition.getLeft() + recognition.getRight()) / 2;
                double y = (recognition.getTop() + recognition.getBottom()) / 2;
                double width = Math.abs(recognition.getRight() - recognition.getLeft());
                double height = Math.abs(recognition.getTop() - recognition.getBottom());

                telemetry.addData("", " ");
                telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                telemetry.addData("- Position (Row/Col)", "%.0f / %.0f", x, y);
                telemetry.addData("- Size (Width/Height)", "%.0f / %.0f", width, height);
            }
            telemetry.update();
        }
    }

    private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";
    private static final String[] LABELS = {"1 Bolt", "2 Bulb", "3 Panel"};
    private static final String VUFORIA_KEY = "ARv5q57/////AAABmTua0bpDkkD0sY/XwyoGyhY7kDB+eoToDQJzM/kQLS1NeePnF41Ocp0mM7RcGKFgKDhXo5F25sKyRZYwym/JSPh6KRA67zkDgVHMOSnQkmwrnuigYdNp1/N95q4hZ6uHYcsS6xHb19zs8l4NRXUuKiim4tcSnpDbKVDJrdZb/vrkqJWpjQrWicCzpoG6XOhXuX2YqELGSzVerUJjmYDvjOO7O8poolRJNI2lbpQWQwf4aD3nSnJ004JHLpu/+Is3ICx6bRjn6m5AqpNfbIxH2LbV/0sEgWzuKhmN/9htzeOyj14QmbsWkFY2b5jw6I3T1x7xJaFj08jIU/OrPp57G/dA4UeFR7MKmz2nChvlbscl";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);

        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}

// 14413!

// /\__/\
// (=o.o=)
// |/--\|
// (")-(")
