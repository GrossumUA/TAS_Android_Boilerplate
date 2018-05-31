package com.theappsolutions.boilerplate

import android.Manifest

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
object TasBoilerplateSettings {

    val API = "https://dev.test.org/"
    val API_VERSION = "v1"

    val MIN_PASSWORD_LENGTH = 6
    val PASSWORD_SHOULD_HAVE_NUMBERS_AND_LETTERS = true
    val PASSWORD_SHOULD_HAVE_UPPERCASE_LETTERS = true

    var REALM_LOCAL_DB_VERSION: Long = 1
    val REALM_DB_NAME = "tas_boilerplate.realm"

    val SPLASH_SCREEN_DELAY_SEC = 2

    val NECESSARY_PERMISSIONS = arrayOf(android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)

    object TestSettings {
        val RESPONSE_ELEMENT_COUNT = 3
    }
}
