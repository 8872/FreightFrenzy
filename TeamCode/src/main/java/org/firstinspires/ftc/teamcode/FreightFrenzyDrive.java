package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class FreightFrenzyDrive extends FreightFrenzyOpMode {

    {msStuckDetectLoop = 30_000;}


    private boolean slowMode = false;
    private boolean lastAState, lastLeftStickState, lastBumperState, lastAState2;
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

        if (gamepad2.a && !lastAState2) {
            // TODO: run code from ArmAutomation
        }
        lastAState2 = gamepad1.a;

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

        if ((gamepad1.left_bumper && gamepad1.right_bumper) && !lastBumperState) {
            intake.setVelocity(0);
        } else if (gamepad1.left_bumper && !lastBumperState) {
            intake.setVelocity(intakeVelocity, AngleUnit.DEGREES);
        } else if (gamepad1.right_bumper && !lastBumperState) {
            intake.setVelocity(-intakeVelocity, AngleUnit.DEGREES);
        }
        lastBumperState = gamepad1.left_bumper || gamepad1.right_bumper;

        update();
    }

    @Override
    protected void composeTelemetry() {
        super.composeTelemetry();
        telemetry.addData("Accelerating", () -> acceleratePower != 0);
        telemetry.addData("Slow Mode", () -> slowMode);
    }
}
