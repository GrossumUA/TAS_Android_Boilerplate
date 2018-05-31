package com.theappsolutions.boilerplate.injection.component

import android.app.Application
import android.content.Context

import com.google.gson.Gson
import com.theappsolutions.boilerplate.data.DataManager
import com.theappsolutions.boilerplate.data.PreferencesManager
import com.theappsolutions.boilerplate.injection.ApplicationContext
import com.theappsolutions.boilerplate.injection.module.ApplicationModule
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager
import com.theappsolutions.boilerplate.other.analytics.ErrorLogger
import com.theappsolutions.boilerplate.util.data.ValidationUtils

import javax.inject.Singleton

import dagger.Component

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    val errorLogger: ErrorLogger

    val analyticsManager: AnalyticsManager

    val validationUtils: ValidationUtils

    @ApplicationContext
    fun context(): Context

    fun application(): Application

    fun preferencesHelper(): PreferencesManager

    fun gson(): Gson

    fun dataManager(): DataManager
}
