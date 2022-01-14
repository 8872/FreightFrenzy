package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class CarouselSetPowerAuto extends AutonomousOpMode {

    @Override
    protected void runOpMode() throws InterruptedException {
        drive.setWeightedDrivePower(0, 0.3, 0);
        sleepWhile(200);
        drive.setWeightedDrivePower(-0.2, 0, 0);
        sleepWhile(2500);
        carousel.setPower(0.5);
        sleepWhile(5000);
        carousel.setPower(0);
        drive.setWeightedDrivePower(0.4, 0, 0);
        sleepWhile(8000);
        drive.setWeightedDrivePower(0, 0, 0);
    }
}