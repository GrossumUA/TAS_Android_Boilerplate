package com.theappsolutions.boilerplate.ui.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate._additional_useful_classes.ui.menu.BottomMenuActivity;
import com.theappsolutions.boilerplate.custom_views.CustomToolbar;
import com.theappsolutions.boilerplate.other.change_log.ChangeLogManager;
import com.theappsolutions.boilerplate.ui.base.BasePresenter;
import com.theappsolutions.boilerplate.ui.projects.ProjectListActivity;
import com.theappsolutions.boilerplate.util.other_utils.permissions_check.PermissionsCheckActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public class AuthActivity extends PermissionsCheckActivity implements AuthView {

    @BindView(R.id.et_login)
    EditText edtLogin;

    @BindView(R.id.et_password)
    EditText edtPassword;

    @BindView(R.id.login_input_lay)
    TextInputLayout textInputLayLogin;

    @BindView(R.id.pass_input_lay)
    TextInputLayout textInputLayPassword;

    @Inject
    AuthPresenter authPresenter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AuthActivity.class);
    }

    public static void start(@NonNull Activity activity) {
        Intent intent = getStartIntent(activity);
        activity.startActivity(intent);
    }


    public static void startClearTop(@NonNull Activity activity) {
        Intent intent = new Intent(activity, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void goAheadAfterPermissionsEnabling() {
        ChangeLogManager manager = new ChangeLogManager(this);
        manager.analyze();

        authPresenter.attachView(this);
    }

    @OnEditorAction(R.id.et_password)
    public boolean onEtPassAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onLogInButtonClick();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        processPermissionAfterStart();
    }

    @OnClick(R.id.btn_login)
    public void onLogInButtonClick() {
        String login = edtLogin.getText().toString();
        String password = edtPassword.getText().toString();
        authPresenter.tryLogIn(login, password);
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_auth;
    }

    @Override
    protected CustomToolbar.Config getToolbarConfig() {
        return CustomToolbar.Config.withoutToolbar();
    }

    @Override
    public void showLoginError(String text) {
        textInputLayLogin.setError(text);
    }

    @Override
    public void showPasswordError(String text) {
        textInputLayPassword.setError(text);
    }

    @OnTextChanged(R.id.et_login)
    void onLoginTextChanged(CharSequence text) {
        textInputLayLogin.setError(null);
    }

    @OnTextChanged(R.id.et_password)
    void onPassTextChanged(CharSequence text) {
        textInputLayPassword.setError(null);
    }

    @Override
    public void navigateToMenuScreen() {
        BottomMenuActivity.start(this);
    }

    @Override
    public void navigateToListScreen() {
        ProjectListActivity.startWithAnimation(this);
    }

    @Override
    public BasePresenter getBasePresenter() {
        return authPresenter;
    }

}
