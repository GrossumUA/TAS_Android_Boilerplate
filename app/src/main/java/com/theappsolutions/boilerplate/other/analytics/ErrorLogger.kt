package com.theappsolutions.boilerplate.other.analytics

import android.content.Context
import android.util.Log

import com.crashlytics.android.Crashlytics
import com.theappsolutions.boilerplate.TasBoilerplateApplication

import io.reactivex.functions.Consumer

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Wrap all error logging services
 */
class ErrorLogger {

    private var withCrashlytics: Boolean = false
    private var withLog: Boolean = false

    fun withCrashlytics(): ErrorLogger {
        this.withCrashlytics = true
        return this
    }

    fun withLog(): ErrorLogger {
        this.withLog = true
        return this
    }

    fun logMessage(log: String) {
        Crashlytics.log(log)
    }

    fun logError(t: Throwable) {
        if (withCrashlytics) {
            Crashlytics.logException(t)
        }
        if (withLog) {
            Log.e(TAG, t.message, t)
        }
    }

    companion object {
        private val TAG = "nioxin"
        fun errorLogger(context: Context): Consumer<Throwable> {
            return Consumer { TasBoilerplateApplication.get(context).getComponent().errorLogger.logError(it) }
        }
    }
}
