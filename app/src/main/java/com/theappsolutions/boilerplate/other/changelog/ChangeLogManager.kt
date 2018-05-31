package com.theappsolutions.boilerplate.other.changelog

import android.content.Context
import android.support.annotation.VisibleForTesting
import android.support.v4.app.FragmentActivity

import com.theappsolutions.boilerplate.util.other.getVersionCode

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Manages ChangeLog view
 */
class ChangeLogManager {
    private val activity: FragmentActivity
    private val preferenceManager: ChangeLogPreferencesHelper
    private var currentVersionCode: Int = 0

    constructor(activity: FragmentActivity) {
        this.activity = activity
        preferenceManager = ChangeLogPreferencesHelper(activity)
    }

    private val isChangeLogNeeded: Boolean
        get() {
            val cachedVersionCode = preferenceManager.versionCode
            currentVersionCode = getVersionCode(activity)
            return cachedVersionCode == ChangeLogPreferencesHelper.DEFAULT_VERSION_CODE || currentVersionCode != cachedVersionCode
        }

    fun analyze() {
        if (isChangeLogNeeded) {
            ChangeLogFragment.show(activity.supportFragmentManager)
            preferenceManager.versionCode = currentVersionCode
        }
    }

    companion object {
        @VisibleForTesting
        fun disable(context: Context) {
            ChangeLogPreferencesHelper(context).versionCode = getVersionCode(context)
        }
    }
}
