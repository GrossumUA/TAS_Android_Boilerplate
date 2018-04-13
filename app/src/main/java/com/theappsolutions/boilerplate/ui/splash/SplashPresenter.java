package com.theappsolutions.boilerplate.ui.splash;

import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.data.PreferencesManager;
import com.theappsolutions.boilerplate.injection.ConfigPersistent;
import com.theappsolutions.boilerplate.ui.base.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class SplashPresenter extends BasePresenter<SplashView> {

    private final PreferencesManager preferencesManager;
    private DataManager dataManager;

    @Inject
    public SplashPresenter(PreferencesManager preferencesManager, DataManager dataManager) {
        this.preferencesManager = preferencesManager;
        this.dataManager = dataManager;
    }

    public void init() {
        if (!preferencesManager.getAutologinEnabled()) {
            preferencesManager.clear();
            dataManager.clearData();
        }
        getMvpView().ifPresent(splashView -> {
            preferencesManager.getToken().ifPresentOrElse(token -> splashView.goToProjectList(), splashView::goToLogin);
            splashView.close();
        });
    }
}
