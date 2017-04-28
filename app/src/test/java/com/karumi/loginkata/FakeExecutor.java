package com.karumi.loginkata;

public class FakeExecutor implements SessionApiClient.Executor {

    // Llamando al run() haemos que el rest funcione de forma SINCRONA (lo que es necesario para no
    // obtener resultados raros)
    @Override
    public void post(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void sleep(long millis) {

    }
}
