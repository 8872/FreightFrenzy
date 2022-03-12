package org.firstinspires.ftc.teamcode.freightfrenzy.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import org.firstinspires.ftc.teamcode.freightfrenzy.AutonomousOpMode;

public abstract class JustShippingHub3Auto extends AutonomousOpMode {

    private final boolean isRed;

    protected JustShippingHub3Auto(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d startPoseR = new Pose2d(-33, -62, Math.toRadians(90));
        Pose2d startPoseB = new Pose2d(-33, 62, Math.toRadians(-90));
        Pose2d startPose = isRed ? startPoseR : startPoseB;
        drive.setPoseEstimate(startPose);

        Trajectory traj;
        if (isRed) { // go to shipping hub
            traj = drive.trajectoryBuilder(startPose).lineToLinearHeading(new Pose2d(-27, -28, Math.toRadians(180))).build();
        } else {
            traj = drive.trajectoryBuilder(startPose).lineToLinearHeading(new Pose2d(-27, 28, Math.toRadians(180))).build();
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

        if (isRed) { // park
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-63, -37)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-63, 37)).build();
        }
        drive.followTrajectory(traj);

    }
}
