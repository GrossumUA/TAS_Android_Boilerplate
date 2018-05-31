package com.theappsolutions.boilerplate.other

import android.content.Context
import android.graphics.Typeface

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Provides interface for font files
 */
enum class CustomFont private constructor(private val index: Int, private val fileName: String) {
    CLANT_OT_NARR_BOLD(0, "fonts/ClanOT_NarrBold.otf"),
    CLANT_OT_NARR_BOOK(1, "fonts/ClanOT_NarrBook.otf"),
    CLANT_OT_NARR_THIN(2, "fonts/ClanOT_NarrThin.otf"),
    CLANT_OT_NEWS(3, "fonts/ClanOT_News.otf");

    fun asTypeface(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, fileName)
    }

    companion object {
        fun findByIndex(index: Int): CustomFont? {
            return CustomFont.values().filter { font ->
                font.index == index
            }.firstOrNull()
        }
    }
}
