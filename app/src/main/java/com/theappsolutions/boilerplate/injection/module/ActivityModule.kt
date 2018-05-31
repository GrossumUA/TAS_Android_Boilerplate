package com.theappsolutions.boilerplate.injection.module

import android.app.Activity
import android.content.Context

import com.theappsolutions.boilerplate.injection.ActivityContext

import dagger.Module
import dagger.Provides

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@Module
class ActivityModule(private val mActivity: Activity) {

    @Provides
    fun provideActivity(): Activity {
        return mActivity
    }

    @Provides
    @ActivityContext
    fun providesContext(): Context {
        return mActivity
    }
}
