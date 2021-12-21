package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

public class FreightFrenzyDrive extends BaseOpMode {

    private DcMotor carousel, intake, launcherArm, pulley;
    private TouchSensor railLimit;

    private boolean slowMode = false;
    private boolean lastBumperState;
    public static double intakePower = 0.5;

    @Override
    public void loop() {
        mechanumDrive(slowMode);

        if (gamepad1.a) {
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
        telemetry.addData("carousel", carousel::getPower);
        telemetry.addData("intake", intake::getPower);
        telemetry.addData("launcherArm", launcherArm::getPower);
    }
}
