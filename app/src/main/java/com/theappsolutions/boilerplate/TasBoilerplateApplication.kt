package com.theappsolutions.boilerplate

import android.annotation.SuppressLint
import android.content.Context
import android.os.StrictMode
import android.support.multidex.MultiDexApplication
import android.util.Log

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.theappsolutions.boilerplate.injection.component.ApplicationComponent
import com.theappsolutions.boilerplate.injection.component.DaggerApplicationComponent
import com.theappsolutions.boilerplate.injection.module.ApplicationModule
import com.theappsolutions.boilerplate.other.leakcanary.SemEmergencyManagerLeakingActivity

import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.GINGERBREAD
import android.os.Build.VERSION_CODES.O_MR1
import com.theappsolutions.boilerplate.util.data.hasLetters


/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
class TasBoilerplateApplication : MultiDexApplication() {

    private lateinit var applicationComponent: ApplicationComponent

    // Needed to replace the component with a test specific one
    fun getComponent(): ApplicationComponent {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(ApplicationModule(this))
                    .build()
        return applicationComponent
    }

    // Needed to replace the component with a test specific one
    fun setComponent(applicationComponent: ApplicationComponent) {
        this.applicationComponent = applicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        SemEmergencyManagerLeakingActivity.applyFix(this)

        initTimberTrees()
        setUpRealm()

        Fabric.with(this, Crashlytics(), Answers())
        if (BuildConfig.DEBUG) {
            initStetho()
            setUpLeakCanary()
        }
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }

    private fun setUpRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .name(TasBoilerplateSettings.REALM_DB_NAME)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }

    private fun initTimberTrees() {
        if (BuildConfig.DEBUG) {
            Timber.plant(CustomDebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    private fun setUpLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        enabledStrictMode()
        LeakCanary.install(this)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD && SDK_INT < O_MR1) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build())
        }
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?,
                         message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

            Crashlytics.log(priority, tag, message)

            if (t != null && (priority == Log.ERROR || priority == Log.WARN)) {
                Crashlytics.logException(t)
            }
        }
    }

    private class CustomDebugTree : Timber.DebugTree() {
        override fun log(priority: Int, tag: String?,
                         message: String, t: Throwable?) {
            var message = message
            message = "##### $message"
            super.log(priority, tag, message, t)
        }
    }

    companion object {
        operator fun get(context: Context): TasBoilerplateApplication {
            return context.applicationContext as TasBoilerplateApplication
        }
    }

}
