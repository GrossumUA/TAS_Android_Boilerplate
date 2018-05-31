package com.theappsolutions.boilerplate.other.functions

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
interface Function3<T1, T2, T3, R> {
    fun call(param1: T1, param2: T2, param3: T3): R
}