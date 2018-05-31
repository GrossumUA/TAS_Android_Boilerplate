package com.theappsolutions.boilerplate.util.other

import timber.log.Timber

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
    fun logThread(str: String) {
        var strTread = str
        val name = Thread.currentThread().name
        strTread += " [ $name ] "
        Timber.d(str)
    }
