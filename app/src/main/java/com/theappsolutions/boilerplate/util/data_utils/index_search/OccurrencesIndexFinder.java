package com.theappsolutions.boilerplate.util.data_utils.index_search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Util for finding start/end indexes of phrase in text
 */
public class OccurrencesIndexFinder {

    private String searchString;

    public OccurrencesIndexFinder(String searchString) {
        this.searchString = searchString;
    }

    public List<IndexWrapper> findIndexesForKeyword(String keyword) {
        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(searchString);

        List<IndexWrapper> wrappers = new ArrayList<>();

        while (matcher.find()) {
            int end = matcher.end();
            int start = matcher.start();
            IndexWrapper wrapper = new IndexWrapper(start, end);
            wrappers.add(wrapper);
        }
        return wrappers;
    }

}
