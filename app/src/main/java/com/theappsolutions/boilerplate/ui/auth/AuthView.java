package com.theappsolutions.boilerplate.ui.auth;

import com.theappsolutions.boilerplate.ui.base.BaseView;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public interface AuthView extends BaseView {

    void navigateToMenuScreen();

    void navigateToListScreen();

    void showLoginError(String text);

    void showPasswordError(String text);

}