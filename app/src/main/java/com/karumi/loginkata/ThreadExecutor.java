package com.karumi.loginkata;

public class ThreadExecutor implements SessionApiClient.Executor{

    @Override
    public void post(Runnable run) {
        new Thread(run).start();
    }

    @Override
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {

        }
    }
}
