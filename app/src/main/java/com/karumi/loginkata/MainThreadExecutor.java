package com.karumi.loginkata;

import android.os.Handler;
import android.os.Looper;

public class MainThreadExecutor implements SessionApiClient.Executor {

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void post(Runnable runnable) {
        mainThreadHandler.post(runnable);
    }

    @Override
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {

        }
    }
}
