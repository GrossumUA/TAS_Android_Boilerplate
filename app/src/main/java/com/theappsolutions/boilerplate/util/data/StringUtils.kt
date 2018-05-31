package com.theappsolutions.boilerplate.util.data

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */

/**
 * Make only first letter as capitalized and other letter in lower case.
 */
fun capitalizeOnlyFirstLetter(input: String): String {
    return Character.toUpperCase(input[0]) + input.substring(1).toLowerCase()
}

fun hasLetters(input: String): Boolean {
    return input.matches(".*[^a-z].*".toRegex())
}

fun hasNumbers(input: String): Boolean {
    return input.matches(".*\\d+.*".toRegex())
}

fun hasUppercase(input: String): Boolean {
    return input.matches(".*[A-Z].*".toRegex())
}

