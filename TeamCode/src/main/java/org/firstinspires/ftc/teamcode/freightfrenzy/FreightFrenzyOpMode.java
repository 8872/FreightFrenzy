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
    protected TouchSensor railLimit, armLimit;

    @Override
    protected void composeTelemetry() {
        super.composeTelemetry();
        telemetry.addData("touch sensor", armLimit::isPressed);
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

        clamp.setPosition(1);
    }

    protected static class ArmPosition {
        public static final int TOP_GOAL = -2200, MIDDLE_GOAL = -2700, BOTTOM_GOAL = -3000;
    }

    protected final void fullArmSequence(int armPosition) {
        pullOutArm(armPosition);
        retractArm();
    }

    protected void pullOutArm(int armPosition) {
        if (armIsOut) {
            return;
        }
        clamp.setPosition(0);
        sleepWhile(300);
        pulley.setPower(-0.4);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setTargetPosition(armPosition);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (armPosition < ArmPosition.MIDDLE_GOAL) {
            sleep(500);
        }
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
            return !pulleyFinished[0] || !armFinished[0]; // wait until both are done
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
        arm.setPower(0.6);
        if (arm.getCurrentPosition() < ArmPosition.MIDDLE_GOAL - 100) {
            sleepWhile(500);
        }
        sleepWhile(500);
        pulley.setPower(0.5); // retreat downward
        sleepWhile(800);
        pulley.setPower(0);
        sleepWhile(() -> !armLimit.isPressed());
        arm.setPower(0);
        armIsOut = false;
    }
}
