package com.theappsolutions.boilerplate;

import android.support.annotation.NonNull;

import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.data.PreferencesManager;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsEvent;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager;
import com.theappsolutions.boilerplate.ui.auth.AuthPresenter;
import com.theappsolutions.boilerplate.ui.settings.SettingsPresenter;
import com.theappsolutions.boilerplate.ui.settings.SettingsView;
import com.theappsolutions.boilerplate.util.RxSchedulersOverrideRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link AuthPresenter}
 */
@RunWith(MockitoJUnitRunner.class)
public class SettingsPresenterTest extends CommonPresenterTest<SettingsView, SettingsPresenter> {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new
            RxSchedulersOverrideRule();

    @Mock
    SettingsView mockView;
    @Mock
    DataManager dataManager;
    @Mock
    AnalyticsManager analyticsManagerMock;
    @Mock
    PreferencesManager preferencesManagerMock;

    @InjectMocks
    private SettingsPresenter presenter;

    @NonNull
    @Override
    protected SettingsPresenter getPresenter() {
        return presenter;
    }

    @NonNull
    @Override
    protected SettingsView getView() {
        return mockView;
    }

    @Test
    public void initTest() {
        boolean isAutoLoginEnabled = true;

        when(preferencesManagerMock.getAutologinEnabled()).thenReturn(isAutoLoginEnabled);
        presenter.init();

        verify(analyticsManagerMock, once()).logEvent(AnalyticsEvent.ENTER_SETTINGS);
        verify(mockView, once()).fillView(isAutoLoginEnabled);
    }

    @Test
    public void onAutoLoginSwitchedTest() {
        boolean isAutoLoginEnabled = false;
        presenter.onAutoLoginSwitched(isAutoLoginEnabled);

        verify(preferencesManagerMock, once()).setAutologinEnabled(isAutoLoginEnabled);
    }

    @Test
    public void onLogoutSelectedTest() {
        presenter.onLogoutSelected();

        verify(analyticsManagerMock, once()).logEvent(AnalyticsEvent.LOG_OUT);

        verify(dataManager, once()).clearData();
        verify(preferencesManagerMock, once()).setToken(null);
        verify(mockView, once()).startAuthClearTop();
    }
}
