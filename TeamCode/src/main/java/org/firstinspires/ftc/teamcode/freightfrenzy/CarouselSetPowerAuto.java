package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Config
@Autonomous
public class CarouselSetPowerAuto extends AutonomousOpMode {
    public static int sleep2 = 2500;
    public static int sleep3 = 5000;
    public static int sleep4 = 3500;
    public static int sleep5 = 350;

    @Override
    protected void runOpMode() throws InterruptedException {
        drive.setWeightedDrivePower(-0.3, -0.01, 0);
        sleepWhile(sleep2);
        drive.setWeightedDrivePower(-0.05, 0, 0);
        carousel.setPower(carouselPower);
        sleepWhile(sleep3);
        carousel.setPower(0);
        sleepWhile(sleep5);
        drive.setWeightedDrivePower(0.6, -0.01, 0);
        sleepWhile(sleep4);
        drive.setWeightedDrivePower(0, 0, 0);
    }
}