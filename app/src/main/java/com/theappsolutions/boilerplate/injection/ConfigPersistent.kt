package com.theappsolutions.boilerplate.injection

import com.theappsolutions.boilerplate.injection.component.ConfigPersistentComponent

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Scope

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 *
 * A scoping annotation to permit dependencies conform to the life of the
 * [ConfigPersistentComponent]
 */
@Scope
@Retention(value = RetentionPolicy.RUNTIME)
annotation class ConfigPersistent
