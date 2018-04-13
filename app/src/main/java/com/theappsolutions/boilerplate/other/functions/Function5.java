package com.theappsolutions.boilerplate.other.functions;

/**
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 */
public interface Function5<T1, T2, T3, T4, T5, R> {
    R call(T1 param1, T2 param2, T3 param3, T4 param4, T5 param5);
}