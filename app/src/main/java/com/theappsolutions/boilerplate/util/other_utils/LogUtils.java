package com.theappsolutions.boilerplate.util.other_utils;

import timber.log.Timber;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class LogUtils {

    public static void logThread(String str) {
        String name = Thread.currentThread().getName();
        str += " [ " + name + " ] ";
        Timber.d(str);
    }
}
