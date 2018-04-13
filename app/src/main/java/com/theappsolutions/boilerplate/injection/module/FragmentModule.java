package com.theappsolutions.boilerplate.injection.module;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;

import com.theappsolutions.boilerplate.injection.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    Fragment provideFragment() {
        return fragment;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return fragment.getContext();
    }

    @Provides
    Resources provideResources() {
        //noinspection ConstantConditions
        return fragment.getContext().getResources();
    }

}
