package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

public class FreightFrenzyAuto extends AutonomousOpMode {
    private final boolean red;
    private Trajectory trajectory1R, trajectory2R, trajectory3R, trajectory1B, trajectory2B, trajectory3B;

    public FreightFrenzyAuto(boolean red) {
        this.red = red;
    }

    @Override
    public void init_loop() {
        super.init_loop();
        update();
    }

    @Override
    protected void setUpHardwareDevices() {
        super.setUpHardwareDevices();
        Pose2d startPoseR = new Pose2d(-33, -60, Math.toRadians(90));
        Pose2d startPoseB = new Pose2d(-33, 60, Math.toRadians(270));
        drive.setPoseEstimate(red ? startPoseR : startPoseB);
        update();

        //red
        trajectory1R = drive.trajectoryBuilder(startPoseR)
                .lineToLinearHeading(new Pose2d(-50, -60, Math.toRadians(90)))
                .build();
        trajectory2R = drive.trajectoryBuilder(trajectory1R.end())
                .lineToLinearHeading(new Pose2d(-12, -46, Math.toRadians(-90)))
                .build();
        trajectory3R = drive.trajectoryBuilder(trajectory2R.end())
                .splineToLinearHeading(new Pose2d(54, -54, Math.toRadians(0)), Math.toRadians(0))
                .build();
        //blue
        trajectory1B = drive.trajectoryBuilder(startPoseB)
                .lineToLinearHeading(new Pose2d(-50, 60, Math.toRadians(-90)))
                .build();
        trajectory2B = drive.trajectoryBuilder(trajectory1B.end())
                .lineToLinearHeading(new Pose2d(-12, 46, Math.toRadians(90)))
                .build();
        trajectory3B = drive.trajectoryBuilder(trajectory2B.end())
                .splineToLinearHeading(new Pose2d(54, 54, Math.toRadians(0)), Math.toRadians(0))
                .build();
    }

    @Override
    protected void runOpMode() throws InterruptedException {
        if (red) {
            drive.followTrajectory(trajectory1R); //turn on carousel
            carousel.setPower(carouselPower);
            sleepWhile(10_000);
            carousel.setPower(0);
            drive.followTrajectory(trajectory2R);
            fullArmSequence(ArmPosition.TOP_GOAL); //drop off payload (pulley, arm)
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
