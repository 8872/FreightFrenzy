package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class TestAutoOpMode extends TestAutonomousOpMode {
    private final Pose2d startPoseR = new Pose2d(-33, -60, Math.toRadians(90));

    @Override
    protected void runOpMode() throws InterruptedException {
        Trajectory trajectory1R = drive.trajectoryBuilder(startPoseR)
                .strafeLeft(50)
                .build();

        drive.followTrajectory(trajectory1R);

        waitUntilRequestStop = true;
    }

    @Override
    protected void setUpHardwareDevices() {
        super.setUpHardwareDevices();
        drive.setPoseEstimate(startPoseR);
        update();
    }


}
