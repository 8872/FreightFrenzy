package org.firstinspires.ftc.teamcode.freightfrenzy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

abstract class AutonomousOpMode extends FreightFrenzyOpMode {

    {msStuckDetectInit = 10_000;}

    private static final ExecutorService pool = Executors.newSingleThreadExecutor();
    @SuppressWarnings("rawtypes")
    private Future opModeFuture;

    @Override
    public void start() {
        super.start();

        opModeFuture = pool.submit(() -> {
            try {
                runOpMode();
                requestOpModeStop();
            } catch (InterruptedException ignored) {
            }
        });
    }

    @Override
    public void stop() {
        super.stop();
        if (opModeFuture != null) {
            opModeFuture.cancel(true);
            opModeFuture = null;
        }
    }

    @Override
    public void loop() {}

    protected abstract void runOpMode() throws InterruptedException;
}
