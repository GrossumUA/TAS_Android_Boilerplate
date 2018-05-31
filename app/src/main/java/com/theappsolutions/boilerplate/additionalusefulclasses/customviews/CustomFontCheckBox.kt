package com.theappsolutions.boilerplate.additionalusefulclasses.custom_views

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.util.AttributeSet

import com.theappsolutions.boilerplate.customviews.CustomFontTextView

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
class CustomFontCheckBox : AppCompatCheckBox {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        CustomFontTextView.setupTypefaceToTextView(this, attrs)
    }
}