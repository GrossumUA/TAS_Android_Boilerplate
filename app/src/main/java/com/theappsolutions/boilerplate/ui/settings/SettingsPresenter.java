package com.theappsolutions.boilerplate.ui.settings;

import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.data.PreferencesManager;
import com.theappsolutions.boilerplate.injection.ConfigPersistent;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsEvent;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager;
import com.theappsolutions.boilerplate.ui.base.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private final DataManager dataManager;
    private PreferencesManager preferencesManager;
    private AnalyticsManager analyticsManager;

    @Inject
    public SettingsPresenter(PreferencesManager preferencesManager,
                             DataManager dataManager,
                             AnalyticsManager analyticsManager) {
        this.preferencesManager = preferencesManager;
        this.dataManager = dataManager;
        this.analyticsManager = analyticsManager;
    }

    public void onLogoutSelected() {
        analyticsManager.logEvent(AnalyticsEvent.LOG_OUT);
        preferencesManager.setToken(null);
        dataManager.clearData();
        getMvpView().ifPresent(SettingsView::startAuthClearTop);
    }

    public void onAutoLoginSwitched(boolean isEnabled) {
        preferencesManager.setAutologinEnabled(isEnabled);
    }

    public void init() {
        analyticsManager.logEvent(AnalyticsEvent.ENTER_SETTINGS);
        getMvpView().ifPresent(settingsView -> settingsView.fillView(preferencesManager.getAutologinEnabled()));
    }
}