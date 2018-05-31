package com.theappsolutions.boilerplate.ui.auth

import android.annotation.SuppressLint

import com.theappsolutions.boilerplate.data.DataManager
import com.theappsolutions.boilerplate.data.model.api.LoginResponse
import com.theappsolutions.boilerplate.injection.ConfigPersistent
import com.theappsolutions.boilerplate.other.analytics.AnalyticsEvent
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager
import com.theappsolutions.boilerplate.other.functions.Action1
import com.theappsolutions.boilerplate.ui.base.BasePresenter
import com.theappsolutions.boilerplate.util.data.ValidationUtils

import java.util.ArrayList

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@ConfigPersistent
class AuthPresenter @Inject
constructor(private val dataManager: DataManager,
            private val validationUtils: ValidationUtils,
            private val analyticsManager: AnalyticsManager) : BasePresenter<AuthView>() {

    @SuppressLint("CheckResult")
    fun tryLogIn(login: String, password: String) {
        analyticsManager.logEvent(AnalyticsEvent.ATTEMPT_TO_LOGIN)

        val validationResults = ArrayList<ValidationUtils.ValidationResult>()

        validationUtils.validatePassword(password)
                .doIfFailureWithMessage(object : Action1<String> {
                    override fun call(msg: String) {
                        this@AuthPresenter.getMvpView().ifPresent { authView -> authView.showPasswordError(msg) }
                    }
                })
                .storeResults(validationResults)
        validationUtils.validateEmail(login)
                .doIfFailureWithMessage(object : Action1<String> {
                    override fun call(msg: String) {
                        this@AuthPresenter.getMvpView().ifPresent { authView -> authView.showLoginError(msg) }
                    }
                })
                .storeResults(validationResults)

        if (ValidationUtils.ValidationResult.isAllSuccess(validationResults)) {
            val disposable = dataManager.tryLogin(login, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer { (token) ->
                        analyticsManager.logEvent(AnalyticsEvent.SUCCESS_LOGIN)
                        this@AuthPresenter.navigateToNext()
                    })
            addToDisposables(disposable)
        }
    }

    private fun navigateToNext() {
        getMvpView().ifPresent { authView ->
            authView.navigateToListScreen()
            authView.close()
        }
    }

}
