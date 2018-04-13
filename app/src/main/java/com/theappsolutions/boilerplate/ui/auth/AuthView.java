package com.theappsolutions.boilerplate.ui.auth;


import com.theappsolutions.boilerplate.ui.base.BaseView;

public interface AuthView extends BaseView {

    void navigateToMenuScreen();

    void navigateToListScreen();

    void showLoginError(String text);

    void showPasswordError(String text);

}