package com.karumi.loginkata;

import com.karumi.loginkata.SessionApiClient.LoginCallback;
import com.karumi.loginkata.SessionApiClient.LogoutCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

// El Presenter al ser codigo puramente Java se puede meter en los test unitarios
@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    private LogInPresenter loginPresenter;
    @Mock
    private LogInView mockView;
    @Mock
    private SessionApiClient mockSessionApiClient;

    @Before
    public void setUp() throws Exception {
        loginPresenter = new LogInPresenter(mockSessionApiClient, new FakeSessionStorage());
        loginPresenter.injectView(mockView);
    }

    @Test
    public void initialize_should_enable_buttons() throws Exception {
        loginPresenter.initialize();

        verify(mockView).enableButtons();

    }

    @Test
    public void initialize_should_display_login_form() throws Exception {
        loginPresenter.initialize();

        verify(mockView).showLogInForm();

    }

    @Test
    public void initialize_should_hide_loading() throws Exception {
        loginPresenter.initialize();

        verify(mockView).hideLoading();

    }

    @Test
    public void login_shows_error_if_the_api_client_returns_error_on_login_click() throws Exception {
        givenAnInvalidLogin("", "");

        loginPresenter.onLoginButtonClicked("", "");

        verify(mockView).showError("Login failed!! Check your data");
    }


    @Test
    public void login_shows_logout_form_on_login_success() throws Exception {
        givenAnValidLogin("hellogmail.com", "world");

        loginPresenter.onLoginButtonClicked("hellogmail.com", "world");

        verify(mockView).hideLogInForm();
        verify(mockView).showLogOutButton();

    }

    @Test
    public void logout_disable_loading_if_api_client_return_error() throws Exception {
        givenAnInvalidLogout();

        loginPresenter.onLogoutButtonClicked();

        verify(mockView).hideLoading();

    }

    @Test
    public void logout_enable_buttons_if_api_client_return_error() throws Exception {
        givenAnInvalidLogout();

        loginPresenter.onLogoutButtonClicked();

        verify(mockView).enableButtons();

    }
    @Test
    public void logout_should_show_login_form_if_api_client_return_success() throws Exception {
        givenAnValidLogout();

        loginPresenter.onLogoutButtonClicked();

        verify(mockView).showLogInForm();

    }
    @Test
    public void logout_should_reset_inputs_if_api_client_return_success() throws Exception {
        givenAnValidLogout();

        loginPresenter.onLogoutButtonClicked();

        verify(mockView).emptyInputUsername();
        verify(mockView).emptyInputPassword();

    }

    // Los doAnswer(new Answer()...) son parecidos los thenReturn(...) pero para los Callbacks
    private void givenAnInvalidLogout() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LogoutCallback loginCallback = (LogoutCallback) invocation.getArguments()[0];
                loginCallback.onError();
                return null;
            }
        }).when(mockSessionApiClient).logout(any(LogoutCallback.class));
    }

    private void givenAnValidLogout() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LogoutCallback loginCallback = (LogoutCallback) invocation.getArguments()[0];
                loginCallback.onSuccess();
                return null;
            }
        }).when(mockSessionApiClient).logout(any(LogoutCallback.class));
    }

    private void givenAnInvalidLogin(String email, String password) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginCallback loginCallback = (LoginCallback) invocation.getArguments()[2];
                loginCallback.onError();
                return null;
            }
        }).when(mockSessionApiClient).login(eq(email), eq(password), any(LoginCallback.class));
    }
    private void givenAnValidLogin(String email, String password) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginCallback loginCallback = (LoginCallback) invocation.getArguments()[2];
                loginCallback.onSuccess();
                return null;
            }
        }).when(mockSessionApiClient).login(eq(email), eq(password), any(LoginCallback.class));
    }
}
