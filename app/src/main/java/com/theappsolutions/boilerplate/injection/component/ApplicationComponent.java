package com.theappsolutions.boilerplate.injection.component;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.data.PreferencesManager;
import com.theappsolutions.boilerplate.injection.ApplicationContext;
import com.theappsolutions.boilerplate.injection.module.ApplicationModule;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager;
import com.theappsolutions.boilerplate.other.analytics.ErrorLogger;
import com.theappsolutions.boilerplate.util.data_utils.ValidationUtils;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    PreferencesManager preferencesHelper();

    Gson gson();

    DataManager dataManager();

    ErrorLogger getErrorLogger();

    AnalyticsManager getAnalyticsManager();

    ValidationUtils getValidationUtils();
}
