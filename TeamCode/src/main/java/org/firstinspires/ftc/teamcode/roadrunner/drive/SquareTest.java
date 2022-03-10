package org.firstinspires.ftc.teamcode.roadrunner.drive;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Disabled
@Autonomous(group = "drive")
public class SquareTest extends LinearOpMode {
    public static double DISTANCE = 40; // in

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(new Pose2d());

        Trajectory trajectory1 = drive.trajectoryBuilder(new Pose2d())
                .forward(DISTANCE)
                .build();

        Trajectory trajectory2 = drive.trajectoryBuilder(trajectory1.end())
                .strafeLeft(DISTANCE)
                .build();

        Trajectory trajectory3 = drive.trajectoryBuilder(trajectory2.end())
                .forward(-DISTANCE)
                .build();

        Trajectory trajectory4 = drive.trajectoryBuilder(trajectory3.end())
                .strafeRight(DISTANCE)
                .build();

        waitForStart();


        for (int i = 0; i < 3 && !isStopRequested(); i++) {
            drive.followTrajectory(trajectory1);
            drive.followTrajectory(trajectory2);
            drive.followTrajectory(trajectory3);
            drive.followTrajectory(trajectory4);
        }

        Pose2d poseEstimate = drive.getPoseEstimate();
        telemetry.addData("finalX", poseEstimate.getX());
        telemetry.addData("finalY", poseEstimate.getY());
        telemetry.addData("finalHeading", poseEstimate.getHeading());
        telemetry.update();

        while (opModeIsActive()) ;
    }
}
