package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Config
abstract class FreightFrenzyOpMode extends BaseOpMode {

    public static double intakeVelocity = 90, carouselPower = 0.5;

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
        telemetry.addData("procs", Runtime.getRuntime()::availableProcessors);
    }

    @Override
    protected void initHardwareDevices() {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    protected void extendArm() {
        clamp.setPosition(0);
        whileSleep(1000);
        pulley.setPower(-0.5);
        whileSleep(() -> !railLimit.isPressed());
        pulley.setPower(-0.3);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setTargetPosition(-2200);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(-0.3);
        whileSleep(arm::isBusy);
        arm.setPower(0);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        whileSleep(1000);
        clamp.setPosition(1.0);
        whileSleep(2000);
        pulley.setPower(0);
        arm.setTargetPosition(0);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.3);
        whileSleep(500);
        pulley.setPower(0.4); // retreat downward
        whileSleep(800);
        pulley.setPower(0);
        whileSleep(arm::isBusy);
        arm.setPower(0);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        System.out.println("Ran to target");
    }
}
