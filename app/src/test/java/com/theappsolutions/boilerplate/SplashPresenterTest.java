package com.theappsolutions.boilerplate;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.data.PreferencesManager;
import com.theappsolutions.boilerplate.ui.splash.SplashPresenter;
import com.theappsolutions.boilerplate.ui.splash.SplashView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link SplashPresenter}
 */
@RunWith(MockitoJUnitRunner.class)
public class SplashPresenterTest extends CommonPresenterTest<SplashView, SplashPresenter> {

    @Mock
    SplashView mockView;
    @Mock
    DataManager dataManager;
    @Mock
    PreferencesManager preferencesManager;

    @InjectMocks
    private SplashPresenter presenter;
    private Optional<String> mockToken;

    @Before
    public void setUp() {
        super.setUp();
        mockToken = Optional.of("Token");
    }

    @NonNull
    @Override
    protected SplashPresenter getPresenter() {
        return presenter;
    }

    @NonNull
    @Override
    protected SplashView getView() {
        return mockView;
    }

    @Test
    public void positiveGetAutologinTest() {
        when(preferencesManager.getAutologinEnabled()).thenReturn(true);
        when(preferencesManager.getToken()).thenReturn(mockToken);
        presenter.init();

        //verify repository interactions
        verify(preferencesManager, never()).clear();
        verify(dataManager, never()).clearData();

        //verify view interactions
        verify(mockView, once()).goToProjectList();
        verify(mockView, once()).close();
    }

    @Test
    public void negativeGetAutologinTest() {
        when(preferencesManager.getAutologinEnabled()).thenReturn(true);
        when(preferencesManager.getToken()).thenReturn(Optional.empty());
        presenter.init();

        //verify repository interactions
        verify(preferencesManager, never()).clear();
        verify(dataManager, never()).clearData();

        //verify view interactions
        verify(mockView, once()).goToLogin();
        verify(mockView, once()).close();
    }

    @Test
    public void withoutAutologinTest() {
        when(preferencesManager.getAutologinEnabled()).thenReturn(false);
        when(preferencesManager.getToken()).thenReturn(Optional.empty());
        presenter.init();

        //verify repository interactions
        verify(preferencesManager, once()).clear();
        verify(dataManager, once()).clearData();

        //verify view interactions
        verify(mockView, once()).goToLogin();
        verify(mockView, once()).close();
    }
}
