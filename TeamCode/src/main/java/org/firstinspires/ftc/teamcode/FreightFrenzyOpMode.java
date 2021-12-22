package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@Config
abstract class FreightFrenzyOpMode extends BaseOpMode {

    public static double intakePower = 0.5;

    protected DcMotor carousel, intake, launcherArm, pulley;
    protected TouchSensor railLimit;

    @Override
    protected void composeTelemetry() {
        super.composeTelemetry();
        telemetry.addData("carousel", carousel::getPower);
        telemetry.addData("intake", intake::getPower);
        telemetry.addData("launcherArm", launcherArm::getPower);
    }

}
