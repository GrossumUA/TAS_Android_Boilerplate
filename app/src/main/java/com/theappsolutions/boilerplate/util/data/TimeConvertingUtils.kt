package com.theappsolutions.boilerplate.util.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
object TimeConvertingUtils {
    val FORMAT_SEC_AND_MIL_SEC_2 = "MM-dd HH:mm"
    val FORMAT_SEC_AND_MIL_SEC = "mm:ss:SSS"
    val FORMAT_MIN_SEC_AND_MIL_SEC = "hh:mm:ss:SSS"

    fun getCurrentTime(): String {
        val time = System.currentTimeMillis()
        return " [Time: " + SimpleDateFormat(FORMAT_SEC_AND_MIL_SEC, Locale.getDefault()).format(time) + "]"
    }

    fun getCurrentTimeShort(): String {
        val time = System.currentTimeMillis()
        return SimpleDateFormat(
                FORMAT_MIN_SEC_AND_MIL_SEC, Locale.getDefault()).format(time) + ": "
    }

    fun getDateAndTime(): String {
        val sdfDate = SimpleDateFormat(FORMAT_SEC_AND_MIL_SEC_2, Locale.getDefault())
        val now = Date()
        return sdfDate.format(now)
    }
}
