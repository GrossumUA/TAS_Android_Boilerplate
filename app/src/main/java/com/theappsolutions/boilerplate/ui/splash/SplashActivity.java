package com.theappsolutions.boilerplate.ui.splash;

import android.os.Bundle;

import com.theappsolutions.boilerplate.custom_views.CustomToolbar;
import com.theappsolutions.boilerplate.ui.auth.AuthActivity;
import com.theappsolutions.boilerplate.ui.base.BaseMvpActivity;
import com.theappsolutions.boilerplate.ui.base.BasePresenter;
import com.theappsolutions.boilerplate.ui.projects.ProjectListActivity;

import javax.inject.Inject;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public class SplashActivity extends BaseMvpActivity implements SplashView {

    @Inject
    SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        splashPresenter.attachView(this);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        splashPresenter.init();
    }

    @Override
    protected int getContentViewRes() {
        return 0;
    }

    @Override
    public BasePresenter getBasePresenter() {
        return splashPresenter;
    }

    @Override
    public void goToLogin() {
        AuthActivity.start(this);
    }

    @Override
    public void goToProjectList() {
        startActivity(ProjectListActivity.getIntent(this));
    }

    @Override
    protected CustomToolbar.Config getToolbarConfig() {
        return CustomToolbar.Config.withoutToolbar();
    }
}
