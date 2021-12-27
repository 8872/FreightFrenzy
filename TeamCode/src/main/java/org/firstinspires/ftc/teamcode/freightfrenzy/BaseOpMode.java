package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BooleanSupplier;

public abstract class BaseOpMode extends OpMode {

    protected SampleMecanumDrive drive;
    protected final Telemetry dashTelemetry = FtcDashboard.getInstance().getTelemetry();

    protected DcMotor leftRear, rightRear, leftFront, rightFront;
    protected BNO055IMU imu;

    protected static final ExecutorService pool = Executors.newSingleThreadExecutor();
    @SuppressWarnings("rawtypes")
    protected Future poolFuture;

    @Override
    public final void init() {
        drive = new SampleMecanumDrive(hardwareMap);
        hardwareMap.voltageSensor.forEach(voltageSensor -> {
            if (voltageSensor.getVoltage() < 12.7) {
                RobotLog.addGlobalWarningMessage("Battery voltage is very low. Motors may not run at full speed");
            }
        });

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);

        initHardware();

        brake(); // drive motors are already configured by SampleMecanumDrive

        initHardwareDevices();

        composeTelemetry();

        telemetry.addData("Mode", "Done initializing");
        telemetry.update();
    }

    protected abstract void initHardwareDevices();

    protected void update() {
        telemetry.update();
        drive.update();
    }

    protected void brake() {
        leftRear.setPower(0);
        rightRear.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);
    }

    protected void composeTelemetry() {
        telemetry.addData("leftFront", () -> round(leftFront.getPower()));
        telemetry.addData("leftRear", () -> round(leftRear.getPower()));
        telemetry.addData("rightFront", () -> round(rightFront.getPower()));
        telemetry.addData("rightRear", () -> round(rightRear.getPower()));
        telemetry.addData("x", () -> imu.getPosition().x);
        telemetry.addData("y", () -> imu.getPosition().y);
        telemetry.addData("Imu Heading", () -> imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
    }

    protected void mechanumDrive(boolean slowMode) {
        mechanumDrive(slowMode, false, false);
    }

    @SuppressWarnings("SameParameterValue")
    protected void mechanumDrive(boolean slowMode, boolean xDisable, boolean yDisable) {
        double horizontal = -gamepad1.left_stick_x;
        double vertical = -gamepad1.left_stick_y;
        double angle = -gamepad1.right_stick_x;

        if (xDisable) {
            horizontal = 0;
        }
        if (yDisable) {
            vertical = 0;
        }
        if (slowMode) {
            horizontal = horizontal / 2;
            vertical = vertical / 2;
            angle = angle / 2;
        }

        drive.setWeightedDrivePower(vertical, horizontal, angle); // in roadrunner x is vertical and y is horizontal
        drive.update();
    }

    protected double accelerate(boolean gamepadButton, double acceleratePower) {
        if (gamepadButton && acceleratePower < 0.5) {
            acceleratePower += 0.001;
            drive.setMotorPowers(acceleratePower, acceleratePower, acceleratePower, acceleratePower);
        } else if (!gamepadButton && acceleratePower > 0) {
            acceleratePower -= 0.001;
            if (acceleratePower <= 0) {
                acceleratePower = 0;
            }
            drive.setMotorPowers(acceleratePower, acceleratePower, acceleratePower, acceleratePower);
        }
        drive.update();

        return acceleratePower;
    }

    protected static double toRPM(double degreesPerSecond) {
        return degreesPerSecond / 6;
    }

    private static double round(double value) {
        return round(value, 4);
    }

    /**
     * Rounds value to the specified number of decimal places.
     */
    private static double round(double value, @SuppressWarnings("SameParameterValue") int places) {
        if (places < 0) throw new IllegalArgumentException();

        return new BigDecimal(Double.toString(value)).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    @SuppressWarnings("SameParameterValue")
    protected final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected void whileSleep(long millis) {
        final long end = System.currentTimeMillis() + millis;
        whileSleep(() -> System.currentTimeMillis() < end);
    }

    protected void whileSleep(BooleanSupplier supplier) {
        while (!isStopRequested() && supplier.getAsBoolean()) {
            Thread.yield();
            update();
            sleep(50);
            Thread.yield();
        }
    }

    protected void waitForRoadRunnerIdle() {
        whileSleep(drive::isBusy);
    }

    protected final boolean isStopRequested() {
        return Thread.currentThread().isInterrupted();
    }

    @Override
    public void stop() {
        super.stop();
        if (poolFuture != null) {
            poolFuture.cancel(true);
            poolFuture = null;
        }
    }

    /**
     * This method takes all fields in this class and subclasses of type
     */
    @SuppressWarnings("rawtypes")
    private void initHardware() {
        Class clazz = getClass();
        var fields = new HashSet<Field>();
        while (clazz != null && clazz != OpMode.class) { // only BaseOpMode and subclasses
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }

        for (Field field : fields) {
            // if the field is of type HardwareDevice
            if (HardwareDevice.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    // if field is not already set
                    if (field.get(this) == null) {
                        field.set(this, hardwareMap.get(field.getType(), field.getName()));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
