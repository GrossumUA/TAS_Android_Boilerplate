package com.theappsolutions.boilerplate.test.common;

import android.content.Context;

import com.theappsolutions.boilerplate.TasBoilerplateApplication;
import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.test.common.injection.component.TestComponent;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


/**
 * Test rule that creates and sets a Dagger TestComponent into the application overriding the
 * existing application component.
 * Use this rule in your test case in order for the app to use mock dependencies.
 * It also exposes some of the dependencies so they can be easily accessed from the tests, e.g. to
 * stub mocks etc.
 */
public class TestComponentRule implements TestRule {

    private final Context mContext;
    private TestComponent mTestComponent;

    public TestComponentRule(Context context) {
        mContext = context;
        TasBoilerplateApplication application = TasBoilerplateApplication.get(context);
        // TODO: apply correct building of dagger test component.
       /* mTestComponent = DaggerTestComponent.builder()
                .applicationTestModule(new ApplicationTestModule(application))
                .build();*/
    }

    public Context getContext() {
        return mContext;
    }

    public DataManager getMockDataManager() {
        return mTestComponent.dataManager();
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                TasBoilerplateApplication application = TasBoilerplateApplication.get(mContext);
                application.setComponent(mTestComponent);
                base.evaluate();
                application.setComponent(null);
            }
        };
    }
}
