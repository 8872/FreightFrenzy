package org.firstinspires.ftc.teamcode.freightfrenzy;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous
public class CameraDropOffFreight extends AutonomousOpMode {


    private static final String VUFORIA_KEY =
        "AR1EGWL/////AAABmao6fwhlA0nZgC4AC92PSFIkRoulXKGjKgy0eFqp2+gwuiWL9ULzw2QJD/Jr7os9Xby/GjZHBwwPW3P6vvVfidwd556TIQRTX6NzaGOooiLjLWebMMHcEJdvLD+4VdbHvZaEiXlH4O/Vb+Rqqo+PS5LUE9LQxnYtSYvbtWDVz757S56MSByBrH7Zt7zTFu0a3Rlvr7s7o9wGR74qQ1jI/vIuWWUIWXPUXCb9L+TVqMPFk0yOumhdyUhmTf8JXBPOWnppwXKJ7049tnegzoc6Ov+IuIu7FsKYgrLa2dI9iufeFN8/ITlZTzkmjl17KhdPbQpiJs68rleAN3LIsFsgSpL5ZWxd4ZcZ3WeEFaEREQfn";
    private static final String TFOD_MODEL_ASSET = "/sdcard/FIRST/vision/FreightFrenzy_DM.tflite";

    private static final String[] LABELS = {
        "Duck",
    };

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private DuckPosition duckPosition = DuckPosition.RIGHT;

    private enum DuckPosition {
        LEFT, MIDDLE, RIGHT;
    }

    @Override
    public void init_loop() {
        super.init_loop();
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                        recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                        recognition.getRight(), recognition.getBottom());
                    duckPosition = getPosition(recognition);
                    telemetry.addData("position " + i, duckPosition);
                    i++;
                }
                telemetry.update();
            }
        }
    }

    @Override
    protected void setUpHardwareDevices() {
        super.setUpHardwareDevices();
        initVuforia();
        initTfod();
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.6, 16.0 / 9.0);
        }
    }

    @Override
    protected void runOpMode() throws InterruptedException {
        clamp.setPosition(0);
        drive.setWeightedDrivePower(-1, 0, 0);
        sleepWhile(200);
        drive.setWeightedDrivePower(-0.5, -0.01, 0);
        sleepWhile(500);
        drive.setWeightedDrivePower(-0.02, 0, 0);
        carousel.setPower(carouselPower);
        sleepWhile(5000);
        carousel.setPower(0);
        sleepWhile(350);
        drive.setWeightedDrivePower(0.4, 0, 0);
        sleepWhile(1000);
        drive.setWeightedDrivePower(0.3, 0.2, 0);
        sleepWhile(1450);
        drive.setWeightedDrivePower(0, 0, 0);
        drive.turnAsync(Math.toRadians(-105));
        sleepWhile(drive::isBusy);
        switch (duckPosition) {
            case LEFT:
            case RIGHT:
                fullArmSequence(ArmPosition.TOP_GOAL);
                break;
            case MIDDLE:
                drive.setWeightedDrivePower(0.3, 0, 0);
                sleep(350);
                drive.setWeightedDrivePower(0, 0, 0);
                fullArmSequence(ArmPosition.MIDDLE_GOAL);
                drive.setWeightedDrivePower(-0.3, 0, 0);
                sleep(350);
                break;
//            case LEFT:
//                drive.setWeightedDrivePower(0.3, 0, 0);
//                sleep(500);
//                drive.setWeightedDrivePower(0, 0, 0);
//                pullOutArm(ArmPosition.BOTTOM_GOAL);
//                drive.setWeightedDrivePower(-0.3, 0, 0);
//                sleep(150);
//                drive.setWeightedDrivePower(0,-0.3,0);
//                sleep(250);
//                drive.setWeightedDrivePower(0, 0, 0);
//                sleep(100);
//                retractArm();
//                drive.setWeightedDrivePower(-0.3, 0, 0);
//                sleep(400);
//                break;
        }
        drive.setWeightedDrivePower(0, 0, 0);
        // park
        drive.turnAsync(Math.toRadians(110));
        sleepWhile(drive::isBusy);
//        drive.setWeightedDrivePower(0, -0.1, 0);
//        sleepWhile(2000);
        drive.setWeightedDrivePower(0.5, 0, 0);
        sleepWhile(5000);
        drive.setWeightedDrivePower(0, 0, 0);
    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.4f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfodParameters.maxNumDetections = 1;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromFile(TFOD_MODEL_ASSET, LABELS);
    }


    private DuckPosition getPosition(Recognition recognition) {
        double pos = recognition.getLeft();
        if (pos > 50 && pos < 300) {
            return DuckPosition.MIDDLE;
        } else if (pos > 350) {
            return DuckPosition.RIGHT;
        } else {
            return DuckPosition.LEFT;
        }
    }

}
