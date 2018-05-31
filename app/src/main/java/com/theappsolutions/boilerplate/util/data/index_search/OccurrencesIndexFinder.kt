package com.theappsolutions.boilerplate.util.data.index_search

import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Util for finding start/end indexes of phrase in text
 */
class OccurrencesIndexFinder(private val searchString: String) {

    fun findIndexesForKeyword(keyword: String): List<IndexWrapper> {
        val pattern = Pattern.compile(keyword)
        val matcher = pattern.matcher(searchString)
        val wrappers = ArrayList<IndexWrapper>()

        while (matcher.find()) {
            val end = matcher.end()
            val start = matcher.start()
            val wrapper = IndexWrapper(start, end)
            wrappers.add(wrapper)
        }
        return wrappers
    }
}
