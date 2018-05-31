package com.theappsolutions.boilerplate.data

import android.content.Context
import android.content.SharedPreferences

import com.annimon.stream.Optional
import com.theappsolutions.boilerplate.injection.ApplicationContext

import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@Singleton
class PreferencesManager @Inject
constructor(@param:ApplicationContext private val context: Context) {

    private val preferences: SharedPreferences

    fun getToken(): Optional<String> = Optional.ofNullable(preferences.getString(PREF_KEY_TOKEN, null))

    var autologinEnabled: Boolean
        get() = preferences.getBoolean(PREF_IS_AUTOLOGIN_ENABLED, true)
        set(isEnabled) = preferences.edit().putBoolean(PREF_IS_AUTOLOGIN_ENABLED, isEnabled).apply()

    init {
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

    fun setToken(token: String) {
        preferences.edit().putString(PREF_KEY_TOKEN, token).apply()
    }

    fun removeToken() {
        preferences.edit().remove(PREF_KEY_TOKEN).apply()
    }

    companion object {

        private val PREF_FILE_NAME = "android_tas_pref_file"
        private val PREF_KEY_TOKEN = "token"
        private val PREF_IS_AUTOLOGIN_ENABLED = "is_autologin_enabled"
    }
}
