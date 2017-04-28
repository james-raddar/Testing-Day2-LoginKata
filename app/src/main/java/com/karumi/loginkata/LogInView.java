package com.karumi.loginkata;

public interface LogInView {

    void hideLogInForm();
    void showLogInForm();

    void hideLogOutButton();
    void showLogOutButton();


    void showError(String message);

    void showLoading();
    void hideLoading();

    void enableButtons();
    void disableButtons();

    void emptyInputUsername();
    void emptyInputPassword();
}
