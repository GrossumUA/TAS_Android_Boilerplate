package com.theappsolutions.boilerplate.other;

import android.content.Context;
import android.graphics.Typeface;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

public enum CustomFont {
    CLANT_OT_NARR_BOLD(0, "fonts/ClanOT_NarrBold.otf"),
    CLANT_OT_NARR_BOOK(1, "fonts/ClanOT_NarrBook.otf"),
    CLANT_OT_NARR_THIN(2, "fonts/ClanOT_NarrThin.otf"),
    CLANT_OT_NEWS(3, "fonts/ClanOT_News.otf");

    private final int index;
    private final String fileName;

    CustomFont(int index, String fileName) {
        this.fileName = fileName;
        this.index = index;
    }

    public static Optional<CustomFont> findByIndex(int index) {
        return Stream.of(CustomFont.values()).filter(font -> font.index == index).findFirst();
    }

    public Typeface asTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), fileName);
    }
}
