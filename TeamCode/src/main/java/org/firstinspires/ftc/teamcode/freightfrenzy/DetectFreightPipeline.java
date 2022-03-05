package org.firstinspires.ftc.teamcode.freightfrenzy;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class DetectFreightPipeline extends OpenCvPipeline {

    Mat hsv = new Mat();
    List<MatOfPoint> contours = new ArrayList<>();
    Mat hierarchy = new Mat();

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);
        Imgproc.GaussianBlur(hsv, hsv, new Size(5, 5), 1);
        Imgproc.findContours(hsv, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        return hsv;
    }
}
