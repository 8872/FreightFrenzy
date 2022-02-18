package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Disabled
@Autonomous(group = "drive")
@Config
public class StraightTest extends AutonomousOpMode {
    public static double DISTANCE = 30; // in
    private Trajectory trajectory1;
    private Trajectory trajectory2;

    @Override
    protected void setUpHardwareDevices() {
        super.setUpHardwareDevices();
        Pose2d startPoseR = new Pose2d(-33, -60, 0);
        drive.setPoseEstimate(startPoseR);

        trajectory1 = drive.trajectoryBuilder(startPoseR)
                .forward(DISTANCE)
                .build();

        trajectory2 = drive.trajectoryBuilder(trajectory1.end())
                .forward(-DISTANCE)
                .build();
    }

    @Override
    public void runOpMode() throws InterruptedException {

        drive.followTrajectory(trajectory1);
//        drive.followTrajectory(trajectory2);

        waitUntilRequestStop = true;
    }
}
