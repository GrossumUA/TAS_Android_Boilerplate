package com.theappsolutions.boilerplate.util.data;

import android.annotation.SuppressLint;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class RoundingHelper {

    @SuppressLint("DefaultLocale")
    public static String formatFloat(float d) {
        if (d == (int) d)
            return String.format("%d", (int) d);
        else
            return String.format("%s", d);
    }
}
