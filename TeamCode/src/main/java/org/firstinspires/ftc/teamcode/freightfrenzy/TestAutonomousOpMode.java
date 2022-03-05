package org.firstinspires.ftc.teamcode.freightfrenzy;

abstract class TestAutonomousOpMode extends BaseOpMode {
    {
        msStuckDetectInit = 10_000;
    }

    protected boolean waitUntilRequestStop = false;


    @Override
    public void start() {
        super.start();

        poolFuture = pool.submit(() -> {
            try {
                runOpMode();
                if (!waitUntilRequestStop) {
                    requestOpModeStop();
                }
            } catch (InterruptedException ignored) {
            }
        });
    }

    @Override
    public void loop() {
        update();
    }

    protected abstract void runOpMode() throws InterruptedException;
}
