package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Config
abstract class FreightFrenzyOpMode extends BaseOpMode {

    public static double intakeVelocity = 90, carouselPower = 0.5, pulleyIdlePower = -0.1;

    protected boolean armIsOut = false;

    protected DcMotor carousel;
    protected DcMotorEx intake, pulley, arm;
    protected Servo clamp;
    protected TouchSensor railLimit;

    @Override
    protected void composeTelemetry() {
        super.composeTelemetry();
        telemetry.addData("touch sensor", railLimit::isPressed);
        telemetry.addData("carousel", carousel::getPower);
        telemetry.addData("intake", intake::getPower);
        telemetry.addData("intake velocity", () -> intake.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("pulley", pulley::getPower);
        telemetry.addData("arm", arm::getPower);
        telemetry.addData("arm pos", arm::getCurrentPosition);
        telemetry.addData("arm busy", arm::isBusy);
        telemetry.addData("clamp", clamp::getPosition);
    }

    @Override
    protected void setUpHardwareDevices() {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pulley.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pulley.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    protected static class ArmPosition {
        public static final int TOP_GOAL = -2200, MIDDLE_GOAL = -2400, BOTTOM_GOAL = -2600;
    }

    protected final void fullArmSequence() {
        pullOutArm();
        retractArm();
    }

    protected final void pullOutArm() {
        if (armIsOut) {
            return;
        }
        clamp.setPosition(0);
        sleepWhile(300);
        pulley.setPower(-0.4);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setTargetPosition(ArmPosition.TOP_GOAL);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.6);
        boolean[] pulleyFinished = {false}, armFinished = {false}; // using array so vars can still be effectively final
        sleepWhile(() -> {
            if (!pulleyFinished[0] && railLimit.isPressed()) {
                pulley.setPower(pulleyIdlePower);
                pulleyFinished[0] = true;
            }
            if (!armFinished[0] && !arm.isBusy()) {
                arm.setPower(0);
                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                armFinished[0] = true;
            }
            return !railLimit.isPressed() || arm.isBusy(); // wait until both are done
        });
        armIsOut = true;
    }

    protected final void retractArm() {
        if (!armIsOut) {
            return;
        }
        clamp.setPosition(1);
        sleepWhile(400); // drop element
        pulley.setPower(0);
        arm.setTargetPosition(0);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.3);
        sleepWhile(500);
        pulley.setPower(0.4); // retreat downward
        sleepWhile(800);
        pulley.setPower(0);
        sleepWhile(arm::isBusy);
        arm.setPower(0);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armIsOut = false;
    }
}
