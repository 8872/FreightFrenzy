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
    public static final boolean ENABLE_T265 = true;

    private final FtcDashboard dashboard = FtcDashboard.getInstance();


    public T265Localizer(HardwareMap hardwareMap) {
        if (slamra == null && ENABLE_T265) {
            slamra = new T265Camera(new Transform2d(), 0.1, hardwareMap.appContext);
            slamra.start();
        }

    }

    @NotNull
    @Override
    public Pose2d getPoseEstimate() {
        if (ENABLE_T265) {
            return new Pose2d(toInches(internalPose().getX()), toInches(internalPose().getY()), internalPose().getHeading());
        } else {
            return new Pose2d();
        }
    }

    @Override
    public void setPoseEstimate(@NotNull Pose2d pose2d) {
        if (ENABLE_T265) {
            slamra.setPose(new com.arcrobotics.ftclib.geometry.Pose2d(toMeters(pose2d.getX()), toMeters(pose2d.getY()), new Rotation2d((pose2d.getHeading()))));
        }
    }

    @Nullable
    @Override
    public Pose2d getPoseVelocity() {
        if (ENABLE_T265) {
            return new Pose2d(slamra.getLastReceivedCameraUpdate().velocity.vxMetersPerSecond / 0.0254, slamra.getLastReceivedCameraUpdate().velocity.vyMetersPerSecond / 0.0254);
        } else {
            return new Pose2d();
        }
    }

    @Override
    public void update() {

    }

    private static com.arcrobotics.ftclib.geometry.Pose2d internalPose() {
        if (ENABLE_T265) {
            return slamra.getLastReceivedCameraUpdate().pose;
        } else {
            return new com.arcrobotics.ftclib.geometry.Pose2d();
        }
    }

    public static double toInches(double meters) {
        return meters / 0.0254;
    }

    public static double toMeters(double inches) {
        return inches * 0.0254;
    }

}
