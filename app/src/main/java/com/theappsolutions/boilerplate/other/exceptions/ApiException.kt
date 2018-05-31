package com.theappsolutions.boilerplate.other.exceptions

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
class ApiException : UserReadableException {

    var code: Int = 0
        private set

    constructor(code: Int, message: String)
            : super(message) {
        this.code = code
    }

    constructor(code: Int, message: String, cause: Throwable)
            : super(message, cause) {
        this.code = code
    }

    constructor(code: Int, cause: Throwable)
            : super(cause) {
        this.code = code
    }
}
