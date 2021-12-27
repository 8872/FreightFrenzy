package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class ArmAutomation extends AutonomousOpMode {

    @Override
    protected void runOpMode() {
        extendArm();
        waitUntilRequestStop = true;
    }
}
