package com.theappsolutions.boilerplate.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.annimon.stream.Optional;
import com.theappsolutions.boilerplate.injection.component.FragmentComponent;
import com.theappsolutions.boilerplate.injection.module.FragmentModule;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 * <p>
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    private Optional<BaseActivity> baseActivity = Optional.empty();
    private Optional<FragmentComponent> fragmentComponent = Optional.empty();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            baseActivity = Optional.of((BaseActivity) context);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseActivity.ifPresent(activity ->
                fragmentComponent = Optional.of(activity.activityComponent().fragmentComponent(new FragmentModule(this))));
    }

    @Override
    public void showToastMessage(String text) {
        baseActivity.ifPresent(activity -> activity.showToastMessage(text));
    }

    @Override
    public void showToastMessage(int textRes) {
        baseActivity.ifPresent(activity -> activity.showToastMessage(textRes));
    }

    @Override
    public void goBack() {
        baseActivity.ifPresent(BaseActivity::goBack);
    }

    @Override
    public void close() {
        baseActivity.ifPresent(BaseActivity::close);
    }

    @Override
    public void showProgress() {
        baseActivity.ifPresent(BaseActivity::showProgress);
    }

    @Override
    public void dismissProgress() {
        baseActivity.ifPresent(BaseActivity::dismissProgress);
    }

    public void setToolbarTitle(String title) {
        baseActivity.ifPresent(activity -> activity.setToolbarTitle(title));
    }

    public Optional<FragmentComponent> getFragmentComponent() {
        return fragmentComponent;
    }


    public Optional<BaseActivity> getBaseActivity() {
        return baseActivity;
    }
}
