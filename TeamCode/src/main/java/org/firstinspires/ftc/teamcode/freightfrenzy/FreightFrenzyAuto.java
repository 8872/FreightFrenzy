package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

public class FreightFrenzyAuto extends AutonomousOpMode {
    private final boolean red;

    public FreightFrenzyAuto(boolean red) {
        this.red = red;
    }


    @Override
    protected void runOpMode() throws InterruptedException {

        Pose2d startPoseR = new Pose2d(-33, -60, Math.toRadians(0));
        Pose2d startPoseB = new Pose2d(-33, 60, Math.toRadians(0));

        //red
        Trajectory trajectory1R = drive.trajectoryBuilder(startPoseR)
                .lineToLinearHeading(new Pose2d(-50, -60, Math.toRadians(0)))
                .build();
        Trajectory trajectory2R = drive.trajectoryBuilder(trajectory1R.end())
                .lineToLinearHeading(new Pose2d(-12, -46, Math.toRadians(-90)))
                .build();
        Trajectory trajectory3R = drive.trajectoryBuilder(trajectory2R.end())
                .splineToLinearHeading(new Pose2d(54, -54, Math.toRadians(0)), Math.toRadians(0))
                .build();
        //blue
        Trajectory trajectory1B = drive.trajectoryBuilder(startPoseB)
                .lineToLinearHeading(new Pose2d(-50, 60, Math.toRadians(-90)))
                .build();
        Trajectory trajectory2B = drive.trajectoryBuilder(trajectory1B.end())
                .lineToLinearHeading(new Pose2d(-12, 46, Math.toRadians(90)))
                .build();
        Trajectory trajectory3B = drive.trajectoryBuilder(trajectory2B.end())
                .splineToLinearHeading(new Pose2d(54, 54, Math.toRadians(0)), Math.toRadians(0))
                .build();

        if (isStopRequested()) return;

        if (red) {
            drive.followTrajectory(trajectory1R);
            //turn on carousel
            drive.followTrajectory(trajectory2R);
            //drop off payload (pulley, arm)
            drive.followTrajectory(trajectory3R);
        } else {
            drive.followTrajectory(trajectory1B);
            //turn on carousel
            drive.followTrajectory(trajectory2B);
            //drop off payload (pulley, arm)
            drive.followTrajectory(trajectory3B);
        }

        waitUntilRequestStop = true;
    }
}
