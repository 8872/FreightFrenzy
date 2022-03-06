package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.hardware.TouchSensor;

public class BadTouchSensor implements TouchSensor {
    public static final BadTouchSensor INSTANCE = new BadTouchSensor();

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Other;
    }

    @Override
    public String getDeviceName() {
        return "";
    }

    @Override
    public String getConnectionInfo() {
        return "";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }

    @Override
    public double getValue() {
        return 0;
    }

    @Override
    public boolean isPressed() {
        return false;
    }
}
