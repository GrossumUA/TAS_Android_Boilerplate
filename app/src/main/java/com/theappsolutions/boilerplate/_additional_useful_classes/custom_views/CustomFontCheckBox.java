package com.theappsolutions.boilerplate._additional_useful_classes.custom_views;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.theappsolutions.boilerplate.custom_views.CustomFontTextView;

public class CustomFontCheckBox extends AppCompatCheckBox {

    public CustomFontCheckBox(Context context) {
        super(context);
    }

    public CustomFontCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontTextView.setupTypefaceToTextView(this, attrs);
    }
}