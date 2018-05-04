package com.theappsolutions.boilerplate.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate.other.CustomFont;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public class CustomFontTextView extends AppCompatTextView {

    public CustomFontTextView(Context context) {
        super(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupTypefaceToTextView(this, attrs);
    }

    public static void setupTypefaceToTextView(TextView tv, AttributeSet attrs) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            applyFixForPre21ver(tv);
        }
        if (!tv.isInEditMode() && attrs != null) {
            TypedArray typedArray = tv.getContext().obtainStyledAttributes(
                attrs,
                R.styleable.CustomFontTextView);
            final int fontIndex = typedArray.getInt(R.styleable.CustomFontTextView_customFontName, -1);
            if (fontIndex < 0) {
                typedArray.recycle();
                throw new IllegalArgumentException("You must provide attribute \"customFontName\" for your CustomFontTextView");
            } else {
                final Typeface customTypeface = CustomFont.findByIndex(fontIndex).orElse(CustomFont.CLANT_OT_NARR_BOOK)
                    .asTypeface(tv.getContext());
                tv.setTypeface(customTypeface);
                typedArray.recycle();
            }
        }
    }

    private static void setFont(TextView tv, CustomFont customFont) {
        tv.setTypeface(customFont.asTypeface(tv.getContext()));
    }

    private static void applyFixForPre21ver(TextView tv) {
        int paddingBottom = tv.getPaddingBottom();
        paddingBottom = paddingBottom - (int) tv.getLineSpacingExtra();
        tv.setPadding(tv.getPaddingLeft(), tv.getPaddingTop(), tv.getPaddingRight(), paddingBottom);
    }
}