package com.theappsolutions.boilerplate.other.functions

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
interface Function2<T1, T2, R> {
    fun call(param1: T1, param2: T2): R
}
