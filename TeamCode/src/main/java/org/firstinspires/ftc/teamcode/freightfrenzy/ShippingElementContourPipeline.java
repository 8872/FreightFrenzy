package org.firstinspires.ftc.teamcode.freightfrenzy;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class ShippingElementContourPipeline extends OpenCvPipeline {

    public static int channel = 2;
    Mat YCbCrMat = new Mat();
    Mat CbMat = new Mat();

    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar GREEN = new Scalar(0, 255, 0);

    public static int lower1 = 100;
    public static int lower2 = 100;
    public static int lower3 = 100;

    public static int higher1 = 255;
    public static int higher2 = 255;
    public static int higher3 = 255;

    Scalar lower = new Scalar(lower1, lower2, lower3);
    Scalar higher = new Scalar(higher1, higher2, higher3);

    @Override
    public Mat processFrame(Mat input) {
        inputToCb(input);

        Core.inRange(CbMat, lower, higher, CbMat);
        Imgproc.morphologyEx(CbMat, CbMat, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(CbMat, CbMat, Imgproc.MORPH_CLOSE, new Mat());
        Imgproc.GaussianBlur(CbMat, CbMat, new Size(5.0, 5.0), 0);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(CbMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(CbMat, contours, -1, GREEN, 2);

        List<Moments> mu = new ArrayList<>(contours.size());
        for (int i = 0; i < contours.size(); i++) {
            mu.add(i, Imgproc.moments(contours.get(i), false));
            Moments p = mu.get(i);
            
        }


        return CbMat;
    }

    void inputToCb(Mat input) {
        Imgproc.cvtColor(input, YCbCrMat, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCbCrMat, CbMat, channel);
    }
}
