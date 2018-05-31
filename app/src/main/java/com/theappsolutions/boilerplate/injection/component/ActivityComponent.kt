package com.theappsolutions.boilerplate.injection.component

import com.theappsolutions.boilerplate.injection.PerActivity
import com.theappsolutions.boilerplate.injection.module.ActivityModule
import com.theappsolutions.boilerplate.injection.module.FragmentModule
import com.theappsolutions.boilerplate.ui.auth.AuthActivity
import com.theappsolutions.boilerplate.ui.projects.ProjectListActivity
import com.theappsolutions.boilerplate.ui.settings.SettingsActivity
import com.theappsolutions.boilerplate.ui.splash.SplashActivity

import dagger.Subcomponent

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(activity: SplashActivity)

    fun inject(activity: AuthActivity)

    fun inject(activity: ProjectListActivity)

    fun inject(activity: SettingsActivity)

    fun fragmentComponent(fragmentModule: FragmentModule): FragmentComponent

    /*TODO uncomment for BottomMenu functionality*/
    /*void inject(BottomMenuActivity activity);*/
}
