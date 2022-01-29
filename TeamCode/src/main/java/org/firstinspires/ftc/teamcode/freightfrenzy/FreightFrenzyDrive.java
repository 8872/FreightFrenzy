package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class FreightFrenzyDrive extends FreightFrenzyOpMode {

    {
        msStuckDetectLoop = 30_000;
    }


    private boolean slowMode = false;
    private double acceleratePower = 0;
    private boolean lastLeftStickState1, lastLeftStickState2, lastBumperState1, lastBumperState2;
    private boolean lastDpadState, lastDpadState2;

    @Override
    public void loop() {
        if (gamepad1.right_stick_button) {
            acceleratePower = 0;
        }
        if (!drive.isBusy() && (gamepad1.x || acceleratePower != 0)) {
            acceleratePower = accelerate(gamepad1.x, acceleratePower);
        } else if (!drive.isBusy()) {
            mechanumDrive(slowMode);
        }

        if (gamepad1.left_stick_button && !lastLeftStickState1) {
            slowMode = !slowMode;
        }
        lastLeftStickState1 = gamepad1.left_stick_button;

        if (gamepad1.y) {
            carousel.setPower(carouselPower);
        } else {
            carousel.setPower(0);
        }


        // only allow one arm action at a time
        if (poolFuture == null || poolFuture.isDone()) {
            armLoop();
        }

        if (((gamepad1.left_bumper && gamepad1.right_bumper) && !lastBumperState1) || ((gamepad2.left_bumper && gamepad2.right_bumper) && !lastBumperState2)) {
            intake.setVelocity(0);
        } else if ((gamepad1.left_bumper && !lastBumperState1) || (gamepad2.left_bumper && !lastBumperState2)) {
            intake.setVelocity(intakeVelocity, AngleUnit.DEGREES);
        } else if ((gamepad1.right_bumper && !lastBumperState1) || (gamepad2.right_bumper && !lastBumperState2)) {
            intake.setVelocity(-intakeVelocity, AngleUnit.DEGREES);
        }
        lastBumperState1 = gamepad1.left_bumper || gamepad1.right_bumper;
        lastBumperState2 = gamepad2.left_bumper || gamepad2.right_bumper;

        update();
    }

    private void armLoop() {
        // Functionality for gamepad 1
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
        // Functionality for gamepad 2
        if (gamepad2.dpad_up && !lastDpadState2 && !armIsOut) {
            poolFuture = pool.submit(gamepad2.a ? () -> fullArmSequence(ArmPosition.TOP_GOAL) : () -> pullOutArm(ArmPosition.TOP_GOAL));
        } else if (gamepad2.dpad_right && !lastDpadState2 && !armIsOut) {
            poolFuture = pool.submit(gamepad2.a ? () -> fullArmSequence(ArmPosition.MIDDLE_GOAL) : () -> pullOutArm(ArmPosition.MIDDLE_GOAL));
        } else if (gamepad2.dpad_down && !lastDpadState2 && !armIsOut) {
            poolFuture = pool.submit(gamepad2.a ? () -> fullArmSequence(ArmPosition.BOTTOM_GOAL) : () -> pullOutArm(ArmPosition.BOTTOM_GOAL));
        } else if (gamepad2.dpad_left && !lastDpadState2 && armIsOut) {
            poolFuture = pool.submit(this::retractArm);
        }
        lastDpadState2 = gamepad2.dpad_up || gamepad2.dpad_right || gamepad2.dpad_down || gamepad2.dpad_left;


        if (!armLimit.isPressed() || (armLimit.isPressed() && gamepad2.left_stick_x < 0)) {
            arm.setPower(gamepad2.left_stick_x * armPower);
        } else {
            arm.setPower(0);
        }

        if (railTopLimit.isPressed() && gamepad2.left_stick_y <= 0) {
            pulley.setPower(pulleyIdlePower);
        } else if (railBottomLimit.isPressed() && gamepad2.left_stick_y >= 0) {
            pulley.setPower(0);
        } else if (!armIsOut) {
            pulley.setPower(gamepad2.left_stick_y / 2);
        }

        if (gamepad2.left_stick_button && !lastLeftStickState2 && clamp.getPosition() != 0) {
            clamp.setPosition(0);
        } else if (gamepad2.left_stick_button && !lastLeftStickState2) {
            clamp.setPosition(1);
        }
        lastLeftStickState2 = gamepad2.left_stick_button;
    }

    @Override
    protected void composeTelemetry() {
        super.composeTelemetry();
        telemetry.addData("Accelerating", () -> acceleratePower != 0);
        telemetry.addData("Slow Mode", () -> slowMode);
    }
}
