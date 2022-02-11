package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
public class ShippingElementDetectionEOCV extends OpMode {

    Telemetry dashTelemetry = FtcDashboard.getInstance().getTelemetry();
    OpenCvWebcam webcam;
    ShippingElementAvgPipeline pipeline = new ShippingElementAvgPipeline();

    @Override
    public void init() {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                webcam.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });
    }

    @Override
    public void loop() {
        telemetry.addData("Analysis", pipeline.getAnalysis());
        telemetry.addData("Avg1", pipeline.getAvg()[0]);
        telemetry.addData("Avg2", pipeline.getAvg()[1]);
        telemetry.addData("Avg3", pipeline.getAvg()[2]);
        dashTelemetry.addData("Analysis", pipeline.getAnalysis());
        telemetry.update();
    }
}
