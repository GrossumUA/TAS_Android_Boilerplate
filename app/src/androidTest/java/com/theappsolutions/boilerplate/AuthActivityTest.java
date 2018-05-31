package com.theappsolutions.boilerplate;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.theappsolutions.boilerplate.other.changelog.ChangeLogManager;
import com.theappsolutions.boilerplate.ui.auth.AuthActivity;
import com.theappsolutions.boilerplate.util.TestUtils;
import com.theappsolutions.boilerplate.util.other.permissions_check.PermissionsCheckActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.theappsolutions.boilerplate.util.TestUtils.getStringByRes;

/**
 * Tests for {@link com.theappsolutions.boilerplate.ui.auth.AuthActivity}, the authorization screen.
 */
@RunWith(AndroidJUnit4.class)
public class AuthActivityTest {

    private final static String VALID_LOGIN = "test@test.com";
    private final static String VALID_PASS = "1Qqqqq";

    private final static String NOT_VALID_LOGIN = "test.com";
    private final static String NOT_VALID_SHORT_PASS = "1Qq";
    private final static String NOT_VALID_WITHOUT_DIGITS_PASS = "Qqqqqq";
    private final static String NOT_VALID_WITHOUT_UPPERCASE_PASS = "1qqqqq";

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<AuthActivity> mAuthActivityTestRule =
            new ActivityTestRule<AuthActivity>(AuthActivity.class, true, true) {
                @Override
                protected Intent getActivityIntent() {
                    Intent intent = AuthActivity.Companion.getStartIntent(
                            InstrumentationRegistry.getTargetContext());
                    intent.putExtra(PermissionsCheckActivity.Companion.getSUPPRESS_PERMISSIONS(), true);
                    return intent;
                }

                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                    ChangeLogManager.Companion.disable(InstrumentationRegistry.getTargetContext());
                }
            };

    @Test
    public void clickLoginButton_notOpenProjectsShowError() {
        // Click on the login button
        onView(withId(R.id.btn_login)).perform(click());

        // Check if the add task screen is not displayed
        onView(withId(R.id.rv_list)).check(doesNotExist());

        // Check if errors appears
        onView(withId(R.id.login_input_lay)).check(matches(TestUtils.hasTextInputLayoutErrorText(getStringByRes(R.string.error_empty_field))));
        onView(withId(R.id.pass_input_lay)).check(matches(TestUtils.hasTextInputLayoutErrorText(getStringByRes(R.string.error_empty_field))));
    }

    @Test
    public void clickLoginButton_checkLoginErrors() {
        // Click on the login button
        onView(withId(R.id.et_password)).perform(clearText());
        onView(withId(R.id.btn_login)).perform(click());
        // Check if error appears
        onView(withId(R.id.login_input_lay)).check(matches(TestUtils.hasTextInputLayoutErrorText(getStringByRes(R.string.error_empty_field))));

        // Type wrong email pattern credentials
        onView(withId(R.id.et_password)).perform(clearText());
        onView(withId(R.id.et_login)).perform(typeText(NOT_VALID_LOGIN));
        onView(withId(R.id.btn_login)).perform(click());
        // Check if error appears
        onView(withId(R.id.login_input_lay)).check(matches(TestUtils.hasTextInputLayoutErrorText(getStringByRes(R.string.error_email_did_not_matches))));

    }

    @Test
    public void clickLoginButton_checkPassErrors() {
        // Click on the login button
        onView(withId(R.id.et_password)).perform(clearText());
        onView(withId(R.id.btn_login)).perform(click());
        // Check if error appears
        onView(withId(R.id.pass_input_lay)).check(matches(TestUtils.hasTextInputLayoutErrorText(getStringByRes(R.string.error_empty_field))));

        // Type wrong short pass
        onView(withId(R.id.et_password)).perform(clearText());
        onView(withId(R.id.et_password)).perform(typeText(NOT_VALID_SHORT_PASS));
        onView(withId(R.id.btn_login)).perform(click());
        // Check if error appears
        onView(withId(R.id.pass_input_lay)).check(matches(TestUtils.hasTextInputLayoutErrorText(getStringByRes(R.string.error_short_pass, TasBoilerplateSettings.INSTANCE.getMIN_PASSWORD_LENGTH()))));

        // Type wrong without digits pass
        onView(withId(R.id.et_password)).perform(clearText());
        onView(withId(R.id.et_password)).perform(typeText(NOT_VALID_WITHOUT_DIGITS_PASS));
        onView(withId(R.id.btn_login)).perform(click());
        // Check if error appears
        onView(withId(R.id.pass_input_lay)).check(matches(TestUtils.hasTextInputLayoutErrorText(getStringByRes(R.string.error_pass_should_contains_letter_and_digits))));

        // Type wrong without uppercase pass
        onView(withId(R.id.et_password)).perform(clearText());
        onView(withId(R.id.et_password)).perform(typeText(NOT_VALID_WITHOUT_UPPERCASE_PASS));
        onView(withId(R.id.btn_login)).perform(click());
        // Check if error appears
        onView(withId(R.id.pass_input_lay)).check(matches(TestUtils.hasTextInputLayoutErrorText(getStringByRes(R.string.error_pass_should_contains_uppercase))));

    }

    @Test
    public void checkClearError() {
        // Click on the login button
        onView(withId(R.id.et_password)).perform(clearText());
        onView(withId(R.id.btn_login)).perform(click());
        // Check if error appears
        onView(withId(R.id.pass_input_lay)).check(matches(TestUtils.hasTextInputLayoutErrorText(getStringByRes(R.string.error_empty_field))));

        // Type wrong short pass
        onView(withId(R.id.et_password)).perform(clearText());
        onView(withId(R.id.pass_input_lay)).check(matches(TestUtils.hasTextInputLayoutErrorText(null)));
    }

    @Test
    public void clickLoginButton_openNotShowError() {
        // Type auth credentials
        onView(withId(R.id.et_login)).perform(typeText(VALID_LOGIN));
        onView(withId(R.id.et_password)).perform(typeText(VALID_PASS));

        // Click on the login button
        onView(withId(R.id.btn_login)).perform(click());

        // Check if the add task screen is displayed
        onView(withId(R.id.rv_list)).check(matches(isDisplayed()));
    }
}
