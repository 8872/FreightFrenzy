package org.firstinspires.ftc.teamcode.freightfrenzy.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import org.firstinspires.ftc.teamcode.freightfrenzy.AutonomousOpMode;

@Config
public abstract class NoCarouselAuto extends AutonomousOpMode {

    private final boolean isRed;

    public static int turn = -90;

    protected NoCarouselAuto(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d startPoseR = new Pose2d(9, -62, Math.toRadians(90));
        Pose2d startPoseB = new Pose2d(9, 62, Math.toRadians(-90));
        Pose2d startPose = isRed ? startPoseR : startPoseB;
        drive.setPoseEstimate(startPose);

        Trajectory traj;
        if (isRed) { // go to shipping hub
            traj = drive.trajectoryBuilder(startPose).lineTo(new Vector2d(9, -28)).build();
        } else {
            traj = drive.trajectoryBuilder(startPose).lineTo(new Vector2d(9, 28)).build();
        }
        drive.followTrajectory(traj);
        drive.turn(Math.toRadians(turn));
        if (tsePosition.armPosition() == ArmPosition.BOTTOM_GOAL) {
            traj = drive.trajectoryBuilder(drive.getPoseEstimate()).back(5).build();
            drive.followTrajectory(traj);
            pullOutArm(ArmPosition.BOTTOM_GOAL);
            traj = drive.trajectoryBuilder(traj.end()).forward(5).build();
            drive.followTrajectory(traj);
            retractArm();
        } else {
            fullArmSequence(tsePosition.armPosition());
        }

        if (isRed) { // ram against wall
            traj = drive.trajectoryBuilder(drive.getPoseEstimate()).lineTo(new Vector2d(9, -70)).build();
        } else {
            traj = drive.trajectoryBuilder(drive.getPoseEstimate()).lineTo(new Vector2d(9, 70)).build();
        }
        drive.followTrajectory(traj);
        if (isRed) { // park
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(50, -70)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(50, 70)).build();
        }
        drive.followTrajectory(traj);


    }
}
