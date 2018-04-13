package com.theappsolutions.boilerplate.util.other_utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.theappsolutions.boilerplate.util.data_utils.StringUtils;

import java.util.UUID;

/**
 * Created by Dmytro Yakovlev.
 */
public class HardwareUtils {

    /**
     * Example: DFAA94B7158C41219FEB804B4CA46169
     */
    public static String createTransactionID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * Example: DFAA94B7-158C-4121-9FEB-804B4CA46169
     */
    public static String createUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    /**
     * Example: "19 (4.4.4)"
     */
    public static String getAndroidVersionFull() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return sdkVersion + " (" + release + ")";
    }

    /**
     * Example: "4.4.4"
     */
    public static String getAndroidReleaseVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Example: "Android 4.4.4"
     */
    public static String getAndroidReleaseVersionFull() {
        return "Android " + Build.VERSION.RELEASE;
    }

    /**
     * Example: "Samsung SM-T813"
     */
    public static String getFullDeviceName() {
        return StringUtils.capitalizeOnlyFirstLetter(Build.MANUFACTURER) + " " + Build.MODEL;
    }

    // TODO: apply more robust Identification.
    public static String getDeviceId(Context context) {
        TelephonyManager tm =
            (TelephonyManager) context.
                getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getDeviceId() != null) {
            return tm.getDeviceId(); //use for mobiles
        } else {
            return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID); //use for tablets
        }
    }
}
