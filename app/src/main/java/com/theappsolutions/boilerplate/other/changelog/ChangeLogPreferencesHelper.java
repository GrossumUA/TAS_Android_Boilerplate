package com.theappsolutions.boilerplate.other.changelog;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 * <p>
 * Separate Shared Preferences access for {@link ChangeLogManager}
 */
public class ChangeLogPreferencesHelper {

    public static final int DEFAULT_VERSION_CODE = -1;
    private static final String PREF_FILE_NAME = "android_version_code";
    private static final String PREF_KEY_PREVIOUS_VERSION_CODE = "v_code";

    private final SharedPreferences preferences;

    public ChangeLogPreferencesHelper(Context context) {
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setVersionCode(int versionCode) {
        preferences.edit().putInt(PREF_KEY_PREVIOUS_VERSION_CODE, versionCode).apply();
    }

    public int getVersionCode() {
        return preferences.getInt(PREF_KEY_PREVIOUS_VERSION_CODE, DEFAULT_VERSION_CODE);
    }

}
