package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.dashboard.config.Config;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Config
public class ShippingElementPipeline extends OpenCvPipeline {
    enum ShippingElementPosition {
        LEFT,
        CENTER,
        RIGHT
    }

    Mat YCbCrMat = new Mat();
    Mat CbMat = new Mat();

    public static int channel = 2;

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, YCbCrMat, Imgproc.COLOR_RGBA2BGR);
        Imgproc.cvtColor(YCbCrMat, YCbCrMat, Imgproc.COLOR_BGR2YCrCb);
        Core.extractChannel(YCbCrMat, CbMat, channel);

        return CbMat;
    }
}
