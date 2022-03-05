package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

@Autonomous
public abstract class MainAuto extends TestAutonomousOpMode {

    private final boolean isRed;

    protected MainAuto(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPoseR = new Pose2d(-33, -62, Math.toRadians(90));
        Pose2d startPoseB = new Pose2d(-33, 62, Math.toRadians(-90));

        Trajectory traj = drive.trajectoryBuilder(new Pose2d()).lineToLinearHeading(isRed ? startPoseR : startPoseB).build();
//        drive.followTrajectory(traj);
//        waitForStart();

        if (isRed) {
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-33, -58, Math.toRadians(0))).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-33, 58)).build();
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
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-27, -28, Math.toRadians(180))).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineToLinearHeading(new Pose2d(-27, 28, Math.toRadians(180))).build();
        }
        drive.followTrajectory(traj);
        sleep(1000);
        if (isRed) {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-27, -62)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(-27, 62)).build();
        }
        drive.followTrajectory(traj);
        if (isRed) {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(38, -62)).build();
        } else {
            traj = drive.trajectoryBuilder(traj.end()).lineTo(new Vector2d(38, 62)).build();
        }
        drive.followTrajectory(traj);


    }
}