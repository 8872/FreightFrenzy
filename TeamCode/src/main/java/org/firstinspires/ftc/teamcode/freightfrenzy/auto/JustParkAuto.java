package org.firstinspires.ftc.teamcode.freightfrenzy.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import org.firstinspires.ftc.teamcode.freightfrenzy.AutonomousOpMode;

public abstract class JustParkAuto extends AutonomousOpMode {

    private final boolean isRed;

    protected JustParkAuto(boolean isRed) {
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
            traj = drive.trajectoryBuilder(startPose).lineTo(new Vector2d(-33, -58)).build();

        } else {
            traj = drive.trajectoryBuilder(startPose).lineTo(new Pose2d(-33, 58, Math.toRadians(0)).vec()).build();
        }

        drive.followTrajectory(traj);
        drive.turn(90);


        if (isRed) { // ram against wall
            traj = drive.trajectoryBuilder(drive.getPoseEstimate()).lineTo(new Vector2d(-27, -62)).build();
        } else {
            traj = drive.trajectoryBuilder(drive.getPoseEstimate()).lineTo(new Vector2d(-27, 62)).build();
        }
        drive.followTrajectory(traj);
        if (isRed) { // park
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(38, -62)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(38, 62)).build();
        }
        drive.followTrajectory(traj);


    }
}
