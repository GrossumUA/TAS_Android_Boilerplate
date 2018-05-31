package com.theappsolutions.boilerplate.util.other

import android.content.Context
import android.content.pm.PackageManager

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
fun getVersionCode(context: Context): Int {
    var v = 0
    try {
        v = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
    } catch (ignored: PackageManager.NameNotFoundException) {

    }
    return v
}
