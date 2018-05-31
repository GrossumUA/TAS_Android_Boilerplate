package com.theappsolutions.boilerplate.other.changelog

import android.content.Context
import android.content.SharedPreferences

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 *
 * Separate Shared Preferences access for [ChangeLogManager]
 */
class ChangeLogPreferencesHelper {
    private val preferences: SharedPreferences
    var versionCode: Int
        get() = preferences.getInt(PREF_KEY_PREVIOUS_VERSION_CODE, DEFAULT_VERSION_CODE)
        set(versionCode) = preferences.edit().putInt(PREF_KEY_PREVIOUS_VERSION_CODE, versionCode).apply()

    constructor(context: Context) {
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        val DEFAULT_VERSION_CODE = -1
        private val PREF_FILE_NAME = "android_version_code"
        private val PREF_KEY_PREVIOUS_VERSION_CODE = "v_code"
    }

}
