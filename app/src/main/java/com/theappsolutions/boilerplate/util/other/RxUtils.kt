package com.theappsolutions.boilerplate.util.other

import io.reactivex.disposables.Disposable

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */

fun dispose(disposable: Disposable?) {
    disposable?.let {
        if (!it.isDisposed) {
            it.dispose()
        }
    }
}

