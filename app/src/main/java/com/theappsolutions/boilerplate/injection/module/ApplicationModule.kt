package com.theappsolutions.boilerplate.injection.module

import android.app.Application
import android.content.Context
import android.content.res.Resources

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.TasBoilerplateSettings
import com.theappsolutions.boilerplate.data.DataManager
import com.theappsolutions.boilerplate.data.PreferencesManager
import com.theappsolutions.boilerplate.data.datasources.local.DataRepository
import com.theappsolutions.boilerplate.data.datasources.local.DataRepositoryImpl
import com.theappsolutions.boilerplate.data.datasources.local.RealmManager
import com.theappsolutions.boilerplate.data.datasources.remote.RestApiClient
import com.theappsolutions.boilerplate.data.datasources.remote.RestService
import com.theappsolutions.boilerplate.data.datasources.remote.RetrofitBuilder
import com.theappsolutions.boilerplate.injection.ApplicationContext
import com.theappsolutions.boilerplate.injection.DefaultRequestHeaders
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager
import com.theappsolutions.boilerplate.other.analytics.ErrorLogger
import com.theappsolutions.boilerplate.other.api.RequestsHandler
import com.theappsolutions.boilerplate.util.data.ValidationUtils
import com.theappsolutions.boilerplate.util.other.ApiUtils
import com.theappsolutions.boilerplate.util.storage.FileSystemHelper

import java.util.HashMap

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.realm.Realm
import retrofit2.Retrofit

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 *
 * Provide application-level dependencies.
 */
@Module
class ApplicationModule(protected val mApplication: Application) {

    @Provides
    fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return mApplication
    }

    @Provides
    fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }

    @Provides
    fun provideFileSystemHelper(@ApplicationContext context: Context): FileSystemHelper {
        return FileSystemHelper(context)
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create()
    }

    @Provides
    @Singleton
    fun provideRequestsHandler(@ApplicationContext context: Context): RequestsHandler {
        return RequestsHandler(context)
    }

    @Provides
    @DefaultRequestHeaders
    fun provideDefaultRequestHeaders(): Map<String, String> {
        return HashMap()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context,
                                 requestsHandler: RequestsHandler,
                                 gson: Gson,
                                 @DefaultRequestHeaders headers: Map<String, String>,
                                 preferencesManager: PreferencesManager): Retrofit {
        return RetrofitBuilder.buildRetrofitInstance(context, requestsHandler, gson, headers,
                TasBoilerplateSettings.API, preferencesManager)
    }

    @Provides
    @Singleton
    fun provideRestService(retrofit: Retrofit): RestService {
        return retrofit.create(RestService::class.java)
    }

    @Provides
    fun provideApiUtils(gson: Gson): ApiUtils {
        return ApiUtils(gson)
    }

    @Provides
    @Singleton
    fun provideRealmManager(@ApplicationContext context: Context): RealmManager {
        return RealmManager(context)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(realmManager: RealmManager): DataRepository {
        return DataRepositoryImpl(realmManager)
    }

    @Provides
    fun provideDataManager(restApiClient: RestApiClient,
                                    preferencesManager: PreferencesManager,
                                    dataRepository: DataRepository): DataManager {
        return DataManager(restApiClient, preferencesManager, dataRepository)
    }

    @Provides
    @Singleton
    fun provideErrorLogger(@ApplicationContext context: Context): ErrorLogger {
        return ErrorLogger()
                .withLog()
                .withCrashlytics()
    }

    @Provides
    @Singleton
    fun provideAnalyticsManager(): AnalyticsManager {
        return AnalyticsManager()
                .withGoogleAnalytics(mApplication)
                .withFabricAnswers(mApplication)
    }

    @Provides
    @Singleton
    fun provideValidationUtils(@ApplicationContext context: Context): ValidationUtils {
        val res = context.resources
        return ValidationUtils.Builder(res.getString(R.string.error_empty_field))
                .setPasswordValidation(ValidationUtils.Builder.PasswordValidation(
                        ValidationUtils.ValidationRule.withDynamicResource(TasBoilerplateSettings.MIN_PASSWORD_LENGTH,
                                res.getString(R.string.error_short_pass)),
                        ValidationUtils.ValidationRule(TasBoilerplateSettings.PASSWORD_SHOULD_HAVE_NUMBERS_AND_LETTERS,
                                res.getString(R.string.error_pass_should_contains_letter_and_digits)),
                        ValidationUtils.ValidationRule(TasBoilerplateSettings.PASSWORD_SHOULD_HAVE_UPPERCASE_LETTERS,
                                res.getString(R.string.error_pass_should_contains_uppercase))))
                .setEmailValidation(
                        ValidationUtils.Builder.EmailValidation(res.getString(R.string.error_email_did_not_matches)))
                .build()
    }
}


