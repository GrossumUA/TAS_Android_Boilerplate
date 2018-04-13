package com.theappsolutions.boilerplate.util.data_utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dmytro Yakovlev.
 */
public class TimeConvertingUtils {

    public static final String FORMAT_SEC_AND_MIL_SEC = "mm:ss:SSS";
    public static final String FORMAT_MIN_SEC_AND_MIL_SEC = "hh:mm:ss:SSS";

    public static String getCurrentTime() {
        long time = System.currentTimeMillis();
        return " [Time: " + new SimpleDateFormat(FORMAT_SEC_AND_MIL_SEC, Locale.getDefault()).
                format(time) + "]";
    }

    public static String getCurrentTimeShort() {
        long time = System.currentTimeMillis();
        return new SimpleDateFormat(
                FORMAT_MIN_SEC_AND_MIL_SEC, Locale.getDefault()).format(time) + ": ";
    }


    private static final String FORMAT_SEC_AND_MIL_SEC_2 = "MM-dd HH:mm";

    public static String getDateAndTime() {
        SimpleDateFormat sdfDate =
                new SimpleDateFormat(FORMAT_SEC_AND_MIL_SEC_2, Locale.getDefault());
        Date now = new Date();
        return sdfDate.format(now);
    }
}
