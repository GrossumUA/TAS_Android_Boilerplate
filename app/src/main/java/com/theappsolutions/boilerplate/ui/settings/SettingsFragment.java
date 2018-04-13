package com.theappsolutions.boilerplate.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.theappsolutions.boilerplate.R;

/**
 * @author Dmytro Yakovlev <d.yakovlev@theappsolutions.com>
 * @copyright (c) The App Solutions. (https://theappsolutions.com)
 */
public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private static String AUTOLOGIN_ENABLED_ARGS = "is_autologin_enabled";
    private SettingsCallback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (SettingsCallback) context;
    }

    public static SettingsFragment newInstance(boolean autologinEnabled) {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        args.putBoolean(AUTOLOGIN_ENABLED_ARGS, autologinEnabled);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_screen, rootKey);
        initPrefs();
    }

    private void initPrefs() {
        findPreference(getString(R.string.id_pref_logout)).setOnPreferenceClickListener(this);

        Preference switchBackgroundJob = findPreference(getString(R.string.id_pref_switcher));
        switchBackgroundJob.setOnPreferenceChangeListener(this);

        switchBackgroundJob.setEnabled(getArguments().getBoolean(AUTOLOGIN_ENABLED_ARGS));
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (isAppropriatePreference(preference, R.string.id_pref_logout)) {
            callback.onLogoutSelected();
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (isAppropriatePreference(preference, R.string.id_pref_switcher)) {
            callback.onAutoLoginSwitched((boolean) newValue);
            return true;
        }
        return false;
    }

    private boolean isAppropriatePreference(Preference preference, int resId) {
        return preference.getKey().equals(getString(resId));
    }

    public interface SettingsCallback {

        void onAutoLoginSwitched(boolean isEnabled);

        void onLogoutSelected();
    }
}