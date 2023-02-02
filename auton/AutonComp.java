package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.RobotConfig;

import java.util.List;

@Autonomous(name = "Auton: November 30", group = "Auton")
public class AutonComp extends LinearOpMode implements RobotConfig {
    Robot robot = new Robot(this);

    @Override
    public void runOpMode() {
        robot.init();

        telemetry.addData("start", "hello world :>");
        telemetry.update();

        initCamera();
        waitForStart();

        robot.setClaw(Robot.ClawState.CLOSE);
        int zone = sleeveZone(0);
        driveTarget(0, 25, 0, 0.7);
        driveTarget(12, 0, 0, 0.7);

//        robot.setArm(Robot.ArmState.MEDIUM);
        driveTarget(0, 5, 0, 0.5);
        robot.setClaw(Robot.ClawState.OPEN);
        driveTarget(0, -5, 0, 0.5);
//        robot.setArm(Robot.ArmState.GROUND);

        switch (zone) {
            case 0:
                driveTarget(-37, 0, 0, 0.7);
                driveTarget(0, -25, 0, 0.7);
                break;
            case 1:
                driveTarget(12, 0, 0, 0.7);
                break;
            case 2:
                driveTarget(-12, 0, 0, 0.7);
                break;
            case 3:
                driveTarget(-24, 0, 0, 0.7);
                break;
        }
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
        while (robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backLeft.isBusy() && robot.backRight.isBusy()) {
            telemetry.addData("Running to", "%4d : %4d : %4d : %4d", frontLeftTarget, frontRightTarget, backLeftTarget, backRightTarget);
            telemetry.addData("Currently at", "%4d : %4d : %4d : %4d", robot.frontLeft.getCurrentPosition(), robot.frontRight.getCurrentPosition(), robot.backLeft.getCurrentPosition(), robot.backRight.getCurrentPosition());
            telemetry.update();
        }

        robot.drivePower(0, 0, 0);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";
    private static final String[] LABELS = {"1 Bolt", "2 Bulb", "3 Panel"};
    private static final String VUFORIA_KEY = "ARv5q57/////AAABmTua0bpDkkD0sY/XwyoGyhY7kDB+eoToDQJzM/kQLS1NeePnF41Ocp0mM7RcGKFgKDhXo5F25sKyRZYwym/JSPh6KRA67zkDgVHMOSnQkmwrnuigYdNp1/N95q4hZ6uHYcsS6xHb19zs8l4NRXUuKiim4tcSnpDbKVDJrdZb/vrkqJWpjQrWicCzpoG6XOhXuX2YqELGSzVerUJjmYDvjOO7O8poolRJNI2lbpQWQwf4aD3nSnJ004JHLpu/+Is3ICx6bRjn6m5AqpNfbIxH2LbV/0sEgWzuKhmN/9htzeOyj14QmbsWkFY2b5jw6I3T1x7xJaFj08jIU/OrPp57G/dA4UeFR7MKmz2nChvlbscl";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private void initCamera() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);

        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0 / 9.0);
        }
    }

    private int sleeveZone(long delay) {
        ElapsedTime timer = new ElapsedTime();

        List<Recognition> recognitions = tfod.getUpdatedRecognitions();
        if (recognitions == null) return 0;

        telemetry.addData("# Objects Detected", recognitions.size());

        float maxConfidence = 0;
        int currentZone = 0;

        while (timer.seconds() < delay) {
            for (int i = 0; i < recognitions.size(); i++) {
                Recognition recognition = recognitions.get(i);

                double x = (recognition.getLeft() + recognition.getRight()) / 2;
                double y = (recognition.getTop() + recognition.getBottom()) / 2;
                double width = Math.abs(recognition.getRight() - recognition.getLeft());
                double height = Math.abs(recognition.getTop() - recognition.getBottom());

                telemetry.addData("", " ");
                telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                telemetry.addData("- Position (Row/Col)", "%.0f / %.0f", x, y);
                telemetry.addData("- Size (Width/Height)", "%.0f / %.0f", width, height);
                telemetry.update();

                if (recognition.getConfidence() > maxConfidence) {
                    maxConfidence = recognition.getConfidence();
                    currentZone = Character.getNumericValue(recognition.getLabel().charAt(0));
                }
            }
        }

        return currentZone;
    }
}

// 14413!

// /\__/\
// (=o.o=)
// |/--\|
// (")-(")
