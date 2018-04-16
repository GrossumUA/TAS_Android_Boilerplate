package com.theappsolutions.boilerplate.ui.base;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public interface BaseView {

    void showToastMessage(String text);

    void showToastMessage(int textRes);

    void goBack();

    void close();

    void showProgress();

    void dismissProgress();
}
