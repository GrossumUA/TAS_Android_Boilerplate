package com.theappsolutions.boilerplate.ui.splash

import com.annimon.stream.function.Consumer
import com.theappsolutions.boilerplate.data.DataManager
import com.theappsolutions.boilerplate.data.PreferencesManager
import com.theappsolutions.boilerplate.injection.ConfigPersistent
import com.theappsolutions.boilerplate.ui.base.BasePresenter

import javax.inject.Inject

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@ConfigPersistent
class SplashPresenter @Inject
constructor(private val preferencesManager: PreferencesManager, private val dataManager: DataManager) : BasePresenter<SplashView>() {

    fun init() {
        if (!preferencesManager.autologinEnabled) {
            preferencesManager.clear()
            dataManager.clearData()
        }
        getMvpView().ifPresent { splashView ->
            preferencesManager.getToken().ifPresentOrElse({ splashView.goToProjectList() }) { splashView.goToLogin() }
            splashView.close()
        }
    }
}
