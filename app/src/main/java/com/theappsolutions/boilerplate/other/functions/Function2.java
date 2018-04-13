package com.theappsolutions.boilerplate.other.functions;

/**
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 */
public interface Function2<T1, T2, R> {
    R call(T1 param1, T2 param2);
}
