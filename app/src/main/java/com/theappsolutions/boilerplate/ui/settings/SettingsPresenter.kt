package com.theappsolutions.boilerplate.ui.settings

import com.annimon.stream.function.Consumer
import com.theappsolutions.boilerplate.data.DataManager
import com.theappsolutions.boilerplate.data.PreferencesManager
import com.theappsolutions.boilerplate.injection.ConfigPersistent
import com.theappsolutions.boilerplate.other.analytics.AnalyticsEvent
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager
import com.theappsolutions.boilerplate.ui.base.BasePresenter

import javax.inject.Inject

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@ConfigPersistent
class SettingsPresenter @Inject
constructor(private val preferencesManager: PreferencesManager,
            private val dataManager: DataManager,
            private val analyticsManager: AnalyticsManager) : BasePresenter<SettingsView>() {

    fun onLogoutSelected() {
        analyticsManager.logEvent(AnalyticsEvent.LOG_OUT)
        preferencesManager.setToken("")
        dataManager.clearData()
        getMvpView().ifPresent(Consumer<SettingsView> { it.startAuthClearTop() })
    }

    fun onAutoLoginSwitched(isEnabled: Boolean) {
        preferencesManager.autologinEnabled = isEnabled
    }

    fun init() {
        analyticsManager.logEvent(AnalyticsEvent.ENTER_SETTINGS)
        getMvpView().ifPresent { settingsView -> settingsView.fillView(preferencesManager.autologinEnabled) }
    }
}