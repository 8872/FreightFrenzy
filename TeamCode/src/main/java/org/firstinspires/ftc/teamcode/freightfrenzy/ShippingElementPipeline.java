package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.dashboard.config.Config;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Config
public class ShippingElementPipeline extends OpenCvPipeline {
    enum ShippingElementPosition {
        LEFT,
        CENTER,
        RIGHT
    }

    public static int lower1, lower2, lower3, lower4 = 100;
    public static int higher1, higher2, higher3, higher4 = 255;
    Scalar lower = new Scalar(lower1, lower2, lower3, lower4);
    Scalar higher = new Scalar(higher1, higher2, higher3, higher4);


    Mat YCbCrMat = new Mat();
    Mat CbMat = new Mat();

    public static int channel = 2;

    @Override
    public Mat processFrame(Mat input) {


        Imgproc.cvtColor(input, YCbCrMat, Imgproc.COLOR_RGBA2BGR);
        Imgproc.cvtColor(YCbCrMat, YCbCrMat, Imgproc.COLOR_BGR2YCrCb);
        Core.extractChannel(YCbCrMat, CbMat, channel);
        Core.inRange(CbMat, lower, higher, CbMat);
        Imgproc.morphologyEx(CbMat, CbMat, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(CbMat, CbMat, Imgproc.MORPH_CLOSE, new Mat());

        return CbMat;
    }
}
