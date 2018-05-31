package com.theappsolutions.boilerplate.other.analytics

import android.app.Application
import android.os.Bundle

import com.annimon.stream.Optional
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.google.firebase.analytics.FirebaseAnalytics

import com.annimon.stream.Optional.empty
import com.annimon.stream.Optional.of

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Wrap all analytics services and provides single assess to them
 */
class AnalyticsManager {

    private var fabricManager = empty<FabricAnswersManager>()
    private var googleManager = empty<GoogleAnalyticsManager>()

    fun withFabricAnswers(app: Application): AnalyticsManager {
        fabricManager = of(FabricAnswersManager())
        return this
    }

    fun withGoogleAnalytics(app: Application): AnalyticsManager {
        googleManager = of(GoogleAnalyticsManager(app))
        return this
    }

    @JvmOverloads
    fun logEvent(event: AnalyticsEvent, bundle: Bundle = Bundle()) {
        fabricManager.ifPresent { fabricManager -> fabricManager.logEvent(event) }
        googleManager.ifPresent { googleManager -> googleManager.logEvent(event, bundle) }
    }


    private class FabricAnswersManager {
        fun logEvent(event: AnalyticsEvent) {
            Answers.getInstance().logCustom(CustomEvent(event.label))
        }
    }

    private class GoogleAnalyticsManager internal constructor(private val application: Application) {
        private val firebaseAnalytics: FirebaseAnalytics

        init {
            firebaseAnalytics = FirebaseAnalytics.getInstance(application)
        }

        @JvmOverloads
        fun logEvent(event: AnalyticsEvent, bundle: Bundle = Bundle()) {
            firebaseAnalytics.logEvent(event.label, bundle)
        }
    }
}
