package org.firstinspires.ftc.teamcode.freightfrenzy;

import androidx.annotation.Nullable;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

abstract class AutonomousOpMode extends FreightFrenzyOpMode {

    {msStuckDetectInit = 10_000;}

    protected boolean waitUntilRequestStop = false;

    @Override
    public void start() {
        super.start();

        poolFuture = pool.submit(() -> {
            try {
                runOpMode();
                if (!waitUntilRequestStop) {
                    requestOpModeStop();
                }
            } catch (InterruptedException ignored) {
            }
        });
    }

    @Override
    public void loop() {update();}

    protected abstract void runOpMode() throws InterruptedException;

    protected TSEPosition tsePosition;

    private OpenCvCamera camera;
    private AprilTagDetectionPipeline aprilTagDetectionPipeline;

    private static final double FEET_PER_METER = 3.28084;
    private static final double INCH_PER_FEET = 12.0;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    private static final double fx = 578.272, fy = 578.272, cx = 402.145, cy = 221.506;

    // UNITS ARE METERS
    private static final double tagsize = 0.166; // needs to be changed

    private static final int ID_TAG_OF_INTEREST = 18; // Tag ID 18 from the 36h11 family

    @Override
    protected void setUpHardwareDevices() {
        super.setUpHardwareDevices();
        initCV();
        telemetry.addData("TSE Position", () -> tsePosition);
    }

    private void initCV() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                RobotLog.addGlobalWarningMessage("Error opening camera: " + errorCode);
            }
        });
    }

    @Override
    public void init_loop() {
        super.init_loop();

        for (AprilTagDetection tag : aprilTagDetectionPipeline.getLatestDetections()) {
            if (tag.id == ID_TAG_OF_INTEREST) {
                tsePosition = TSEPosition.from(tag);
            }
        }

        sleepWhile(20);
    }

    protected enum TSEPosition {
        LEFT, MIDDLE, RIGHT;

        @Nullable
        public static TSEPosition from(AprilTagDetection detection) {
            if (detection.pose.x * FEET_PER_METER * INCH_PER_FEET <= -7) {
                return LEFT;
            } else if (detection.pose.x * FEET_PER_METER * INCH_PER_FEET > -7 && detection.pose.x * FEET_PER_METER * INCH_PER_FEET < 7) {
                return MIDDLE;
            } else if (detection.pose.x * FEET_PER_METER * INCH_PER_FEET >= 7) {
                return RIGHT;
            }
            return null;
        }
    }

}
