package com.theappsolutions.boilerplate.util.other

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager

import com.theappsolutions.boilerplate.util.data.StringUtils

import java.util.UUID

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */


/**
 * Example: "19 (4.4.4)"
 */
fun getAndroidVersionFull(): String {
    val release = Build.VERSION.RELEASE
    val sdkVersion = Build.VERSION.SDK_INT
    return "($sdkVersion  $release )"
}

/**
 * Example: "4.4.4"
 */
fun getAndroidReleaseVersion(): String = Build.VERSION.RELEASE

/**
 * Example: "Android 4.4.4"
 */
fun getAndroidReleaseVersionFull(): String = "Android ${Build.VERSION.RELEASE}"

/**
 * Example: "Samsung SM-T813"
 */
fun getFullDeviceName(): String = "${StringUtils.capitalizeOnlyFirstLetter(Build.MANUFACTURER)} ${Build.MODEL}"

/**
 * Example: DFAA94B7158C41219FEB804B4CA46169
 */
fun createTransactionID(): String {
    return UUID.randomUUID()
            .toString()
            .replace("-".toRegex(), "")
            .toUpperCase()
}

/**
 * Example: DFAA94B7-158C-4121-9FEB-804B4CA46169
 */
fun createUUID(): String {
    return UUID.randomUUID()
            .toString()
            .toUpperCase()
}

@Suppress("DEPRECATION")
@SuppressLint("MissingPermission", "HardwareIds")
// TODO: apply more robust Identification.
fun getDeviceId(context: Context): String {
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    when {
        tm.deviceId != null -> return tm.deviceId.toString() //use for mobiles
        else -> return Settings.Secure.getString(context.contentResolver,
                Settings.Secure.ANDROID_ID) //use for tablets
    }
}

