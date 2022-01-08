package org.firstinspires.ftc.teamcode.roadrunner.drive.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

/*
 * This is a simple routine to test translational drive capabilities.
 */


public class StraightTest extends LinearOpMode {
    public static double DISTANCE = 48; // in

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor leftFront = hardwareMap.dcMotor.get("leftFront");
        DcMotor leftRear = hardwareMap.dcMotor.get("leftRear");
        DcMotor rightFront = hardwareMap.dcMotor.get("rightFront");
        DcMotor rightRear = hardwareMap.dcMotor.get("rightRear");

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Trajectory trajectory1 = drive.trajectoryBuilder(new Pose2d())
                .forward(DISTANCE)
                .build();

        Trajectory trajectory2 = drive.trajectoryBuilder(new Pose2d())
                .forward(-DISTANCE)
                .build();



        waitForStart();

        if (isStopRequested()) return;

        drive.followTrajectoryAsync(trajectory1);
        while (!Thread.currentThread().isInterrupted() && drive.isBusy()) {
            drive.update();
            telemetry.addData("leftFront", leftFront.getPower());
            telemetry.addData("leftRear", leftRear.getPower());
            telemetry.addData("rightFront", rightFront.getPower());
            telemetry.addData("rightRear", rightRear.getPower());
            telemetry.update();
        }
        drive.followTrajectoryAsync(trajectory2);
        while (!Thread.currentThread().isInterrupted() && drive.isBusy()) {
            drive.update();
            telemetry.addData("leftFront", leftFront.getPower());
            telemetry.addData("leftRear", leftRear.getPower());
            telemetry.addData("rightFront", rightFront.getPower());
            telemetry.addData("rightRear", rightRear.getPower());
            telemetry.update();
        }


        Pose2d poseEstimate = drive.getPoseEstimate();
        telemetry.addData("finalX", poseEstimate.getX());
        telemetry.addData("finalY", poseEstimate.getY());
        telemetry.addData("finalHeading", poseEstimate.getHeading());
        telemetry.update();

        while (!isStopRequested() && opModeIsActive()) ;
    }
}
