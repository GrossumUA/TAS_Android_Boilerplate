package com.theappsolutions.boilerplate.util.data

import android.annotation.SuppressLint

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
object RoundingHelper {

    @SuppressLint("DefaultLocale")
    fun formatFloat(d: Float): String {
        when (d) {
            d.toInt().toFloat() -> return String.format("%d", d.toInt())
            else -> return String.format("%s", d)
        }
    }
}
