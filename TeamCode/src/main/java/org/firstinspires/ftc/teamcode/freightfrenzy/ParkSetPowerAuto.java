package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class ParkSetPowerAuto extends AutonomousOpMode {

    @Override
    protected void runOpMode() throws InterruptedException {
        drive.setWeightedDrivePower(0, 0.3, 0);
        sleepWhile(600);
        drive.setWeightedDrivePower(0.7, 0, 0);
        sleepWhile(3500);
        drive.setWeightedDrivePower(0, 0, 0);

    }
}
