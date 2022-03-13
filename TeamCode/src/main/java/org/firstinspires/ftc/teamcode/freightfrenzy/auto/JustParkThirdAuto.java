package org.firstinspires.ftc.teamcode.freightfrenzy.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import org.firstinspires.ftc.teamcode.freightfrenzy.AutonomousOpMode;

public abstract class JustParkThirdAuto extends AutonomousOpMode {

    private final boolean isRed;

    protected JustParkThirdAuto(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d startPoseR = new Pose2d(-33, -62, Math.toRadians(90));
        Pose2d startPoseB = new Pose2d(-33, 62, Math.toRadians(-90));
        Pose2d startPose = isRed ? startPoseR : startPoseB;
        drive.setPoseEstimate(startPose);

        Trajectory traj;
        if (isRed) { // park
            traj = drive.trajectoryBuilder(startPose).lineTo(new Vector2d(-63, -35)).build();
        } else {
            traj = drive.trajectoryBuilder(startPose).lineTo(new Vector2d(-63, 35)).build();
        }
        drive.followTrajectory(traj);

    }
}
