package org.firstinspires.ftc.teamcode.freightfrenzy.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import org.firstinspires.ftc.teamcode.freightfrenzy.AutonomousOpMode;

public abstract class MainAuto extends AutonomousOpMode {

    private final boolean isRed;

    protected MainAuto(boolean isRed) {
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
            traj = drive.trajectoryBuilder(startPose).lineTo(new Vector2d(-33, 58)).build();
        }
        drive.followTrajectory(traj);

        if (isRed) { // go to carousel
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-53, -57)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-53, 57)).build();
        }
        drive.followTrajectory(traj);
        carousel.setPower(carouselPower);
        drive.setWeightedDrivePower(-0.02, 0, 0);
        sleepWhile(5_000);
        carousel.setPower(0);
        drive.setWeightedDrivePower(0, 0, 0);

        if (isRed) { // go to shipping hub
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-27, -28, Math.toRadians(180))).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-27, 28, Math.toRadians(180))).build();
        }
        drive.followTrajectory(traj);

        if (tsePosition.armPosition() == ArmPosition.BOTTOM_GOAL) {
            traj = drive.trajectoryBuilder(traj.end()).back(5).build();
            drive.followTrajectory(traj);
            pullOutArm(ArmPosition.BOTTOM_GOAL);
            traj = drive.trajectoryBuilder(traj.end()).forward(5).build();
            drive.followTrajectory(traj);
            retractArm();
        } else {
            fullArmSequence(tsePosition.armPosition());
        }

        if (isRed) { // ram against wall
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-27, -62)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-27, 62)).build();
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
