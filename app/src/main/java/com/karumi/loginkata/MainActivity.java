package com.karumi.loginkata;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LogInView {

    public static final String USER_LOGGED = "user_logged";
    private EditText inputUsername, inputPassword;
    private View buttonLogin, buttonLogout;
    private SessionApiClient sessionApiClient = new SessionApiClient(new TimeProvider(), new ThreadExecutor(), new MainThreadExecutor());
    boolean isLoggedIn = false;
    private SharedPreferences sharedPreferences;
    private View progressBar;
    private LogInPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        initViews();

        // El ThreadExecutor crea otro hilo para gestionar el logueo
        // El MainThreadExecutor se asegura de que la respuesta del hilo del ThreadExecutor va al hilo principal
        presenter = new LogInPresenter(new SessionApiClient(new TimeProvider(), new ThreadExecutor(),
                new MainThreadExecutor()), new SharedPreferencesSessionStorage(PreferenceManager.getDefaultSharedPreferences(this)));
        presenter.injectView(this);
        presenter.initialize();
    }

    private void initViews() {
        inputUsername = (EditText) findViewById(R.id.edit_username);
        inputPassword = (EditText) findViewById(R.id.edit_password);
        buttonLogin = findViewById(R.id.button_login);
        buttonLogout = findViewById(R.id.button_logout);
        progressBar = findViewById(R.id.progressBar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = getInputEmail();
                String password = getInputPassword();
                presenter.onLoginButtonClicked(email,password);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onLogoutButtonClicked();
            }
        });

    }


    @Override
    public void hideLogInForm() {
        inputUsername.setVisibility(View.GONE);
        inputPassword.setVisibility(View.GONE);
        buttonLogin.setVisibility(View.GONE);

    }

    @Override
    public void showLogInForm() {
        inputUsername.setVisibility(View.VISIBLE);
        inputPassword.setVisibility(View.VISIBLE);
        buttonLogin.setVisibility(View.VISIBLE);

    }


    @Override
    public void hideLogOutButton() {
        buttonLogout.setVisibility(View.GONE);
    }

    @Override
    public void showLogOutButton() {
        buttonLogout.setVisibility(View.VISIBLE);

    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void enableButtons() {
        buttonLogin.setClickable(true);
        buttonLogout.setClickable(true);
    }

    @Override
    public void disableButtons() {
        buttonLogin.setClickable(false);
        buttonLogout.setClickable(false);

    }

    public String getInputEmail() {
        return inputUsername.getText().toString();
    }

    public String getInputPassword() {
        return inputPassword.getText().toString();
    }

    @Override
    public void emptyInputUsername() {
        inputUsername.setText(null);
    }

    @Override
    public void emptyInputPassword() {
        inputPassword.setText(null);
    }
}
