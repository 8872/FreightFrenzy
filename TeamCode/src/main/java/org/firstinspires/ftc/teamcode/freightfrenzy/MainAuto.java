package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;


public abstract class MainAuto extends AutonomousOpMode {
    private Trajectory traj;

    private final boolean isRed;

    private Pose2d startPoseR, startPoseB;

    protected MainAuto(boolean isRed) {
        super();
        this.isRed = isRed;
    }

    protected void setUpHardwareDevices() {
        super.setUpHardwareDevices();
        startPoseR = new Pose2d(-33, -62, Math.toRadians(90));
        startPoseB = new Pose2d(-33, 62, Math.toRadians(-90));
        drive.setPoseEstimate(isRed ? startPoseR : startPoseB);
        update();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        update();

        if (isRed) {
            traj = drive.trajectoryBuilder(startPoseR).lineToLinearHeading(new Pose2d(-33, -58, Math.toRadians(0))).build();

        } else {
            traj = drive.trajectoryBuilder(startPoseB).lineTo(new Vector2d(-33, 58)).build();
        }

        drive.followTrajectory(traj);

        if (isRed) {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-53, -57)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-53, 57)).build();
        }
        drive.followTrajectory(traj);
        sleep(1000);

        if (isRed) {
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-27, -28, Math.toRadians(270))).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-27, 28, Math.toRadians(270))).build();
        }
        drive.followTrajectory(traj);
        sleep(1000);
        if (isRed) {
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-27, -62, Math.toRadians(180))).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-27, 62, Math.toRadians(180))).build();
        }
        drive.followTrajectory(traj);
        if (isRed) {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(38, -62)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(38, 62)).build();
        }
        drive.followTrajectory(traj);

        waitUntilRequestStop = true;


    }
}
