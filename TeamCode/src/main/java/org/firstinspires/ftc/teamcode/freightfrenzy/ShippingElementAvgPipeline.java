package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.acmerobotics.dashboard.config.Config;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Config
public class ShippingElementAvgPipeline extends OpenCvPipeline {
    enum ShippingElementPosition {
        LEFT,
        CENTER,
        RIGHT
    }

    ShippingElementPosition position;

    public static int lower1 = 148;
    public static int lower2 = 148;
    public static int lower3 = 148;

    public static int higher1 = 255;
    public static int higher2 = 255;
    public static int higher3 = 255;

    Scalar lower = new Scalar(lower1, lower2, lower3);
    Scalar higher = new Scalar(higher1, higher2, higher3);

    // Scalar lower = new Scalar(0);
    // Scalar higher = new Scalar(100);

    Mat YCbCrMat = new Mat();
    Mat CbMat = new Mat();

    // Change channel 1 for Cr channel. The orange-red will appear white since there is a small difference
    public static int channel = 1;
    public static boolean inRange = true;
    public static boolean clean = true;
    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar GREEN = new Scalar(0, 255, 0);

    public static boolean returnCbMat = false;

    public static int region1x, region1y, region2x, region2y, region3x, region3y;
    static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(650, 650); // center
    static final Point REGION2_TOPLEFT_ANCHOR_POINT = new Point(1250, 650); // left
    static final Point REGION3_TOPLEFT_ANCHOR_POINT = new Point(200, 650); // right
    static final int REGION_WIDTH = 100;
    static final int REGION_HEIGHT = 100;

    Point region1_pointA = new Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x,
            REGION1_TOPLEFT_ANCHOR_POINT.y);
    Point region1_pointB = new Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
    Point region2_pointA = new Point(
            REGION2_TOPLEFT_ANCHOR_POINT.x,
            REGION2_TOPLEFT_ANCHOR_POINT.y);
    Point region2_pointB = new Point(
            REGION2_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION2_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
    Point region3_pointA = new Point(
            REGION3_TOPLEFT_ANCHOR_POINT.x,
            REGION3_TOPLEFT_ANCHOR_POINT.y);
    Point region3_pointB = new Point(
            REGION3_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION3_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    Mat region1_Cb, region2_Cb, region3_Cb;
    int avg1, avg2, avg3;

    @Override
    public void init(Mat firstFrame) {
        inputToCb(firstFrame);


        region1_Cb = CbMat.submat(new Rect(region1_pointA, region1_pointB));
        region2_Cb = CbMat.submat(new Rect(region2_pointA, region2_pointB));
        region3_Cb = CbMat.submat(new Rect(region3_pointA, region3_pointB));

    }

    void inputToCb(Mat input) {
        Imgproc.cvtColor(input, YCbCrMat, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCbCrMat, CbMat, channel);
    }

    @Override
    public Mat processFrame(Mat input) {

        inputToCb(input);

        if (inRange) {
            Core.inRange(CbMat, lower, higher, CbMat);
        }

        if (clean) {
            Imgproc.morphologyEx(CbMat, CbMat, Imgproc.MORPH_OPEN, new Mat());
            Imgproc.morphologyEx(CbMat, CbMat, Imgproc.MORPH_CLOSE, new Mat());
        }

        avg1 = (int) Core.mean(region1_Cb).val[0]; // center
        avg2 = (int) Core.mean(region2_Cb).val[0]; // left
        avg3 = (int) Core.mean(region3_Cb).val[0]; // right

        Imgproc.rectangle(
                input, // Buffer to draw on
                region1_pointA, // First point which defines the rectangle
                region1_pointB, // Second point which defines the rectangle
                BLUE, // The color the rectangle is drawn in
                -1); // Thickness of the rectangle lines

        Imgproc.rectangle(
                input, // Buffer to draw on
                region2_pointA, // First point which defines the rectangle
                region2_pointB, // Second point which defines the rectangle
                BLUE, // The color the rectangle is drawn in
                -1); // Thickness of the rectangle lines

        Imgproc.rectangle(
                input, // Buffer to draw on
                region3_pointA, // First point which defines the rectangle
                region3_pointB, // Second point which defines the rectangle
                BLUE, // The color the rectangle is drawn in
                -1); // Thickness of the rectangle lines

        int maxOneTwo = Math.max(avg1, avg2);
        int max = Math.max(maxOneTwo, avg3);


        if (max == avg1) {
            position = ShippingElementPosition.CENTER; // Record our analysis
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill
        } else if (max == avg2) {
            position = ShippingElementPosition.LEFT; // Record our analysis

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region2_pointA, // First point which defines the rectangle
                    region2_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill
        } else {
            position = ShippingElementPosition.RIGHT; // Record our analysis

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region3_pointA, // First point which defines the rectangle
                    region3_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill
        }

        if (returnCbMat) {
            return CbMat;
        }

        return input;
    }

    public ShippingElementPosition getAnalysis() {
        return position;
    }

    public int[] getAvg() {
        return new int[]{avg1, avg2, avg3};
    }
}
