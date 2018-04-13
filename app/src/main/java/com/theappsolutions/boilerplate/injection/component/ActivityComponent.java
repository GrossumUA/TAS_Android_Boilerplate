package com.theappsolutions.boilerplate.injection.component;

import com.theappsolutions.boilerplate.injection.PerActivity;
import com.theappsolutions.boilerplate.injection.module.ActivityModule;
import com.theappsolutions.boilerplate.injection.module.FragmentModule;
import com.theappsolutions.boilerplate.ui.auth.AuthActivity;
import com.theappsolutions.boilerplate.ui.projects.ProjectListActivity;
import com.theappsolutions.boilerplate.ui.settings.SettingsActivity;
import com.theappsolutions.boilerplate.ui.splash.SplashActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity activity);

    void inject(AuthActivity activity);

    void inject(ProjectListActivity activity);

    void inject(SettingsActivity activity);

    FragmentComponent fragmentComponent(FragmentModule fragmentModule);

    /*TODO uncomment for BottomMenu functionality*/
    /*void inject(BottomMenuActivity activity);*/
}
