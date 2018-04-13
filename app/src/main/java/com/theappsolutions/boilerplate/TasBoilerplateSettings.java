package com.theappsolutions.boilerplate;

import android.Manifest;

public class TasBoilerplateSettings {

    public static final String API = "https://dev.test.org/";
    public static final String API_VERSION = "v1";

    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final boolean PASSWORD_SHOULD_HAVE_NUMBERS_AND_LETTERS = true;
    public static final boolean PASSWORD_SHOULD_HAVE_UPPERCASE_LETTERS = true;

    public static long REALM_LOCAL_DB_VERSION = 1;
    public static final String REALM_DB_NAME = "tas_boilerplate.realm";


    public static final String[] NECESSARY_PERMISSIONS = {
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static class TestSettings {
        public static final int RESPONSE_ELEMENT_COUNT = 3;
    }
}
