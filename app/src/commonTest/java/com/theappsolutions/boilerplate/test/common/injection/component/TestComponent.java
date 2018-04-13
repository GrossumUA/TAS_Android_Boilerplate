package com.theappsolutions.boilerplate.test.common.injection.component;

import com.theappsolutions.boilerplate.injection.component.ApplicationComponent;
import com.theappsolutions.boilerplate.test.common.injection.module.ApplicationTestModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
