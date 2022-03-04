package org.firstinspires.ftc.teamcode.roadrunner.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IMULocalizer implements Localizer {

    private static BNO055IMU imu;
    private static Pose2d pose2d = new Pose2d();

    public IMULocalizer(HardwareMap hardwareMap) {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
    }

    @NotNull
    @Override
    public Pose2d getPoseEstimate() {

        pose2d = new Pose2d(pose2d.getX() + imu.getPosition().x, pose2d.getY() + imu.getPosition().y, pose2d.getHeading() + imu.getAngularOrientation().firstAngle);
        return pose2d;
    }

    @Override
    public void setPoseEstimate(@NotNull Pose2d pose2d) {
        this.pose2d = pose2d;
    }

    @Nullable
    @Override
    public Pose2d getPoseVelocity() {
        return null;
    }

    @Override
    public void update() {

    }
}
