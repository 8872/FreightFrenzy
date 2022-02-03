package org.firstinspires.ftc.teamcode.freightfrenzy;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class ShippingElementPipeline extends OpenCvPipeline {
    enum ShippingElementPosition {
        LEFT,
        CENTER,
        RIGHT
    }

    Mat grey = new Mat();

    @Override
    public Mat processFrame(Mat input) {

        Imgproc.cvtColor(input, grey, Imgproc.COLOR_RGB2GRAY);

        return grey;
    }
}
