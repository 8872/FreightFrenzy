package org.firstinspires.ftc.teamcode.freightfrenzy;

import androidx.annotation.Nullable;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous
public class FreightFrenzyAuto extends AutonomousOpMode {
    private final boolean red;
    private Trajectory trajectory1R, trajectory2R, trajectory3R, trajectory1B, trajectory2B, trajectory3B;

    private static final String VUFORIA_KEY =
            "AR1EGWL/////AAABmao6fwhlA0nZgC4AC92PSFIkRoulXKGjKgy0eFqp2+gwuiWL9ULzw2QJD/Jr7os9Xby/GjZHBwwPW3P6vvVfidwd556TIQRTX6NzaGOooiLjLWebMMHcEJdvLD+4VdbHvZaEiXlH4O/Vb+Rqqo+PS5LUE9LQxnYtSYvbtWDVz757S56MSByBrH7Zt7zTFu0a3Rlvr7s7o9wGR74qQ1jI/vIuWWUIWXPUXCb9L+TVqMPFk0yOumhdyUhmTf8JXBPOWnppwXKJ7049tnegzoc6Ov+IuIu7FsKYgrLa2dI9iufeFN8/ITlZTzkmjl17KhdPbQpiJs68rleAN3LIsFsgSpL5ZWxd4ZcZ3WeEFaEREQfn";

    private static final String TFOD_MODEL_ASSET = "/sdcard/FIRST/vision/FreightFrenzy_DM.tflite";

    private static final String[] LABELS = {
            "Duck",
    };

    private enum Positions {
        LEFT,
        MIDDLE,
        RIGHT
    }

    private TFObjectDetector tfod;

    public FreightFrenzyAuto(boolean red) {
        this.red = red;
    }

    @Override
    public void init_loop() {
        super.init_loop();
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
                telemetry.addData("position " + i, getPosition(recognition));
                i++;
            }
            update();
        }
    }

    @Override
    protected void setUpHardwareDevices() {
        super.setUpHardwareDevices();
        Pose2d startPoseR = new Pose2d(-33, -60, Math.toRadians(90));
        Pose2d startPoseB = new Pose2d(-33, 60, Math.toRadians(270));
        drive.setPoseEstimate(red ? startPoseR : startPoseB);
        update();

        //red
        trajectory1R = drive.trajectoryBuilder(startPoseR)
                .lineToLinearHeading(new Pose2d(-50, -60, Math.toRadians(90)))
                .build();
        trajectory2R = drive.trajectoryBuilder(trajectory1R.end())
                .lineToLinearHeading(new Pose2d(-12, -46, Math.toRadians(-90)))
                .build();
        trajectory3R = drive.trajectoryBuilder(trajectory2R.end())
                .splineToLinearHeading(new Pose2d(54, -54, Math.toRadians(0)), Math.toRadians(0))
                .build();
        //blue
        trajectory1B = drive.trajectoryBuilder(startPoseB)
                .lineToLinearHeading(new Pose2d(-50, 60, Math.toRadians(-90)))
                .build();
        trajectory2B = drive.trajectoryBuilder(trajectory1B.end())
                .lineToLinearHeading(new Pose2d(-12, 46, Math.toRadians(90)))
                .build();
        trajectory3B = drive.trajectoryBuilder(trajectory2B.end())
                .splineToLinearHeading(new Pose2d(54, 54, Math.toRadians(0)), Math.toRadians(0))
                .build();

        initializeTfod();
    }

    @Override
    protected void runOpMode() throws InterruptedException {
        if (red) {
            drive.followTrajectory(trajectory1R); //turn on carousel
            carousel.setPower(carouselPower);
            sleepWhile(10_000);
            carousel.setPower(0);
            drive.followTrajectory(trajectory2R);
            fullArmSequence(ArmPosition.TOP_GOAL); //drop off payload (pulley, arm)
            drive.followTrajectory(trajectory3R);
        } else {
            drive.followTrajectory(trajectory1B);
            //turn on carousel
            drive.followTrajectory(trajectory2B);
            //drop off payload (pulley, arm)
            drive.followTrajectory(trajectory3B);
        }

        waitUntilRequestStop = true;
    }

    private void initializeTfod() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(parameters);

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromFile(TFOD_MODEL_ASSET, LABELS);
        tfod.activate();
        tfod.setZoom(1.3, 16.0 / 9.0);
    }

    private enum DuckPosition {
        LEFT, MIDDLE, RIGHT;
    }

    @Nullable
    private DuckPosition getPosition(Recognition recognition) {
        double pos = recognition.getLeft();
        if (pos > 0 && pos < 200) {
            return DuckPosition.LEFT;
        } else if (pos > 300 && pos < 400) {
            return DuckPosition.MIDDLE;
        } else if (pos > 500) {
            return DuckPosition.RIGHT;
        } else {
            return null;
        }
    }
}