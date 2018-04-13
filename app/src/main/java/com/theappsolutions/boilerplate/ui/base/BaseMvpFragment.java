package com.theappsolutions.boilerplate.ui.base;

public abstract class BaseMvpFragment extends BaseFragment implements MvpScreen {

    @Override
    public void onDetach() {
        super.onDetach();
        getBasePresenter().detachView();
    }
}
