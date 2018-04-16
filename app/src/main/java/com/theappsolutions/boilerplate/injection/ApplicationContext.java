package com.theappsolutions.boilerplate.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationContext {
}
