package com.theappsolutions.boilerplate;

import android.support.annotation.NonNull;

import com.theappsolutions.boilerplate.ui.base.BasePresenter;
import com.theappsolutions.boilerplate.ui.base.BaseView;

import org.junit.After;
import org.junit.Before;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.times;

public abstract class CommonPresenterTest<K extends BaseView, T extends BasePresenter<K>> {

    @NonNull
    protected abstract T getPresenter();

    @NonNull
    protected abstract K getView();


    @Before
    public void setUp() {
        getPresenter().attachView(getView());
    }

    @After
    public void tearDown() {
        getPresenter().detachView();
    }

    VerificationMode once() {
        return times(1);
    }
}
