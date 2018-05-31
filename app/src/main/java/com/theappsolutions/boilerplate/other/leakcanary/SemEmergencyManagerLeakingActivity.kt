package com.theappsolutions.boilerplate.other.leakcanary

import android.app.Activity
import android.app.Application
import android.os.Bundle

import android.os.Build.MANUFACTURER
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.KITKAT
import android.os.Build.VERSION_CODES.O_MR1

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 * Fixes a leak caused by SemEmergencyManager. Tracked at https://github.com/square/leakcanary/issues/762
 */
class SemEmergencyManagerLeakingActivity private constructor(private val application: Application) : Application.ActivityLifecycleCallbacks {

    override fun onActivityDestroyed(activity: Activity) {
        try {
            swapActivityWithApplicationContext()
        } catch (ignored: Exception) {
            // the same result is expected on subsequent tries.
        }
        application.unregisterActivityLifecycleCallbacks(this)
    }

    @Throws(ClassNotFoundException::class, NoSuchFieldException::class, IllegalAccessException::class)
    private fun swapActivityWithApplicationContext() {
        val semEmergencyManagerClass = Class.forName("com.samsung.android.emergencymode.SemEmergencyManager")
        val sInstanceField = semEmergencyManagerClass.getDeclaredField("sInstance")
        sInstanceField.isAccessible = true
        val sInstance = sInstanceField.get(null)
        val mContextField = semEmergencyManagerClass.getDeclaredField("mContext")
        mContextField.isAccessible = true
        mContextField.set(sInstance, application)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    companion object {
        fun applyFix(application: Application) {
            if (MANUFACTURER == "samsung" && SDK_INT >= KITKAT && SDK_INT <= O_MR1) {
                application.registerActivityLifecycleCallbacks(SemEmergencyManagerLeakingActivity(application))
            }
        }
    }
}