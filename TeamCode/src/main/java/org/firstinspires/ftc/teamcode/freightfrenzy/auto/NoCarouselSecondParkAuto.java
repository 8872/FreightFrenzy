package org.firstinspires.ftc.teamcode.freightfrenzy.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

public abstract class NoCarouselSecondParkAuto extends LinearOpMode {

    private final boolean isRed;

    protected NoCarouselSecondParkAuto(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public void runOpMode() throws InterruptedException {

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPoseR = new Pose2d(9, -62, Math.toRadians(90));
        Pose2d startPoseB = new Pose2d(9, 62, Math.toRadians(-90));

        Trajectory traj = drive.trajectoryBuilder(new Pose2d())
                .lineToLinearHeading(isRed ? startPoseR : startPoseB).build();
        drive.followTrajectory(traj);
        waitForStart();

        if (isRed) { // go to shipping hub
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(9, -28, Math.toRadians(0))).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(9, 28, Math.toRadians(0))).build();
        }
        drive.followTrajectory(traj);
        sleep(1000);
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
