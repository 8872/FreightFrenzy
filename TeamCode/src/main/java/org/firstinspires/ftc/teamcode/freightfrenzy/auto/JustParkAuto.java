package org.firstinspires.ftc.teamcode.freightfrenzy.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

public abstract class JustParkAuto extends LinearOpMode {

    private final boolean isRed;

    protected JustParkAuto(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public void runOpMode() throws InterruptedException {

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPoseR = new Pose2d(-33, -62, Math.toRadians(90));
        Pose2d startPoseB = new Pose2d(-33, 62, Math.toRadians(-90));

        Trajectory traj = drive.trajectoryBuilder(new Pose2d())
                .lineToLinearHeading(isRed ? startPoseR : startPoseB).build();
        drive.followTrajectory(traj);
        waitForStart();
        if (isRed) { // forward and turn
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-33, -58, Math.toRadians(0))).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-33, 58, Math.toRadians(0))).build();
        }

        drive.followTrajectory(traj);
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
