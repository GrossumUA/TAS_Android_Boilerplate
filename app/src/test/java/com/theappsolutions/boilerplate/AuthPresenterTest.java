package com.theappsolutions.boilerplate;

import android.support.annotation.NonNull;

import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.data.model.api.LoginResponse;
import com.theappsolutions.boilerplate.mock.LoginResponseMock;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsEvent;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager;
import com.theappsolutions.boilerplate.ui.auth.AuthPresenter;
import com.theappsolutions.boilerplate.ui.auth.AuthView;
import com.theappsolutions.boilerplate.util.RxSchedulersOverrideRule;
import com.theappsolutions.boilerplate.util.data_utils.ValidationUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link AuthPresenter}
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthPresenterTest extends CommonPresenterTest<AuthView, AuthPresenter> {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new
            RxSchedulersOverrideRule();

    @Mock
    AuthView mockView;
    @Mock
    DataManager dataManager;
    @Mock
    AnalyticsManager analyticsManagerMock;
    @Mock
    ValidationUtils validationUtils;

    @InjectMocks
    private AuthPresenter presenter;
    private String passwordTestData;
    private String loginTestData;
    private Observable<LoginResponse> loginResponseMock;

    @Before
    public void setUp() {
        super.setUp();
        loginTestData = "test@test.com";
        passwordTestData = "1Qqqqq";

        loginResponseMock = Observable.just(LoginResponseMock.generate());
    }

    @NonNull
    @Override
    protected AuthPresenter getPresenter() {
        return presenter;
    }

    @NonNull
    @Override
    protected AuthView getView() {
        return mockView;
    }

    @Test
    public void tryLoginPositiveTest() {
        when(dataManager.tryLogin(loginTestData, passwordTestData)).thenReturn(loginResponseMock);
        when(validationUtils.validateEmail(loginTestData)).thenReturn(ValidationUtils.ValidationResult.valid());
        when(validationUtils.validatePassword(passwordTestData)).thenReturn(ValidationUtils.ValidationResult.valid());

        presenter.tryLogIn(loginTestData, passwordTestData);

        verify(analyticsManagerMock, once()).logEvent(AnalyticsEvent.ATTEMPT_TO_LOGIN);
        verify(analyticsManagerMock, once()).logEvent(AnalyticsEvent.SUCCESS_LOGIN);

        verify(mockView, never()).showLoginError(Mockito.anyString());
        verify(mockView, never()).showPasswordError(Mockito.anyString());

        verify(validationUtils, once()).validateEmail(Mockito.anyString());
        verify(validationUtils, once()).validatePassword(Mockito.anyString());
        verify(dataManager, once()).tryLogin(Mockito.anyString(), Mockito.anyString());
        verify(mockView, once()).navigateToListScreen();
        verify(mockView, once()).close();
    }

    @Test
    public void tryLoginNegativeEmailTest() {
        when(validationUtils.validateEmail(loginTestData)).thenReturn(ValidationUtils.ValidationResult.notValid(""));
        when(validationUtils.validatePassword(passwordTestData)).thenReturn(ValidationUtils.ValidationResult.valid());

        presenter.tryLogIn(loginTestData, passwordTestData);

        verify(analyticsManagerMock, once()).logEvent(AnalyticsEvent.ATTEMPT_TO_LOGIN);
        verify(analyticsManagerMock, never()).logEvent(AnalyticsEvent.SUCCESS_LOGIN);

        verify(mockView, once()).showLoginError(Mockito.anyString());
        verify(mockView, never()).showPasswordError(Mockito.anyString());

        verify(validationUtils, once()).validateEmail(Mockito.anyString());
        verify(validationUtils, once()).validatePassword(Mockito.anyString());
        verify(dataManager, never()).tryLogin(Mockito.anyString(), Mockito.anyString());
        verify(mockView, never()).navigateToListScreen();
        verify(mockView, never()).close();
    }

    @Test
    public void tryLoginNegativePasswordTest() {
        when(validationUtils.validateEmail(loginTestData)).thenReturn(ValidationUtils.ValidationResult.valid());
        when(validationUtils.validatePassword(passwordTestData)).thenReturn(ValidationUtils.ValidationResult.notValid(""));

        presenter.tryLogIn(loginTestData, passwordTestData);

        verify(analyticsManagerMock, once()).logEvent(AnalyticsEvent.ATTEMPT_TO_LOGIN);
        verify(analyticsManagerMock, never()).logEvent(AnalyticsEvent.SUCCESS_LOGIN);

        verify(mockView, never()).showLoginError(Mockito.anyString());
        verify(mockView, once()).showPasswordError(Mockito.anyString());

        verify(validationUtils, once()).validateEmail(Mockito.anyString());
        verify(validationUtils, once()).validatePassword(Mockito.anyString());
        verify(dataManager, never()).tryLogin(Mockito.anyString(), Mockito.anyString());
        verify(mockView, never()).navigateToListScreen();
        verify(mockView, never()).close();
    }
}
