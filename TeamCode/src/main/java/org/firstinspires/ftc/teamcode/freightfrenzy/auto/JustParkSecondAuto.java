package org.firstinspires.ftc.teamcode.freightfrenzy.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import org.firstinspires.ftc.teamcode.freightfrenzy.AutonomousOpMode;

public abstract class JustParkSecondAuto extends AutonomousOpMode {

    private final boolean isRed;

    protected JustParkSecondAuto(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d startPoseR = new Pose2d(-33, -62, Math.toRadians(90));
        Pose2d startPoseB = new Pose2d(-33, 62, Math.toRadians(-90));
        Pose2d startPose = isRed ? startPoseR : startPoseB;
        drive.setPoseEstimate(startPose);

        Trajectory traj;
        if (isRed) { // forward and turn
            traj = drive.trajectoryBuilder(startPose).lineToLinearHeading(new Pose2d(-33, -58, Math.toRadians(0))).build();
        } else {
            traj = drive.trajectoryBuilder(startPose).lineToLinearHeading(new Pose2d(-33, 58, Math.toRadians(0))).build();
        }

        drive.followTrajectory(traj);

        if (isRed) { // park
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(9, -62)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(9, 62)).build();
        }
        drive.followTrajectory(traj);
        if (isRed) { // ram against wall
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(9, -40)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(9, 40)).build();
        }

        drive.followTrajectory(traj);
        if (isRed) { // park
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(58, -40)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(58, 40)).build();
        }
        drive.followTrajectory(traj);


    }
}
