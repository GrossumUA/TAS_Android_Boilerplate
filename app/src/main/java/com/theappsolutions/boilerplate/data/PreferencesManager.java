package com.theappsolutions.boilerplate.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.annimon.stream.Optional;
import com.theappsolutions.boilerplate.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesManager {

    private static final String PREF_FILE_NAME = "android_tas_pref_file";
    private static final String PREF_KEY_TOKEN = "token";
    private static final String PREF_IS_AUTOLOGIN_ENABLED = "is_autologin_enabled";

    private final SharedPreferences preferences;
    private Context context;

    @Inject
    public PreferencesManager(@ApplicationContext Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public void setToken(String token) {
        preferences.edit().putString(PREF_KEY_TOKEN, token).apply();
    }

    public Optional<String> getToken() {
        return Optional.ofNullable(preferences.getString(PREF_KEY_TOKEN, null));
    }

    public void removeToken() {
        preferences.edit().remove(PREF_KEY_TOKEN).apply();
    }

    public void setAutologinEnabled(boolean isEnabled) {
        preferences.edit().putBoolean(PREF_IS_AUTOLOGIN_ENABLED, isEnabled).apply();
    }

    public boolean getAutologinEnabled() {
        return preferences.getBoolean(PREF_IS_AUTOLOGIN_ENABLED, true);
    }
}
