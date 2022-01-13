package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class ParkAuto extends AutonomousOpMode {

    @Override
    protected void runOpMode() throws InterruptedException {
        drive.setPoseEstimate(new Pose2d());
        Trajectory forward = drive.trajectoryBuilder(new Pose2d()).forward(15).build();
        drive.followTrajectory(forward);
        drive.turn(-90);
        drive.followTrajectory(drive.trajectoryBuilder(drive.getPoseEstimate()).forward(100).build());

        waitUntilRequestStop = true;
    }
}
