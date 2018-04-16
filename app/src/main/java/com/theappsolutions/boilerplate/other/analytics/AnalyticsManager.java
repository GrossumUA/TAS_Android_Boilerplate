package com.theappsolutions.boilerplate.other.analytics;

import android.app.Application;
import android.os.Bundle;

import com.annimon.stream.Optional;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

import static com.annimon.stream.Optional.empty;
import static com.annimon.stream.Optional.of;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Wrap all analytics services and provides single assess to them
 */
public class AnalyticsManager {

    private Optional<FabricAnswersManager> fabricManager = empty();
    private Optional<GoogleAnalyticsManager> googleManager = empty();

    public AnalyticsManager withFabricAnswers(Application app) {
        fabricManager = of(new FabricAnswersManager());
        return this;
    }

    public AnalyticsManager withGoogleAnalytics(Application app) {
        googleManager = of(new GoogleAnalyticsManager(app));
        return this;
    }


    public void logEvent(AnalyticsEvent event) {
        logEvent(event, new Bundle());
    }

    public void logEvent(AnalyticsEvent event, Bundle bundle) {
        fabricManager.ifPresent(fabricManager -> fabricManager.logEvent(event));
        googleManager.ifPresent(googleManager -> googleManager.logEvent(event, bundle));
    }


    private static class FabricAnswersManager {
        public void logEvent(AnalyticsEvent event) {
            Answers.getInstance().logCustom(new CustomEvent(event.label));
        }
    }

    private static class GoogleAnalyticsManager {
        private Application application;
        private FirebaseAnalytics firebaseAnalytics;

        GoogleAnalyticsManager(Application application) {
            this.application = application;
            firebaseAnalytics = FirebaseAnalytics.getInstance(application);
        }

        public void logEvent(AnalyticsEvent event) {
            logEvent(event, new Bundle());
        }

        public void logEvent(AnalyticsEvent event, Bundle bundle) {
            firebaseAnalytics.logEvent(event.label, bundle);
        }
    }
}
