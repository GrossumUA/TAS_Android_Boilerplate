package com.theappsolutions.boilerplate.ui.settings

import com.theappsolutions.boilerplate.ui.base.BaseView

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
interface SettingsView : BaseView {

    fun startAuthClearTop()

    fun fillView(autologinEnabled: Boolean)
}
