package com.theappsolutions.boilerplate.ui.auth;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.injection.ConfigPersistent;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsEvent;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager;
import com.theappsolutions.boilerplate.ui.base.BasePresenter;
import com.theappsolutions.boilerplate.util.data.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@ConfigPersistent
public class AuthPresenter extends BasePresenter<AuthView> {

    private final DataManager dataManager;
    private final ValidationUtils validationUtils;
    private final AnalyticsManager analyticsManager;

    @Inject
    public AuthPresenter(DataManager dataManager,
                         ValidationUtils validationUtils,
                         AnalyticsManager analyticsManager) {
        this.dataManager = dataManager;
        this.validationUtils = validationUtils;
        this.analyticsManager = analyticsManager;
    }

    @SuppressLint("CheckResult")
    public void tryLogIn(@NonNull String login, @NonNull String password) {
        analyticsManager.logEvent(AnalyticsEvent.ATTEMPT_TO_LOGIN);

        List<ValidationUtils.ValidationResult> validationResults = new ArrayList<>();

        validationUtils.validatePassword(password)
                .doIfFailureWithMessage(msg -> getMvpView().ifPresent(authView -> authView.showPasswordError(msg)))
                .storeResults(validationResults);
        validationUtils.validateEmail(login)
                .doIfFailureWithMessage(msg -> getMvpView().ifPresent(authView -> authView.showLoginError(msg)))
                .storeResults(validationResults);

        if (ValidationUtils.ValidationResult.isAllSuccess(validationResults)) {
            Disposable disposable = dataManager.tryLogin(login, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                                analyticsManager.logEvent(AnalyticsEvent.SUCCESS_LOGIN);
                                navigateToNext();
                            },
                            getErrorConsumer());
            addToDisposables(disposable);
        }
    }

    private void navigateToNext() {
        getMvpView().ifPresent(authView -> {
            authView.navigateToListScreen();
            authView.close();
        });
    }

}
