package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class MecanumTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor leftFront = hardwareMap.dcMotor.get("leftFront");
        DcMotor leftRear = hardwareMap.dcMotor.get("leftRear");
        DcMotor rightFront = hardwareMap.dcMotor.get("rightFront");
        DcMotor rightRear = hardwareMap.dcMotor.get("rightRear");
        telemetry.addData("leftFront", leftFront::getPower);
        telemetry.addData("leftRear", leftRear::getPower);
        telemetry.addData("rightFront", rightFront::getPower);
        telemetry.addData("rightRear", rightRear::getPower);
        telemetry.addData("left Front", () -> Math.signum(leftFront.getPower()));
        telemetry.addData("left Rear", () -> Math.signum(leftRear.getPower()));
        telemetry.addData("right Front", () -> Math.signum(rightFront.getPower()));
        telemetry.addData("right Rear", () -> Math.signum(rightRear.getPower()));

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;
        telemetry.addData("leftFront", leftFront.getConnectionInfo());
        telemetry.addData("leftRear", leftRear.getConnectionInfo());
        telemetry.addData("rightFront", rightFront.getConnectionInfo());
        telemetry.addData("rightRear", rightRear.getConnectionInfo());

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            leftFront.setPower(frontLeftPower);
            leftRear.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightRear.setPower(backRightPower);
            telemetry.update();
        }
    }
}