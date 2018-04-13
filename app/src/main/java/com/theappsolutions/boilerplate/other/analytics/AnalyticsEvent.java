package com.theappsolutions.boilerplate.other.analytics;

public enum AnalyticsEvent {

    ATTEMPT_TO_LOGIN("Attempt to login"),
    SUCCESS_LOGIN("Success login"),
    SYNC_PROJECTS("Sync projects"),
    ENTER_SETTINGS("Enter settings"),
    LOG_OUT("Log out");

    public final String label;

    AnalyticsEvent(String label) {
        this.label = label;
    }
}
