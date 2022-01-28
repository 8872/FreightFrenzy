package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Config
abstract class FreightFrenzyOpMode extends BaseOpMode {

    public static double intakeVelocity = 90, carouselPower = -0.5, pulleyIdlePower = -0.1, armPower = 0.5;

    protected boolean armIsOut = false;

    protected DcMotor carousel;
    protected DcMotorEx intake, pulley, arm;
    protected Servo clamp;
    protected TouchSensor railTopLimit, railBottomLimit, armLimit;

    @Override
    protected void composeTelemetry() {
        super.composeTelemetry();
        telemetry.addData("armLimit", armLimit::isPressed);
        telemetry.addData("railTopLimit", railTopLimit::isPressed);
        telemetry.addData("railBottomLimit", railBottomLimit::isPressed);
        telemetry.addData("carousel", carousel::getPower);
        telemetry.addData("intake", intake::getPower);
        telemetry.addData("intake velocity", () -> intake.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("pulley", pulley::getPower);
        telemetry.addData("arm", arm::getPower);
        telemetry.addData("arm pos", arm::getCurrentPosition);
        telemetry.addData("arm velocity", arm::getVelocity);
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
        public static final int TOP_GOAL = -1600, MIDDLE_GOAL = -1900, BOTTOM_GOAL = -2200;
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
        intake.setVelocity(0);
        sleepWhile(300);
        pulley.setPower(-0.4);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setTargetPosition(armPosition);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (armPosition < ArmPosition.MIDDLE_GOAL) {
            sleep(500);
        }
        arm.setPower(armPower);
        boolean[] pulleyFinished = {false}, armFinished = {false}; // using array so vars can still be effectively final
        long endTimeout = System.currentTimeMillis() + 5000;
        sleepWhile(() -> {
            if (!pulleyFinished[0] && railTopLimit.isPressed()) {
                pulley.setPower(pulleyIdlePower);
                pulleyFinished[0] = true;
            }
            if (!armFinished[0] && !arm.isBusy()) {
                arm.setPower(0);
                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                armFinished[0] = true;
            }
            if (System.currentTimeMillis() > endTimeout) {
                arm.setPower(0);
                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                pulley.setPower(pulleyIdlePower);
                RobotLog.addGlobalWarningMessage("Arm/pulley movement timed out. Check the rail limit switch or arm encoder.");
                return false;
            } else {
                return !pulleyFinished[0] || !armFinished[0]; // wait until both are done
            }
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
        arm.setPower(armPower);
        if (arm.getCurrentPosition() < ArmPosition.MIDDLE_GOAL - 100) {
            sleepWhile(500);
        }
        sleepWhile(500);
        pulley.setPower(0.5); // retreat downward
        boolean[] pulleyFinished = {false}, armFinished = {false}; // using array so vars can still be effectively final
        long endTimeout = System.currentTimeMillis() + 9000;
        sleepWhile(() -> {
            if (!pulleyFinished[0] && railBottomLimit.isPressed()) {
                pulley.setPower(0);
                pulleyFinished[0] = true;
            }
            if (!armFinished[0] && !arm.isBusy()) {
                arm.setPower(0);
                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                armFinished[0] = true;
            }
            if (System.currentTimeMillis() > endTimeout) {
                arm.setPower(0);
                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                pulley.setPower(0);
                RobotLog.addGlobalWarningMessage("Arm/pulley movement timed out. Check the limit switches or arm encoder.");
                return false;
            } else {
                return !pulleyFinished[0] || !armFinished[0]; // wait until both are done
            }
        });
        arm.setPower(0);
        armIsOut = false;
    }
}
