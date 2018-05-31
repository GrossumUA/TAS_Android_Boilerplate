package com.theappsolutions.boilerplate.util.data


import java.security.SecureRandom
import java.util.ArrayList
import java.util.HashSet
import java.util.Locale
import java.util.Random

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
class RandomUtils {
    private val random: Random
    private val symbols: CharArray
    private val buf: CharArray

    @JvmOverloads
    constructor(length: Int = 21, random: Random = SecureRandom(), symbols: String = alphanum) {
        if (length < 1) throw IllegalArgumentException()
        if (symbols.length < 2) throw IllegalArgumentException()
        this.random = random
        this.symbols = symbols.toCharArray()
        this.buf = CharArray(length)
    }


    /**
     * Generate a random string.
     */
    fun nextString(): String {
        buf.indices.forEach { idx -> buf[idx] = symbols[random.nextInt(symbols.size)] }
        return String(buf)
    }

    companion object {
        private val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        private val lower = upper.toLowerCase(Locale.ROOT)
        private val digits = "0123456789"
        private val alphanum = upper + lower + digits

        fun <T> generateRandList(src: List<T>, quantity: Int): List<T> {
            if (src.size <= quantity) {
                return src
            }
            val indexes = HashSet<Int>()
            val random = Random()
            while (indexes.size < quantity) {
                val newRand = random.nextInt(src.size)
                indexes.add(newRand)
            }
            val dest = ArrayList<T>()
            indexes.forEach { index -> dest.add(src[index]) }
            return dest
        }
    }
}
/**
 * Create an alphanumeric string generator.
 */
/**
 * Create an alphanumeric strings from a secure generator.
 */
/**
 * Create session identifiers.
 */
