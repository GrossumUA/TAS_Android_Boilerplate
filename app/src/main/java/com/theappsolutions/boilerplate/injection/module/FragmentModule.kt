package com.theappsolutions.boilerplate.injection.module

import android.content.Context
import android.content.res.Resources
import android.support.v4.app.Fragment

import com.theappsolutions.boilerplate.injection.ActivityContext

import dagger.Module
import dagger.Provides

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    fun provideFragment(): Fragment {
        return fragment
    }

    @Provides
    @ActivityContext
    fun providesContext(): Context? {
        return fragment.context
    }

    @Provides
    fun provideResources(): Resources? {
        return fragment.context?.resources
    }

}
