package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class ArmAutomation extends AutonomousOpMode {

    @Override
    public void loop() {
        update();
    }

    @Override
    protected void runOpMode() {
        clamp.setPosition(0);
        whileSleep(1000);
        pulley.setPower(-0.5);
        whileSleep(() -> !railLimit.isPressed());
        pulley.setPower(-0.3);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setTargetPosition(-2000);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(-0.3);
        whileSleep(arm::isBusy);
        arm.setPower(0);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        whileSleep(1000);
        clamp.setPosition(1.0);
        whileSleep(3000);
        clamp.setPosition(0);
        whileSleep(1000);
        pulley.setPower(0);
        arm.setTargetPosition(0);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.3);
        whileSleep(arm::isBusy);
        arm.setPower(0);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        System.out.println("Ran to target");
        pulley.setPower(0.3);
        whileSleep(200);
        pulley.setPower(0);
        whileSleep(10000);
    }
}
