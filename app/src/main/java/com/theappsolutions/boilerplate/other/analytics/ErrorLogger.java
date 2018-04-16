package com.theappsolutions.boilerplate.other.analytics;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.theappsolutions.boilerplate.TasBoilerplateApplication;

import io.reactivex.functions.Consumer;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Wrap all error logging services
 */
public class ErrorLogger {

    private static final String TAG = "nioxin";

    private boolean withCrashlytics;
    private boolean withLog;

    public ErrorLogger() {
    }

    public static Consumer<Throwable> errorLogger(Context context) {
        return TasBoilerplateApplication.get(context).getComponent().getErrorLogger()::logError;
    }

    public ErrorLogger withCrashlytics() {
        this.withCrashlytics = true;
        return this;
    }

    public ErrorLogger withLog() {
        this.withLog = true;
        return this;
    }

    public void logMessage(String log) {
        Crashlytics.log(log);
    }

    public void logError(Throwable t) {
        if (withCrashlytics) {
            Crashlytics.logException(t);
        }
        if (withLog) {
            Log.e(TAG, t.getMessage(), t);
        }
    }
}
