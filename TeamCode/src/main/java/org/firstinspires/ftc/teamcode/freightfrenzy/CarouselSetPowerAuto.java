package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Config
@Autonomous
public class CarouselSetPowerAuto extends AutonomousOpMode {
    public static int sleep1 = 200;
    public static int sleep2 = 2500;
    public static int sleep3 = 5000;
    public static int sleep4 = 7000;
    public static int sleep5 = 350;

    @Override
    protected void runOpMode() throws InterruptedException {
        drive.setWeightedDrivePower(0, 0.3, 0);
        sleepWhile(sleep1);
        drive.setWeightedDrivePower(-0.2, 0, 0);
        sleepWhile(sleep2);
        carousel.setPower(carouselPower);
        sleepWhile(sleep3);
        carousel.setPower(0);
        drive.setWeightedDrivePower(0, 0.3, 0);
        sleepWhile(sleep5);
        drive.setWeightedDrivePower(0.5, 0, 0);
        sleepWhile(sleep4);
        drive.setWeightedDrivePower(0, 0, 0);
    }
}