package com.theappsolutions.boilerplate.util.data_utils;

import android.text.TextUtils;
import android.util.Patterns;

import com.annimon.stream.Stream;
import com.theappsolutions.boilerplate.other.functions.Action1;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Util that provides validation of different fields.
 * Can be used as instance with predefined rules and messages.
 */
public class ValidationUtils {

    private Builder builder;

    public ValidationUtils(Builder builder) {
        this.builder = builder;
    }

    public ValidationResult validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return ValidationResult.notValid(builder.emptyFieldMessage);
        }
        if (builder.passwordValidation == null) {
            return ValidationResult.valid();
        } else {
            ValidationRule<Integer> minLength = builder.passwordValidation.minLength;
            if (minLength != null && password.length() < minLength.param) {
                return ValidationResult.notValid(minLength.message);
            }
            ValidationRule<Boolean> mustHaveNumbersAndLetters = builder.passwordValidation.mustHaveNumbersAndLetters;
            if (mustHaveNumbersAndLetters != null &&
                    mustHaveNumbersAndLetters.param &&
                    (!StringUtils.hasLetters(password) || !StringUtils.hasNumbers(password))) {
                return ValidationResult.notValid(mustHaveNumbersAndLetters.message);
            }

            ValidationRule<Boolean> mustHaveUppercase = builder.passwordValidation.mustHaveUppercase;
            if (mustHaveUppercase != null &&
                    mustHaveUppercase.param &&
                    !StringUtils.hasUppercase(password)) {
                return ValidationResult.notValid(mustHaveUppercase.message);
            }
            return ValidationResult.valid();
        }
    }

    public ValidationResult validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return ValidationResult.notValid(builder.emptyFieldMessage);
        }
        if (builder.emailValidation == null) {
            return ValidationResult.valid();
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return ValidationResult.notValid(builder.emailValidation.message);
            } else {
                return ValidationResult.valid();
            }
        }
    }

    public static class Builder {

        private String emptyFieldMessage;
        private PasswordValidation passwordValidation;
        private EmailValidation emailValidation;

        public Builder(String emptyFieldMessage) {
            this.emptyFieldMessage = emptyFieldMessage;
        }

        public Builder setPasswordValidation(PasswordValidation passwordValidation) {
            this.passwordValidation = passwordValidation;
            return this;
        }

        public Builder setEmailValidation(EmailValidation emailValidation) {
            this.emailValidation = emailValidation;
            return this;
        }

        public ValidationUtils build() {
            return new ValidationUtils(this);
        }

        public static class PasswordValidation {
            private ValidationRule<Integer> minLength;
            private ValidationRule<Boolean> mustHaveNumbersAndLetters;
            private ValidationRule<Boolean> mustHaveUppercase;

            public PasswordValidation(ValidationRule<Integer> minLength) {
                this.minLength = minLength;
            }

            public PasswordValidation(ValidationRule<Integer> minLength,
                                      ValidationRule<Boolean> mustHaveNumbersAndLetters,
                                      ValidationRule<Boolean> mustHaveUppercase) {
                this.minLength = minLength;
                this.mustHaveNumbersAndLetters = mustHaveNumbersAndLetters;
                this.mustHaveUppercase = mustHaveUppercase;
            }
        }

        public static class EmailValidation {
            private String message;

            public EmailValidation(String message) {
                this.message = message;
            }
        }
    }

    public static class ValidationRule<T extends Object> {
        private T param;
        private String message;

        public ValidationRule(T param, String message) {
            this.param = param;
            this.message = message;
        }

        public static <T> ValidationRule<T> withDynamicResource(T param, String message) {
            return new ValidationRule<>(param, String.format(message, param));
        }
    }

    public static class ValidationResult {

        private boolean success;
        private String message;

        private ValidationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult notValid(String message) {
            return new ValidationResult(false, message);
        }

        public boolean isFailure() {
            return !success;
        }

        public String getMessage() {
            return message;
        }

        public static boolean isAllSuccess(List<ValidationResult> results) {
            return Stream.of(results)
                    .filter(ValidationUtils.ValidationResult::isFailure)
                    .count() == 0;
        }

        public ValidationResult doIfFailureWithMessage(Action1<String> action) {
            if (!success) {
                action.call(message);
            }
            return this;
        }

        public void storeResults(@Nonnull List<ValidationResult> results) {
            results.add(this);
        }
    }
}
