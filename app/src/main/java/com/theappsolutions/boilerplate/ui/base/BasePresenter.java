package com.theappsolutions.boilerplate.ui.base;

import com.annimon.stream.Optional;
import com.annimon.stream.function.Supplier;
import com.theappsolutions.boilerplate.BuildConfig;
import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate.other.exceptions.UserReadableException;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<T extends BaseView> implements Presenter<T> {

    private static final String LOG_TAG = BasePresenter.class.getSimpleName();

    private T mMvpView;
    private Consumer<Throwable> errorConsumer = createErrorConsumer(() -> mMvpView);
    private CompositeDisposable disposables = new CompositeDisposable();

    public static Consumer<Throwable> createErrorConsumer(Supplier<? extends BaseView> viewSupplier) {
        return throwable -> {
            BaseView view = viewSupplier.get();
            Timber.e(throwable);
            if (view != null) {
                view.dismissProgress();
                if (throwable instanceof UserReadableException) {
                    view.showToastMessage(throwable.getMessage());
                } else {
                    if (BuildConfig.DEBUG) {
                        view.showToastMessage("(only for DEBUG)\n" + throwable.getMessage());
                    } else {
                        view.showToastMessage(R.string.err_something_going_wrong);
                    }
                }
            }
        };
    }

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        disposables.dispose();
    }

    public Optional<T> getMvpView() {
        if (mMvpView == null) {
            new MvpViewNotAttachedException().printStackTrace();
        }
        return Optional.ofNullable(mMvpView);
    }

    /**
     * Use this method only for debug
     */
    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
    }

    private boolean isViewAttached() {
        return mMvpView != null;
    }

    protected Consumer<Throwable> getErrorConsumer() {
        return errorConsumer;
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }

    protected void addToDisposables(Disposable disposable) {
        disposables.add(disposable);
    }
}

