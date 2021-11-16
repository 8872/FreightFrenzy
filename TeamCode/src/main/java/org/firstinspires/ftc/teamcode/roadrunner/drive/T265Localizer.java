package org.firstinspires.ftc.teamcode.roadrunner.drive;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Transform2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.spartronics4915.lib.T265Camera;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class T265Localizer implements Localizer {

    // We treat this like a singleton because there should only ever be one object per camera
    private static T265Camera slamra = null;

    private final FtcDashboard dashboard = FtcDashboard.getInstance();


    public T265Localizer(HardwareMap hardwareMap) {
        if (slamra == null) {
            slamra = new T265Camera(new Transform2d(), 0.1, hardwareMap.appContext);
        }
        slamra.start();
    }

    @NotNull
    @Override
    public Pose2d getPoseEstimate() {
        return new Pose2d(internalPose().getX(), internalPose().getY(), internalPose().getHeading());
    }

    @Override
    public void setPoseEstimate(@NotNull Pose2d pose2d) {
        slamra.setPose(new com.arcrobotics.ftclib.geometry.Pose2d(pose2d.getX(), pose2d.getY(), new Rotation2d(pose2d.getHeading())));
    }

    @Nullable
    @Override
    public Pose2d getPoseVelocity() {
        return new Pose2d(slamra.getLastReceivedCameraUpdate().velocity.vxMetersPerSecond, slamra.getLastReceivedCameraUpdate().velocity.vyMetersPerSecond);
    }

    @Override
    public void update() {

    }

    private static com.arcrobotics.ftclib.geometry.Pose2d internalPose() {
        return slamra.getLastReceivedCameraUpdate().pose;
    }

}
