package com.theappsolutions.boilerplate.ui.settings;

import com.theappsolutions.boilerplate.ui.base.BaseView;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public interface SettingsView extends BaseView {

    void startAuthClearTop();

    void fillView(boolean autologinEnabled);
}
