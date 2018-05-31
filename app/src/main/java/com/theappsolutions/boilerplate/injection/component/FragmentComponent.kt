package com.theappsolutions.boilerplate.injection.component

import com.theappsolutions.boilerplate.injection.PerFragment
import com.theappsolutions.boilerplate.injection.module.FragmentModule
import com.theappsolutions.boilerplate.ui.settings.SettingsFragment

import dagger.Subcomponent

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 *
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

    fun inject(fragment: SettingsFragment)
}
