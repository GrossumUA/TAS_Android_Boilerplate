package com.theappsolutions.boilerplate.other.leak_canary;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.reflect.Field;

import static android.os.Build.MANUFACTURER;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.O_MR1;

/**
 * Fixes a leak caused by SemEmergencyManager. Tracked at https://github.com/square/leakcanary/issues/762
 */
public final class SemEmergencyManagerLeakingActivity implements Application.ActivityLifecycleCallbacks {
    private Application application;

    private SemEmergencyManagerLeakingActivity(Application application) {
        this.application = application;
    }

    public static void applyFix(Application application) {
        if (MANUFACTURER.equals("samsung") && SDK_INT >= KITKAT && SDK_INT <= O_MR1) {
            application.registerActivityLifecycleCallbacks(new SemEmergencyManagerLeakingActivity(application));
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        try {
            swapActivityWithApplicationContext();
        } catch (Exception ignored) {
            // the same result is expected on subsequent tries.
        }
        application.unregisterActivityLifecycleCallbacks(this);
    }

    private void swapActivityWithApplicationContext()
        throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class<?> semEmergencyManagerClass = Class.forName("com.samsung.android.emergencymode.SemEmergencyManager");
        Field sInstanceField = semEmergencyManagerClass.getDeclaredField("sInstance");
        sInstanceField.setAccessible(true);
        Object sInstance = sInstanceField.get(null);
        Field mContextField = semEmergencyManagerClass.getDeclaredField("mContext");
        mContextField.setAccessible(true);
        mContextField.set(sInstance, application);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }
}