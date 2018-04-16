package com.theappsolutions.boilerplate.util.data_utils;

import com.annimon.stream.Objects;
import com.annimon.stream.Stream;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class RandomUtils {

    private static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String lower = upper.toLowerCase(Locale.ROOT);
    private static final String digits = "0123456789";
    private static final String alphanum = upper + lower + digits;
    private final Random random;
    private final char[] symbols;
    private final char[] buf;

    public RandomUtils(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     */
    public RandomUtils(int length, Random random) {
        this(length, random, alphanum);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    public RandomUtils(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers.
     */
    public RandomUtils() {
        this(21);
    }

    /**
     * Generate a random string.
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public static <T> List<T> generateRandList(List<T> src, int quantity) {
        if (src.size() <= quantity) {
            return src;
        }
        Set<Integer> indexes = new HashSet<>();
        Random random = new Random();
        while (indexes.size() < quantity) {
            int newRand = random.nextInt(src.size());
            indexes.add(newRand);
        }
        List<T> dest = new ArrayList<>();
        Stream.of(indexes).forEach(index -> dest.add(src.get(index)));
        return dest;
    }
}
