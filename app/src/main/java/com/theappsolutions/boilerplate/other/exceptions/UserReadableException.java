package com.theappsolutions.boilerplate.other.exceptions;
/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public class UserReadableException extends RuntimeException {
    public UserReadableException() {
    }

    public UserReadableException(String message) {
        super(message);
    }

    public UserReadableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserReadableException(Throwable cause) {
        super(cause);
    }
}
