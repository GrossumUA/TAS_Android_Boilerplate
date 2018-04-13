package com.theappsolutions.boilerplate.ui.base;

/**
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 */
public interface BaseView {

    void showToastMessage(String text);

    void showToastMessage(int textRes);

    void goBack();

    void close();

    void showProgress();

    void dismissProgress();
}
