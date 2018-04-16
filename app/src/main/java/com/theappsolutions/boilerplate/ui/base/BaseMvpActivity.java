package com.theappsolutions.boilerplate.ui.base;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public abstract class BaseMvpActivity extends BaseActivity implements MvpScreen {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getBasePresenter().detachView();
    }
}
