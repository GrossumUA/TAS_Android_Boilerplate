package com.theappsolutions.boilerplate.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View

import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.customviews.CustomToolbar
import com.theappsolutions.boilerplate.ui.auth.AuthActivity
import com.theappsolutions.boilerplate.ui.base.BaseActivity
import com.theappsolutions.boilerplate.ui.base.BaseMvpActivity
import com.theappsolutions.boilerplate.ui.base.BasePresenter
import com.theappsolutions.boilerplate.util.ui.FragmentUtils

import javax.inject.Inject

import butterknife.BindView
import com.theappsolutions.boilerplate.TasBoilerplateApplication

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
class SettingsActivity : BaseMvpActivity(), SettingsView, SettingsFragment.SettingsCallback {

    @Inject
    lateinit var settingsPresenter: SettingsPresenter
    lateinit var fragment: SettingsFragment

    override fun getContentViewRes(): Int = R.layout.activity_settings

    override fun getToolbarConfig(): CustomToolbar.Config? = CustomToolbar.Config.defaultConfig().hasBackBtn(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this);
        setToolbarTitle(getString(R.string.settings_screen__toolbar_title))
        settingsPresenter.attachView(this)
        settingsPresenter.init()
    }

    override fun getBasePresenter(): BasePresenter<*> {
        return settingsPresenter
    }

    override fun onLogoutSelected() {
        settingsPresenter.onLogoutSelected()
    }

    override fun onAutoLoginSwitched(isEnabled: Boolean) {
        settingsPresenter.onAutoLoginSwitched(isEnabled)
    }

    override fun startAuthClearTop() {
        AuthActivity.startClearTop(this)
    }

    override fun fillView(autologinEnabled: Boolean) {
        FragmentUtils.addFragment(R.id.cnt_main, SettingsFragment.newInstance(autologinEnabled), this)
    }

    companion object {

        fun getIntent(activity: BaseActivity): Intent {
            return Intent(activity, SettingsActivity::class.java)
        }

        fun startWithAnimation(activity: BaseActivity) {
            activity.startActivity(getIntent(activity))
            activity.animateActivityChangingToRight()
        }
    }
}
