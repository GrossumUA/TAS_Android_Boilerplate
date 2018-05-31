package com.theappsolutions.boilerplate.ui.splash

import android.os.Bundle
import com.theappsolutions.boilerplate.TasBoilerplateApplication

import com.theappsolutions.boilerplate.TasBoilerplateSettings
import com.theappsolutions.boilerplate.customviews.CustomToolbar
import com.theappsolutions.boilerplate.ui.auth.AuthActivity
import com.theappsolutions.boilerplate.ui.base.BaseMvpActivity
import com.theappsolutions.boilerplate.ui.base.BasePresenter
import com.theappsolutions.boilerplate.ui.projects.ProjectListActivity
import com.theappsolutions.boilerplate.util.data.StringUtils

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import io.reactivex.Completable

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
class SplashActivity : BaseMvpActivity(), SplashView {

    @Inject
    lateinit var splashPresenter: SplashPresenter

    override fun getContentViewRes(): Int = 0

    override fun getToolbarConfig(): CustomToolbar.Config? = CustomToolbar.Config.withoutToolbar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this);
        splashPresenter.attachView(this)
        Completable.complete()
                .delay(TasBoilerplateSettings.SPLASH_SCREEN_DELAY_SEC.toLong(), TimeUnit.SECONDS)
                .doOnComplete { splashPresenter.init() }.subscribe()
    }

    override fun getBasePresenter(): BasePresenter<*> {
        return splashPresenter
    }

    override fun goToLogin() {
        AuthActivity.start(this)
    }

    override fun goToProjectList() {
        startActivity(ProjectListActivity.getIntent(this))
    }
}
