package com.karumi.loginkata;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class SessionApiClientTest {

    SessionApiClient sessionApiClient;
    @Mock
    TimeProvider mockTimeProvider;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        // NOTA: El FakeExecutor() forzamos que sea SINCRONO para los tests
        sessionApiClient = new SessionApiClient(mockTimeProvider, new FakeExecutor(), new FakeExecutor());
    }

    @Test
    public void login_fails_if_the_user_is_null() throws Exception {
        SessionApiClient.LoginCallback mockCompletionLoginCallback = mock(SessionApiClient.LoginCallback.class);

        sessionApiClient.login(null, "mypass", mockCompletionLoginCallback);

        // Si el onError tuviese parametros (Por ejemplo, onError("error muy malo")) en el Test pondriamos
        // onError(any())
        verify(mockCompletionLoginCallback).onError();
        // Esto es la DOBLE verificacion
        verify(mockCompletionLoginCallback, never()).onSuccess();
    }
    @Test
    public void login_fails_if_the_user_is_empty() throws Exception {
        SessionApiClient.LoginCallback mockCompletionLoginCallback = mock(SessionApiClient.LoginCallback.class);

        sessionApiClient.login("", "mypass", mockCompletionLoginCallback);

        verify(mockCompletionLoginCallback).onError();
        verify(mockCompletionLoginCallback, never()).onSuccess();
    }
    @Test
    public void login_fails_if_the_password_is_empty() throws Exception {
        SessionApiClient.LoginCallback mockCompletionLoginCallback = mock(SessionApiClient.LoginCallback.class);

        sessionApiClient.login("jaimegmail.com", "", mockCompletionLoginCallback);

        verify(mockCompletionLoginCallback).onError();
        verify(mockCompletionLoginCallback, never()).onSuccess();
    }
    @Test
    public void login_fails_if_the_password_is_null() throws Exception {
        SessionApiClient.LoginCallback mockCompletionLoginCallback = mock(SessionApiClient.LoginCallback.class);

        sessionApiClient.login("jaimegmail.com", null, mockCompletionLoginCallback);

        verify(mockCompletionLoginCallback).onError();
        verify(mockCompletionLoginCallback,never()).onSuccess();
    }
    @Test
    public void login_fails_with_invalid_email_and_password() throws Exception {
        SessionApiClient.LoginCallback mockCompletionLoginCallback = mock(SessionApiClient.LoginCallback.class);

        sessionApiClient.login("invalidemail.com", "invalidpass", mockCompletionLoginCallback);

        verify(mockCompletionLoginCallback).onError();
        verify(mockCompletionLoginCallback, never()).onSuccess();
    }

    @Test
    public void login_success_with_correct_email_and_password() throws Exception {
        SessionApiClient.LoginCallback mockCompletionLoginCallback = mock(SessionApiClient.LoginCallback.class);

        sessionApiClient.login("jaimegmail.com", "mypass", mockCompletionLoginCallback);

        verify(mockCompletionLoginCallback, never()).onError();
        verify(mockCompletionLoginCallback).onSuccess();
    }

    @Test
    public void logout_fails_with_invalid_time() throws Exception {
        SessionApiClient.LogoutCallback mockCompletionLoginCallback = mock(SessionApiClient.LogoutCallback.class);
        // Con esto mockeamos la respuesta que queremos obtener. En este caso, como en los segundos impares el logout
        // es incorrecto forzamos a que sea 1
        when(mockTimeProvider.getCurrentTimeSeconds()).thenReturn(1l);

        sessionApiClient.logout(mockCompletionLoginCallback);

        verify(mockCompletionLoginCallback).onError();
        verify(mockCompletionLoginCallback, never()).onSuccess();
    }
    @Test
    public void logout_success_with_valid_time() throws Exception {
        SessionApiClient.LogoutCallback mockCompletionLoginCallback = mock(SessionApiClient.LogoutCallback.class);
        when(mockTimeProvider.getCurrentTimeSeconds()).thenReturn(2l);

        sessionApiClient.logout(mockCompletionLoginCallback);

        verify(mockCompletionLoginCallback).onSuccess();
        verifyNoMoreInteractions(mockCompletionLoginCallback);
    }
}
