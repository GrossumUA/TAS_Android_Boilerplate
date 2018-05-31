package com.theappsolutions.boilerplate.util.ui

import android.content.Context
import android.os.Handler
import android.widget.Toast

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
object ToastUtils {
    fun useLongToast(context: Context?, message: String?) {
        if (context == null || message == null) {
            return
        }

        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.show()
    }


    fun useShortToast(context: Context?, message: String?) {
        if (context == null || message == null) {
            return
        }
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun useLongToastForIntegerSet(ourSet: Set<Int>, context: Context) {
        val printStr = StringBuilder("Set : ")

        val itr = ourSet.iterator()
        while (itr.hasNext()) {
            val element = itr.next()
            printStr.append(element.toString()).append(", ")
        }

        useLongToast(context, printStr.toString())
    }


    fun useToastInService(handler: Handler?,
                          context: Context, message: String) {
        handler?.post { useLongToast(context, message) }
    }
}
