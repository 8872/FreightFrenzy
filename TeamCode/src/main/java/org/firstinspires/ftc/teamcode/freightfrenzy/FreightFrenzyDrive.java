package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class FreightFrenzyDrive extends FreightFrenzyOpMode {

    {msStuckDetectLoop = 30_000;}


    private boolean slowMode = false;
    private boolean lastAState, lastLeftStickState, lastBumperState1, lastBumperState2, lastBState1, lastBState2;
    private double acceleratePower = 0;

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

        if (gamepad1.a && !lastAState) {
            slowMode = !slowMode;
        }
        lastAState = gamepad1.a;

        if (gamepad1.y) {
            carousel.setPower(-carouselPower);
        } else {
            carousel.setPower(0);
        }

        if ((gamepad2.b && !lastBState2) || (gamepad1.b && !lastBState1)) {
            if (poolFuture == null || poolFuture.isDone()) {
                poolFuture = pool.submit(this::extendArm);
            }
        }
        lastBState1 = gamepad1.a;
        lastBState2 = gamepad2.a;

        // don't allow manual control of arm while automation is running
        if (poolFuture == null || poolFuture.isDone()) {
            pulley.setPower(gamepad2.left_stick_y / 2);

            arm.setPower(gamepad2.left_stick_x / 2);

            if (railLimit.isPressed()) {
                pulley.setPower(0.3);
            }

            if (gamepad2.left_stick_button && !lastLeftStickState && clamp.getPosition() != 0) {
                clamp.setPosition(0);
            } else if (gamepad2.left_stick_button && !lastLeftStickState) {
                clamp.setPosition(1);
            }
            lastLeftStickState = gamepad2.left_stick_button;
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

    @Override
    protected void composeTelemetry() {
        super.composeTelemetry();
        telemetry.addData("Accelerating", () -> acceleratePower != 0);
        telemetry.addData("Slow Mode", () -> slowMode);
    }
}
