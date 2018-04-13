package com.theappsolutions.boilerplate.test.common.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate.TasBoilerplateSettings;
import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.data.PreferencesManager;
import com.theappsolutions.boilerplate.data.data_sources.remote.RestService;
import com.theappsolutions.boilerplate.injection.ApplicationContext;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager;
import com.theappsolutions.boilerplate.other.analytics.ErrorLogger;
import com.theappsolutions.boilerplate.util.data_utils.ValidationUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Provides application-level dependencies for an app running on a testing environment
 * This allows injecting mocks if necessary.
 */
@Module
public class ApplicationTestModule {

    private final Application mApplication;

    public ApplicationTestModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    /************* MOCKS *************/

    @Provides
    @Singleton
    DataManager provideDataManager() {
        return mock(DataManager.class);
    }

    @Provides
    @Singleton
    RestService provideService() {
        return mock(RestService.class);
    }


    @Provides
    @Singleton
    PreferencesManager providePreferencesManager() {
        return mock(PreferencesManager.class);
    }

    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
    }

    @Provides
    @Singleton
    ErrorLogger provideErrorLogger(@ApplicationContext Context context) {
        return new ErrorLogger()
                .withLog()
                .withCrashlytics();
    }

    @Provides
    @Singleton
    AnalyticsManager provideAnalyticsManager() {
        return new AnalyticsManager()
                .withGoogleAnalytics(mApplication)
                .withFabricAnswers(mApplication);
    }

    @Provides
    @Singleton
    ValidationUtils provideValidationUtils(@ApplicationContext Context context) {
        Resources res = context.getResources();
        return new ValidationUtils.Builder(res.getString(R.string.error_empty_field))
                .setPasswordValidation(new ValidationUtils.Builder.PasswordValidation(
                        ValidationUtils.ValidationPair.withDynamicResource(TasBoilerplateSettings.MIN_PASSWORD_LENGTH,
                                res.getString(R.string.error_short_pass)),
                        new ValidationUtils.ValidationPair<>(TasBoilerplateSettings.PASSWORD_SHOULD_HAVE_NUMBERS_AND_LETTERS,
                                res.getString(R.string.error_pass_should_contains_letter_and_digits)),
                        new ValidationUtils.ValidationPair<>(TasBoilerplateSettings.PASSWORD_SHOULD_HAVE_UPPERCASE_LETTERS,
                                res.getString(R.string.error_pass_should_contains_uppercase))))
                .setEmailValidation(
                        new ValidationUtils.Builder.EmailValidation(res.getString(R.string.error_email_did_not_matches)))
                .build();
    }
}
