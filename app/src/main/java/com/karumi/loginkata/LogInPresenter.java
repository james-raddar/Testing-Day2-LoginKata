package com.karumi.loginkata;

public class LogInPresenter {

    private final SessionApiClient sessionApiClient;
    private final SessionStorage sessionStorage;

    LogInView view;
    private boolean isLoggedIn;

    public LogInPresenter(SessionApiClient sessionApiClient, SessionStorage sessionStorage) {
        this.sessionApiClient = sessionApiClient;
        this.sessionStorage = sessionStorage;
    }

    public void initialize() {
        isLoggedIn = sessionStorage.getUser()!=null;
        updateButtons();
        setLoading(false);
    }

    public void onLoginButtonClicked(final String email, String password) {
        setLoading(true);
        sessionApiClient.login(email, password, new SessionApiClient.LoginCallback() {
            @Override
            public void onSuccess() {
                sessionStorage.saveUser(email);
                updateLoginStatus(true);
                setLoading(false);
            }

            @Override
            public void onError() {
                updateLoginStatus(false);
                view.showError("Login failed!! Check your data");
                setLoading(false);

            }
        });

    }

    public void onLogoutButtonClicked() {
        setLoading(true);

        sessionApiClient.logout(new SessionApiClient.LogoutCallback() {
            @Override
            public void onSuccess() {
                sessionStorage.saveUser(null);
                view.emptyInputUsername();
                view.emptyInputPassword();
                updateLoginStatus(false);
                setLoading(false);
            }

            @Override
            public void onError() {
                updateLoginStatus(true);
                setLoading(false);
            }
        });

    }

    public void injectView(LogInView view) {
        this.view = view;
    }

    private void setLoading(boolean loading) {
        if (loading) {
            view.showLoading();
            view.disableButtons();
        } else {
            view.hideLoading();
            view.enableButtons();

        }
    }

    private void updateLoginStatus(boolean isLogged) {
        isLoggedIn = isLogged;
        updateButtons();
    }

    private void updateButtons() {
        if (isLoggedIn) {
            view.hideLogInForm();
            view.showLogOutButton();
        } else {
            view.showLogInForm();
            view.hideLogOutButton();
        }
    }
}
