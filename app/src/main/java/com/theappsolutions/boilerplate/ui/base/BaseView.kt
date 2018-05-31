package com.theappsolutions.boilerplate.ui.base

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
interface BaseView {

    fun showToastMessage(text: String)

    fun showToastMessage(textRes: Int)

    fun goBack()

    fun close()

    fun showProgress()

    fun dismissProgress()
}
