package com.theappsolutions.boilerplate.other.analytics

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
enum class AnalyticsEvent private constructor(val label: String) {
    ATTEMPT_TO_LOGIN("Attempt to login"),
    SUCCESS_LOGIN("Success login"),
    SYNC_PROJECTS("Sync projects"),
    ENTER_SETTINGS("Enter settings"),
    LOG_OUT("Log out");
}
