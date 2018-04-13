package com.theappsolutions.boilerplate.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate.custom_views.CustomToolbar;
import com.theappsolutions.boilerplate.ui.auth.AuthActivity;
import com.theappsolutions.boilerplate.ui.base.BaseActivity;
import com.theappsolutions.boilerplate.ui.base.BaseMvpActivity;
import com.theappsolutions.boilerplate.ui.base.BasePresenter;
import com.theappsolutions.boilerplate.util.ui_utils.FragmentUtils;

import javax.inject.Inject;

import butterknife.BindView;

public class SettingsActivity extends BaseMvpActivity implements SettingsView, SettingsFragment.SettingsCallback {

    @BindView(R.id.cnt_main)
    View cntMain;

    @Inject
    SettingsPresenter settingsPresenter;
    private SettingsFragment fragment;

    public static Intent getIntent(BaseActivity activity) {
        return new Intent(activity, SettingsActivity.class);
    }

    public static void startWithAnimation(BaseActivity activity) {
        activity.startActivity(getIntent(activity));
        activity.animateActivityChangingToRight();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setToolbarTitle(getString(R.string.settings_screen__toolbar_title));
        settingsPresenter.attachView(this);
        settingsPresenter.init();
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_settings;
    }

    @Override
    protected CustomToolbar.Config getToolbarConfig() {
        return CustomToolbar.Config.defaultConfig().hasBackBtn(true);
    }

    @Override
    public BasePresenter getBasePresenter() {
        return settingsPresenter;
    }

    @Override
    public void onLogoutSelected() {
        settingsPresenter.onLogoutSelected();
    }

    @Override
    public void onAutoLoginSwitched(boolean isEnabled) {
        settingsPresenter.onAutoLoginSwitched(isEnabled);
    }

    @Override
    public void startAuthClearTop() {
        AuthActivity.startClearTop(this);
    }

    @Override
    public void fillView(boolean autologinEnabled) {
        FragmentUtils.addFragment(R.id.cnt_main, SettingsFragment.newInstance(autologinEnabled), this);
    }
}
