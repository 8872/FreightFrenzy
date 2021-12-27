package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class ArmTest extends AutonomousOpMode {

    @Override
    protected void runOpMode() throws InterruptedException {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setTargetPosition(-2000);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(-0.3);
        var time = System.currentTimeMillis();
        sleepWhile(arm::isBusy);
        System.out.println(System.currentTimeMillis() - time);
        if (System.currentTimeMillis() - time < 100) {
            sleepWhile(1000);
        }
        arm.setPower(0);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitUntilRequestStop = true;
    }
}
