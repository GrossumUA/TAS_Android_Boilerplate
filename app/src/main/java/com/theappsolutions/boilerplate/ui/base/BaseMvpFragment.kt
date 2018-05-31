package com.theappsolutions.boilerplate.ui.base

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
abstract class BaseMvpFragment : BaseFragment(), MvpScreen {

    override fun onDetach() {
        super.onDetach()
        getBasePresenter().detachView()
    }
}
