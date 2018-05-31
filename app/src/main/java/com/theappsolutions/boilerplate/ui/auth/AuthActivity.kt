package com.theappsolutions.boilerplate.ui.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import butterknife.OnTextChanged

import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.additionalusefulclasses.ui.menu.BottomMenuActivity
import com.theappsolutions.boilerplate.customviews.CustomToolbar
import com.theappsolutions.boilerplate.other.changelog.ChangeLogManager
import com.theappsolutions.boilerplate.ui.base.BasePresenter
import com.theappsolutions.boilerplate.ui.projects.ProjectListActivity
import com.theappsolutions.boilerplate.util.other.permissions_check.PermissionsCheckActivity

import javax.inject.Inject


import com.theappsolutions.boilerplate.TasBoilerplateApplication
import kotlinx.android.synthetic.main.activity_auth.*

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
class AuthActivity : PermissionsCheckActivity(), AuthView {

    @Inject
    lateinit var authPresenter: AuthPresenter

    override fun goAheadAfterPermissionsEnabling() {
        val manager = ChangeLogManager(this)
        manager.analyze()

        authPresenter.attachView(this)
        et_password.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onLogInButtonClick()
            }
            true
        }

        btn_login.setOnClickListener {
            onLogInButtonClick()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this);
        processPermissionAfterStart()
    }

    fun onLogInButtonClick() {
        val login = et_login?.text.toString()
        val password = et_password?.text.toString()
        authPresenter.tryLogIn(login, password)
    }

    override fun getContentViewRes(): Int {
        return R.layout.activity_auth
    }

    override fun getToolbarConfig(): CustomToolbar.Config? {
        return CustomToolbar.Config.withoutToolbar()
    }

    override fun showLoginError(text: String) {
        login_input_lay.error = text
    }

    override fun showPasswordError(text: String) {
        pass_input_lay.error = text
    }

    @OnTextChanged(R.id.et_login)
    internal fun onLoginTextChanged(text: CharSequence) {
        login_input_lay.error = null
    }

    @OnTextChanged(R.id.et_password)
    internal fun onPassTextChanged(text: CharSequence) {
        pass_input_lay.error = null
    }

    override fun navigateToMenuScreen() {
        BottomMenuActivity.start(this)
    }

    override fun navigateToListScreen() {
        ProjectListActivity.startWithAnimation(this)
    }

    override fun getBasePresenter(): BasePresenter<*> {
        return authPresenter
    }

    companion object {

        fun getStartIntent(context: Context): Intent {
            return Intent(context, AuthActivity::class.java)
        }

        fun start(activity: Activity) {
            val intent = getStartIntent(activity)
            activity.startActivity(intent)
        }

        fun startClearTop(activity: Activity) {
            val intent = Intent(activity, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
        }
    }

}
