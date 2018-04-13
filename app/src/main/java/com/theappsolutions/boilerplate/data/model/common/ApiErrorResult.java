package com.theappsolutions.boilerplate.data.model.common;

import static com.annimon.stream.Optional.ofNullable;

/**
 * @author Yevhen Nechyporenko <nechiporenko.evgeniy@gmail.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
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
