package com.theappsolutions.boilerplate.customviews

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.os.Build
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.widget.TextView

import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.other.CustomFont

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
class CustomFontTextView : AppCompatTextView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupTypefaceToTextView(this, attrs)
    }

    companion object {
        fun setupTypefaceToTextView(tv: TextView, attrs: AttributeSet?) {
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                applyFixForPre21ver(tv)
            }
            if (!tv.isInEditMode && attrs != null) {
                val typedArray = tv.context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView)
                val fontIndex = typedArray.getInt(R.styleable.CustomFontTextView_customFontName, -1)
                if (fontIndex < 0) {
                    typedArray.recycle()
                    throw IllegalArgumentException("You must provide attribute \"customFontName\" for your CustomFontTextView")
                } else {
                    when {
                        CustomFont.findByIndex(fontIndex)!=null -> {
                            val customTypeface = CustomFont.findByIndex(fontIndex)?.asTypeface(tv.context)
                            tv.typeface = customTypeface
                            typedArray.recycle()
                        }
                        else -> {
                            val customTypeface = CustomFont.CLANT_OT_NARR_BOOK.asTypeface(tv.context)
                            tv.typeface = customTypeface
                            typedArray.recycle()
                        }
                    }
                }
            }
        }

        private fun setFont(tv: TextView, customFont: CustomFont) {
            tv.typeface = customFont.asTypeface(tv.context)
        }

        private fun applyFixForPre21ver(tv: TextView) {
            var paddingBottom = tv.paddingBottom
            paddingBottom = paddingBottom - tv.lineSpacingExtra.toInt()
            tv.setPadding(tv.paddingLeft, tv.paddingTop, tv.paddingRight, paddingBottom)
        }
    }
}