package org.firstinspires.ftc.teamcode.freightfrenzy;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class DropOffFreight extends AutonomousOpMode {

    @Override
    protected void runOpMode() throws InterruptedException {
        clamp.setPosition(0);
        drive.setWeightedDrivePower(-1, 0, 0);
        sleepWhile(200);
        drive.setWeightedDrivePower(-0.5, -0.01, 0);
        sleepWhile(500);
        drive.setWeightedDrivePower(-0.05, 0, 0);
        carousel.setPower(carouselPower);
        sleepWhile(5000);
        carousel.setPower(0);
        sleepWhile(350);
        drive.setWeightedDrivePower(0.4, 0, 0);
        sleepWhile(1000);
        drive.setWeightedDrivePower(0.3, 0.2, 0);
        sleepWhile(1450);
        drive.setWeightedDrivePower(0, 0, 0);
        drive.turnAsync(Math.toRadians(-105));
        sleepWhile(drive::isBusy);
        fullArmSequence(ArmPosition.TOP_GOAL);

        // park
        drive.turnAsync(Math.toRadians(120));
        sleepWhile(drive::isBusy);
        drive.setWeightedDrivePower(0, -0.1, 0);
        sleepWhile(2000);
        drive.setWeightedDrivePower(0.5, 0, 0);
        sleepWhile(5000);
        drive.setWeightedDrivePower(0, 0, 0);
    }

}
