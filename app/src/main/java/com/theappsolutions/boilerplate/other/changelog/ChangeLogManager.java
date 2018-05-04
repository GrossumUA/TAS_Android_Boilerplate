package com.theappsolutions.boilerplate.other.changelog;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;

import com.theappsolutions.boilerplate.util.other.BuildInfoUtils;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Manages ChangeLog view
 */
public class ChangeLogManager {

    private FragmentActivity activity;
    private ChangeLogPreferencesHelper preferenceManager;
    private int currentVersionCode;

    public ChangeLogManager(FragmentActivity context) {
        this.activity = context;
        preferenceManager = new ChangeLogPreferencesHelper(context);
    }

    private boolean isChangeLogNeeded() {
        int cachedVersionCode = preferenceManager.getVersionCode();
        currentVersionCode = BuildInfoUtils.getVersionCode(activity);

        return cachedVersionCode == ChangeLogPreferencesHelper.DEFAULT_VERSION_CODE ||
                currentVersionCode != cachedVersionCode;
    }

    public void analyze() {
        if (isChangeLogNeeded()) {
            ChangeLogFragment.show(activity.getSupportFragmentManager());
            preferenceManager.setVersionCode(currentVersionCode);
        }
    }

    @VisibleForTesting
    public static void disable(Context context) {
        new ChangeLogPreferencesHelper(context).setVersionCode(BuildInfoUtils.getVersionCode(context));
    }
}
