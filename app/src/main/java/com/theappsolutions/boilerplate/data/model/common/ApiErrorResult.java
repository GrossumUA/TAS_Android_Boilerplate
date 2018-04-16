package com.theappsolutions.boilerplate.data.model.common;

import static com.annimon.stream.Optional.ofNullable;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public class ApiErrorResult {

    public String title;
    public String message;
    public String detail;

    public String getMessage() {
        return ofNullable(message)
            .orElse(detail);
    }
}
