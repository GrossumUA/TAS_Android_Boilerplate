package com.theappsolutions.boilerplate.util.other_utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class BuildInfoUtils {

    public static int getVersionCode(Context context) {
        int v = 0;
        try {
            v = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {

        }
        return v;
    }
}
