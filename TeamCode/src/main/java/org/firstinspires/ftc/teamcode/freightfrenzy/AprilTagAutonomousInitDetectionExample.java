/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@TeleOp
@Config

public class AprilTagAutonomousInitDetectionExample extends LinearOpMode {
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    protected final Telemetry dashTelemetry = FtcDashboard.getInstance().getTelemetry();


    static final double FEET_PER_METER = 3.28084;
    static final double INCH_PER_FEET = 12.0;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166; // needs to be changed

    int ID_TAG_OF_INTEREST = 18; // Tag ID 18 from the 36h11 family

    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested()) {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if (currentDetections.size() != 0) {
                boolean tagFound = false;

                for (AprilTagDetection tag : currentDetections) {
                    if (tag.id == ID_TAG_OF_INTEREST) {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if (tagFound) {
                    dashTelemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                } else {
                    dashTelemetry.addLine("Don't see tag of interest :(");

                    if (tagOfInterest == null) {
                        dashTelemetry.addLine("(The tag has never been seen)");
                    } else {
                        dashTelemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            } else {
                dashTelemetry.addLine("Don't see tag of interest :(");

                if (tagOfInterest == null) {
                    dashTelemetry.addLine("(The tag has never been seen)");
                } else {
                    dashTelemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            dashTelemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if (tagOfInterest != null) {
            dashTelemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            dashTelemetry.update();
        } else {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            dashTelemetry.update();
        }

        /* Actually do something useful */
        if (tagOfInterest == null) {
            dashTelemetry.addData("TSE Position", "No Tag Detected!");
            /*
             * Insert your autonomous code here, presumably running some default configuration
             * since the tag was never sighted during INIT
             */
        } else {
            /*
             * Insert your autonomous code here, probably using the tag pose to decide your configuration.
             */

            // e.g.
//            if (tagOfInterest.pose.x * FEET_PER_METER * INCH_PER_FEET <= -7) {
//                // do something
//                dashTelemetry.addData("TSE Position", "LEFT");
//            } else if (tagOfInterest.pose.x * FEET_PER_METER * INCH_PER_FEET > -7 && tagOfInterest.pose.x * FEET_PER_METER * INCH_PER_FEET < 7) {
//                // do something else
//                dashTelemetry.addData("TSE Position", "MIDDLE");
//            } else if (tagOfInterest.pose.x * FEET_PER_METER * INCH_PER_FEET >= 7) {
//                // do something else
//                dashTelemetry.addData("TSE Position", "RIGHT");
//            }
        }


        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        while (opModeIsActive()) {
            sleep(20);
        }
    }

    void tagToTelemetry(AprilTagDetection detection) {
        dashTelemetry.addData("Detected tag", detection.id);
        dashTelemetry.addData("Translation X", detection.pose.x * FEET_PER_METER * INCH_PER_FEET + " inch");
        dashTelemetry.addData("Translation Y", detection.pose.y * FEET_PER_METER * INCH_PER_FEET + " inch");
        dashTelemetry.addData("Translation Z", detection.pose.z * FEET_PER_METER * INCH_PER_FEET + " inch");
        dashTelemetry.addData("Rotation Yaw", Math.toDegrees(detection.pose.yaw));
        dashTelemetry.addData("Rotation Pitch", Math.toDegrees(detection.pose.pitch));
        dashTelemetry.addData("Rotation Roll", Math.toDegrees(detection.pose.roll));
        if (detection.pose.x * FEET_PER_METER * INCH_PER_FEET <= -7) {
            // do something
            dashTelemetry.addData("TSE Position", "LEFT");
        } else if (detection.pose.x * FEET_PER_METER * INCH_PER_FEET > -7 && detection.pose.x * FEET_PER_METER * INCH_PER_FEET < 7) {
            // do something else
            dashTelemetry.addData("TSE Position", "MIDDLE");
        } else if (detection.pose.x * FEET_PER_METER * INCH_PER_FEET >= 7) {
            // do something else
            dashTelemetry.addData("TSE Position", "RIGHT");
        }
    }
}