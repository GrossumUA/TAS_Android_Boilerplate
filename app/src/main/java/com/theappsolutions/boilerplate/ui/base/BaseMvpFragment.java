package com.theappsolutions.boilerplate.ui.base;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public abstract class BaseMvpFragment extends BaseFragment implements MvpScreen {

    @Override
    public void onDetach() {
        super.onDetach();
        getBasePresenter().detachView();
    }
}
