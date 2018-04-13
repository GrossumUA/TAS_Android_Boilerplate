package com.theappsolutions.boilerplate.ui.settings;


import com.theappsolutions.boilerplate.ui.base.BaseView;

public interface SettingsView extends BaseView {

    void startAuthClearTop();

    void fillView(boolean autologinEnabled);
}
