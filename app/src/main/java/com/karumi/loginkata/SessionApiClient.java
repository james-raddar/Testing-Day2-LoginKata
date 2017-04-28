package com.karumi.loginkata;

public class SessionApiClient {

    public static final String VALID_USERNAME = "jaimegmail.com";
    public static final String VALID_PASSWORD = "mypass";

    private final TimeProvider timeProvider;

    private final Executor backgroundExecutor;
    private final Executor callbackExecutor;

    public SessionApiClient(TimeProvider timeProvider, Executor backgroundExecutor, Executor callbackExecutor) {
        this.timeProvider = timeProvider;
        this.backgroundExecutor = backgroundExecutor;
        this.callbackExecutor = callbackExecutor;
    }


    void login(final String email, final String password, final LoginCallback completionLoginCallback) {
        backgroundExecutor.post(new Runnable() {
            @Override
            public void run() {
                backgroundExecutor.sleep(1000);

                if (email == null || password == null) {
                    postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            completionLoginCallback.onError();
                        }
                    });
                    return;
                }

                boolean validLoginData = VALID_USERNAME.equalsIgnoreCase(email) && VALID_PASSWORD.equals(password);

                if (validLoginData) {
                    postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            completionLoginCallback.onSuccess();
                        }
                    });
                } else {
                    postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            completionLoginCallback.onError();
                        }
                    });
                }
            }
        });
    }

    private void postToMainThread(final Runnable runnable) {
        callbackExecutor.post(runnable);
    }

    void logout(final LogoutCallback callback) {
        backgroundExecutor.post(new Runnable() {
            @Override
            public void run() {
                backgroundExecutor.sleep(1000);

                // El tiempo sera valido si es par el segundo en el que nos deslogueamos
                boolean validTime = timeProvider.getCurrentTimeSeconds() % 2 == 0;

                if (validTime) {
                    postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess();
                        }
                    });
                } else {
                    postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError();
                        }
                    });
                }

            }
        });
    }

    public interface LoginCallback {
        void onSuccess();

        void onError();
    }

    public interface LogoutCallback {
        void onSuccess();

        void onError();
    }

    // Creamos esta interfaz para abtraernos del origen de datos, ya que para los tests que estamos realizando NO
    // queremos depender del SDK de Android y la clase MainThreadExecutor contiene las clases Looper y Handler que
    // son de Android
    public interface Executor {
        void post(Runnable run);
        void sleep(long millis);
    }
}
