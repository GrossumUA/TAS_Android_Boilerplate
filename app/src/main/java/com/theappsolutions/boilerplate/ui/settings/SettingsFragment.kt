package com.theappsolutions.boilerplate.ui.settings

import android.content.Context
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat

import com.theappsolutions.boilerplate.R

/**
 * @author Dmytro Yakovlev <d.yakovlev></d.yakovlev>@theappsolutions.com>
 * @copyright (c) The App Solutions. (https://theappsolutions.com)
 */
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {


    private var callback: SettingsCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as SettingsCallback?
    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_screen, rootKey)
        initPrefs()
    }

    private fun initPrefs() {
        findPreference(getString(R.string.id_pref_logout)).onPreferenceClickListener = this
        val switchBackgroundJob = findPreference(getString(R.string.id_pref_switcher))
        switchBackgroundJob.onPreferenceChangeListener = this
        switchBackgroundJob.isEnabled = arguments?.getBoolean(AUTOLOGIN_ENABLED_ARGS) ?: false
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        if (isAppropriatePreference(preference, R.string.id_pref_logout)) {
            callback?.onLogoutSelected()
            return true
        }
        return false
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (isAppropriatePreference(preference, R.string.id_pref_switcher)) {
            callback?.onAutoLoginSwitched(newValue as Boolean)
            return true
        }
        return false
    }

    private fun isAppropriatePreference(preference: Preference, resId: Int): Boolean {
        return preference.key == getString(resId)
    }

    interface SettingsCallback {

        fun onAutoLoginSwitched(isEnabled: Boolean)

        fun onLogoutSelected()
    }

    companion object {

        private val AUTOLOGIN_ENABLED_ARGS = "is_autologin_enabled"

        fun newInstance(autologinEnabled: Boolean): SettingsFragment {
            val args = Bundle()
            val fragment = SettingsFragment()
            args.putBoolean(AUTOLOGIN_ENABLED_ARGS, autologinEnabled)
            fragment.arguments = args
            return fragment
        }
    }
}