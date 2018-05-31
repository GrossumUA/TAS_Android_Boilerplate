package com.theappsolutions.boilerplate.ui.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment

import com.annimon.stream.Optional
import com.annimon.stream.function.Consumer
import com.theappsolutions.boilerplate.injection.component.FragmentComponent
import com.theappsolutions.boilerplate.injection.module.FragmentModule

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 *
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
abstract class BaseFragment : Fragment(), BaseView {

    var baseActivity = Optional.empty<BaseActivity>()
    var fragmentComponent = Optional.empty<FragmentComponent>()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseActivity) {
            baseActivity = Optional.of((context as BaseActivity?))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity.ifPresent { activity -> fragmentComponent = Optional.of(activity.activityComponent()?.fragmentComponent(FragmentModule(this))) }
    }

    override fun showToastMessage(text: String) {
        baseActivity.ifPresent { activity -> activity.showToastMessage(text) }
    }

    override fun showToastMessage(textRes: Int) {
        baseActivity.ifPresent { activity -> activity.showToastMessage(textRes) }
    }

    override fun goBack() {
        baseActivity.ifPresent({ it.goBack() })
    }

    override fun close() {
        baseActivity.ifPresent({ it.close() })
    }

    override fun showProgress() {
        baseActivity.ifPresent({ it.showProgress() })
    }

    override fun dismissProgress() {
        baseActivity.ifPresent({ it.dismissProgress() })
    }

    fun setToolbarTitle(title: String) {
        baseActivity.ifPresent { activity -> activity.setToolbarTitle(title) }
    }
}
