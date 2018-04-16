package com.theappsolutions.boilerplate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.theappsolutions.boilerplate.injection.component.ApplicationComponent;
import com.theappsolutions.boilerplate.injection.component.DaggerApplicationComponent;
import com.theappsolutions.boilerplate.injection.module.ApplicationModule;
import com.theappsolutions.boilerplate.other.leak_canary.SemEmergencyManagerLeakingActivity;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;
import static android.os.Build.VERSION_CODES.O_MR1;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public class TasBoilerplateApplication extends MultiDexApplication {

    ApplicationComponent applicationComponent;

    public static TasBoilerplateApplication get(Context context) {
        return (TasBoilerplateApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SemEmergencyManagerLeakingActivity.applyFix(this);

        initTimberTrees();
        setUpRealm();

        Fabric.with(this, new Crashlytics(), new Answers());
        if (BuildConfig.DEBUG) {
            initStetho();
            setUpLeakCanary();
        }
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }

    private void setUpRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(TasBoilerplateSettings.REALM_DB_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    private void initTimberTrees() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new CustomDebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private void setUpLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        enabledStrictMode();
        LeakCanary.install(this);
    }

    @SuppressLint("ObsoleteSdkInt")
    private void enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD && SDK_INT < O_MR1) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag,
                           @NonNull String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            Crashlytics.log(priority, tag, message);

            if (t != null && (priority == Log.ERROR || priority == Log.WARN)) {
                Crashlytics.logException(t);
            }
        }
    }

    private static class CustomDebugTree extends Timber.DebugTree {
        @Override
        protected void log(int priority, String tag,
                           @NonNull String message, Throwable t) {
            message = "##### " + message;
            super.log(priority, tag, message, t);
        }
    }

}
