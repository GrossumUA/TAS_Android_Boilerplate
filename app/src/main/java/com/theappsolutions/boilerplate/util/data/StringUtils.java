package com.theappsolutions.boilerplate.util.data;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class StringUtils {

    /**
     * Make only first letter as capitalized and other letter in lower case.
     */
    public static String capitalizeOnlyFirstLetter(String input) {
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }

    public static boolean hasLetters(String input) {
        return input.matches(".*[^a-z].*");
    }

    public static boolean hasNumbers(String input) {
        return input.matches(".*\\d+.*");
    }

    public static boolean hasUppercase(String input) {
        return input.matches(".*[A-Z].*");
    }
}
