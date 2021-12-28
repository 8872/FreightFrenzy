package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class ArmAutomation extends AutonomousOpMode {

    @Override
    protected void runOpMode() {
        fullArmSequence(ArmPosition.TOP_GOAL);
        waitUntilRequestStop = true;
    }
}
