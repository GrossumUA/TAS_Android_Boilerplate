package com.theappsolutions.boilerplate.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate.TasBoilerplateSettings;
import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.data.PreferencesManager;
import com.theappsolutions.boilerplate.data.data_sources.local.DataRepository;
import com.theappsolutions.boilerplate.data.data_sources.local.DataRepositoryImpl;
import com.theappsolutions.boilerplate.data.data_sources.local.RealmManager;
import com.theappsolutions.boilerplate.data.data_sources.remote.RestApiClient;
import com.theappsolutions.boilerplate.data.data_sources.remote.RestService;
import com.theappsolutions.boilerplate.data.data_sources.remote.RetrofitBuilder;
import com.theappsolutions.boilerplate.injection.ApplicationContext;
import com.theappsolutions.boilerplate.injection.DefaultRequestHeaders;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager;
import com.theappsolutions.boilerplate.other.analytics.ErrorLogger;
import com.theappsolutions.boilerplate.other.api.RequestsHandler;
import com.theappsolutions.boilerplate.util.data_utils.ValidationUtils;
import com.theappsolutions.boilerplate.util.other_utils.ApiUtils;
import com.theappsolutions.boilerplate.util.storage_utils.FileSystemHelper;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
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

    @Provides
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    FileSystemHelper provideFileSystemHelper(@ApplicationContext Context context) {
        return new FileSystemHelper(context);
    }

    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
    }

    @Provides
    @Singleton
    RequestsHandler provideRequestsHandler(@ApplicationContext Context context) {
        return new RequestsHandler(context);
    }

    @Provides
    @DefaultRequestHeaders
    Map<String, String> provideDefaultRequestHeaders() {
        return new HashMap<>();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(@ApplicationContext Context context,
                             RequestsHandler requestsHandler,
                             Gson gson,
                             @DefaultRequestHeaders Map<String, String> headers,
                             PreferencesManager preferencesManager) {
        return RetrofitBuilder.buildRetrofitInstance(context, requestsHandler, gson, headers,
                TasBoilerplateSettings.API, preferencesManager);
    }

    @Provides
    @Singleton
    RestService provideRestService(Retrofit retrofit) {
        return retrofit.create(RestService.class);
    }

    @Provides
    ApiUtils provideApiUtils(Gson gson) {
        return new ApiUtils(gson);
    }

    @Provides
    @Singleton
    RealmManager provideRealmManager(@ApplicationContext Context context) {
        return new RealmManager(context);
    }

    @Provides
    @Singleton
    DataRepository provideSettingsRepository(RealmManager realmManager) {
        return new DataRepositoryImpl(realmManager);
    }

    @Provides
    DataManager provideDataManager(RestApiClient restApiClient,
                                   PreferencesManager preferencesManager,
                                   DataRepository dataRepository) {
        return new DataManager(restApiClient, preferencesManager, dataRepository);
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


