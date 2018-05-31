package com.theappsolutions.boilerplate.ui.auth

import com.theappsolutions.boilerplate.ui.base.BaseView

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
interface AuthView : BaseView {

    fun navigateToMenuScreen()

    fun navigateToListScreen()

    fun showLoginError(text: String)

    fun showPasswordError(text: String)

}