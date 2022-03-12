package org.firstinspires.ftc.teamcode.freightfrenzy.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

public abstract class PickUpFreightAuto extends LinearOpMode {

    private final boolean isRed;

    protected PickUpFreightAuto(boolean isRed) {
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
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-33, 58)).build();
        }

        drive.followTrajectory(traj);

        if (isRed) { // go to carousel
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-53, -57)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-53, 57)).build();
        }
        drive.followTrajectory(traj);
        sleep(1000);

        if (isRed) { // go to shipping hub
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-27, -28, Math.toRadians(180))).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-27, 28, Math.toRadians(180))).build();
        }
        drive.followTrajectory(traj);
        sleep(1000);
        if (isRed) { // ram against wall
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-27, -62, Math.toRadians(0))).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-27, 62, Math.toRadians(0))).build();
        }
        drive.followTrajectory(traj);
        if (isRed) { // park
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(38, -62)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(38, 62)).build();
        }
        drive.followTrajectory(traj);
        sleep(1000); //Pick up freight
        if (isRed) { // moves slightly forward
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(9, -62)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(9, 62)).build();
        }
        drive.followTrajectory(traj);
        if (isRed) { // moves slightly to the right
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(9, -28)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(9, 28)).build();
        }
        drive.followTrajectory(traj);
        sleep(1000);
        if (isRed) { // moves slightly backward
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(9, -62)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(9, 62)).build();
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
