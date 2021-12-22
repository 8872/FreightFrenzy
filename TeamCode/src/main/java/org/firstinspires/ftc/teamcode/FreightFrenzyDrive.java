package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class FreightFrenzyDrive extends FreightFrenzyOpMode {

    private boolean slowMode = false;
    private boolean lastAState, lastBumperState;
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
            carousel.setPower(-0.5);
        } else {
            carousel.setPower(0);
        }

        if (gamepad1.b) {
            carousel.setPower(-1);
        } else {
            carousel.setPower(0);
        }


        launcherArm.setPower(gamepad2.right_stick_y / 2);

        pulley.setPower(gamepad2.left_stick_y / 2);

        if (railLimit.isPressed()) {
            launcherArm.setPower(0.3);
        }


        if ((gamepad1.left_bumper && gamepad1.right_bumper) && !lastBumperState) {
            intake.setPower(0);
        } else if (gamepad1.left_bumper && !lastBumperState) {
            intake.setPower(intakePower);
        } else if (gamepad1.right_bumper && !lastBumperState) {
            intake.setPower(-intakePower);
        }
        lastBumperState = gamepad1.left_bumper || gamepad1.right_bumper;

        update();
    }

    @Override
    protected void initHardwareDevices() {

    }

    @Override
    protected void composeTelemetry() {
        super.composeTelemetry();
        telemetry.addData("Accelerating", () -> acceleratePower != 0);
        telemetry.addData("Slow Mode", () -> slowMode);
    }
}
