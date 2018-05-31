package com.theappsolutions.boilerplate.ui.base

import com.annimon.stream.Optional
import com.annimon.stream.function.Consumer
import com.annimon.stream.function.Supplier
import com.theappsolutions.boilerplate.BuildConfig
import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.other.exceptions.UserReadableException
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
open class BasePresenter<T : BaseView> : Presenter<T> {

    private var mMvpView: T? = null
    protected val errorConsumer: Consumer<Throwable> = createErrorConsumer(Supplier<BaseView> { mMvpView })

    private val disposables = CompositeDisposable()

    fun getMvpView(): Optional<T> {
        if (mMvpView == null) {
            MvpViewNotAttachedException().printStackTrace()
        }
        return Optional.ofNullable(mMvpView)
    }

    private fun isViewAttached(): Boolean = mMvpView != null

    override fun attachView(mvpView: T) {
        mMvpView = mvpView
    }

    override fun detachView() {
        mMvpView = null
        disposables.dispose()
    }

    /**
     * Use this method only for debug
     */
    fun checkViewAttached() {
        if (!isViewAttached()) {
            throw MvpViewNotAttachedException()
        }
    }

    class MvpViewNotAttachedException : RuntimeException("Please call Presenter.attachView(MvpView) before" + " requesting data to the Presenter")

    protected fun addToDisposables(disposable: Disposable) {
        disposables.add(disposable)
    }

    companion object {
        private val LOG_TAG = BasePresenter::class.java.simpleName

        fun createErrorConsumer(viewSupplier: Supplier<out BaseView>): Consumer<Throwable> {
            return Consumer { throwable ->
                val view = viewSupplier.get()
                Timber.e(throwable)
                if (view != null) {
                    view.dismissProgress()
                    if (throwable is UserReadableException) {
                        throwable.message?.let { view.showToastMessage(it) }
                    } else {
                        if (BuildConfig.DEBUG) {
                            view.showToastMessage("(only for DEBUG)\n${throwable.message}")
                        } else {
                            view.showToastMessage(R.string.err_something_going_wrong)
                        }
                    }
                }
            }
        }
    }
}