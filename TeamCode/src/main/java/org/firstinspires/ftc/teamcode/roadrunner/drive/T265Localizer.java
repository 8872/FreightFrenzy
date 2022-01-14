package org.firstinspires.ftc.teamcode.roadrunner.drive;

import android.util.Log;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.arcrobotics.ftclib.geometry.Transform2d;
import com.intel.realsense.librealsense.UsbUtilities;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.spartronics4915.lib.T265Camera;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Config
public class T265Localizer implements Localizer {

    // We treat this like a singleton because there should only ever be one object per camera
    private static T265Camera slamra = null;
    public static boolean ENABLE_T265 = true;

    private static final com.arcrobotics.ftclib.geometry.Pose2d origin = new com.arcrobotics.ftclib.geometry.Pose2d();

    private static volatile Pose2d translation = new Pose2d();

    public T265Localizer(HardwareMap hardwareMap) {
        if (ENABLE_T265 && slamra == null) {
            slamra = new T265Camera(new Transform2d(), 0.1, hardwareMap.appContext);
            UsbUtilities.grantUsbPermissionIfNeeded(hardwareMap.appContext);
            slamra.start();
        }
        slamra.setPose(origin);
    }

    @NotNull
    @Override
    public Pose2d getPoseEstimate() {
        if (ENABLE_T265) {
            var internalPose = internalPose();
            var untranslated = new Pose2d(toInches(internalPose.getX()), toInches(internalPose.getY()), internalPose.getHeading());
            var translated = untranslated.plus(translation);
//            Log.v("PoseEstimate", "untranslated = " + untranslated);
//            Log.v("PoseEstimate", "translated = " + translated);
            Log.v("T265", String.valueOf(untranslated.getHeading()));
            return translated;
        } else {
            return translation;
        }
    }

    @Override
    public void setPoseEstimate(@NotNull Pose2d newPose) {
        translation = new Pose2d(newPose.getX(), newPose.getY(), 0);
        if (ENABLE_T265) {
            slamra.setPose(origin);
            System.out.println("New pose: " + internalPose());
        }
    }

    @Nullable
    @Override
    public Pose2d getPoseVelocity() {
        if (ENABLE_T265) {
            return new Pose2d(toInches(slamra.getLastReceivedCameraUpdate().velocity.vxMetersPerSecond), toInches(slamra.getLastReceivedCameraUpdate().velocity.vyMetersPerSecond), slamra.getLastReceivedCameraUpdate().velocity.omegaRadiansPerSecond);
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
