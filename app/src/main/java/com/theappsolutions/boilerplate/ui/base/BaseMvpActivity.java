package com.theappsolutions.boilerplate.ui.base;

public abstract class BaseMvpActivity extends BaseActivity implements MvpScreen {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getBasePresenter().detachView();
    }
}
