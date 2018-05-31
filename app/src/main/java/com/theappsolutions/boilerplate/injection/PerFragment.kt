package com.theappsolutions.boilerplate.injection

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Scope

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 *
 * A scoping annotation to permit dependencies conform to the life of the
 * [PerFragment]
 */
@Scope
@Retention(value = RetentionPolicy.RUNTIME)
annotation class PerFragment
