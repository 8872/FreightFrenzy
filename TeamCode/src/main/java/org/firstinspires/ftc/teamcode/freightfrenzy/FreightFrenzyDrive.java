package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.ArrayList;
import java.util.concurrent.Callable;

@TeleOp
public class FreightFrenzyDrive extends FreightFrenzyOpMode {

    {msStuckDetectLoop = 30_000;}


    private boolean slowMode = false;
    private double acceleratePower = 0;
    private boolean lastLeftStickState1, lastLeftStickState2, lastBumperState1;
    private boolean lastDpadState;

    @Override
    public void loop() {
        if (gamepad1.right_stick_button) {
            acceleratePower = 0;
        }
        if (!drive.isBusy() && (gamepad1.x || acceleratePower != 0)) {
            acceleratePower = accelerate(gamepad1.x, acceleratePower);
        } else if (!drive.isBusy()) {
            mechanumDrive(slowMode, false, false);
        }

        if (gamepad1.left_stick_button && !lastLeftStickState1) {
            slowMode = !slowMode;
        }
        lastLeftStickState1 = gamepad1.left_stick_button;

        if (gamepad1.y) {
            carousel.setPower(-carouselPower);
        } else {
            carousel.setPower(0);
        }


        // only allow one arm action at a time
        if (poolFuture == null || poolFuture.isDone()) {
            if (gamepad1.dpad_up && !lastDpadState && !armIsOut) {
                poolFuture = pool.submit(gamepad1.a ? () -> fullArmSequence(ArmPosition.TOP_GOAL) : () -> pullOutArm(ArmPosition.TOP_GOAL));
            } else if (gamepad1.dpad_right && !lastDpadState && !armIsOut) {
                poolFuture = pool.submit(gamepad1.a ? () -> fullArmSequence(ArmPosition.MIDDLE_GOAL) : () -> pullOutArm(ArmPosition.MIDDLE_GOAL));
            } else if (gamepad1.dpad_down && !lastDpadState && !armIsOut) {
                poolFuture = pool.submit(gamepad1.a ? () -> fullArmSequence(ArmPosition.BOTTOM_GOAL) : () -> pullOutArm(ArmPosition.BOTTOM_GOAL));
            } else if (gamepad1.dpad_left && !lastDpadState && armIsOut) {
                poolFuture = pool.submit(this::retractArm);
            }
            lastDpadState = gamepad1.dpad_up || gamepad1.dpad_right || gamepad1.dpad_down || gamepad1.dpad_left;

            arm.setPower(gamepad2.left_stick_x / 2);

            if (railLimit.isPressed() && gamepad2.left_stick_y <= 0) {
                pulley.setPower(pulleyIdlePower);
            } else {
                pulley.setPower(gamepad2.left_stick_y / 2);
            }

            if (gamepad2.left_stick_button && !lastLeftStickState2 && clamp.getPosition() != 0) {
                clamp.setPosition(0);
            } else if (gamepad2.left_stick_button && !lastLeftStickState2) {
                clamp.setPosition(1);
            }
            lastLeftStickState2 = gamepad2.left_stick_button;
        }


        if (gamepad1.left_bumper && gamepad1.right_bumper && !lastBumperState1) {
            intake.setVelocity(0);
        } else if (gamepad1.left_bumper && !lastBumperState1) {
            intake.setVelocity(intakeVelocity, AngleUnit.DEGREES);
        } else if (gamepad1.right_bumper && !lastBumperState1) {
            intake.setVelocity(-intakeVelocity, AngleUnit.DEGREES);
        }
        lastBumperState1 = gamepad1.left_bumper || gamepad1.right_bumper;

        update();
    }

    @Override
    protected void composeTelemetry() {
        super.composeTelemetry();
        telemetry.addData("Accelerating", () -> acceleratePower != 0);
        telemetry.addData("Slow Mode", () -> slowMode);
    }
}
