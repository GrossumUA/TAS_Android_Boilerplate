package com.theappsolutions.boilerplate._additional_useful_classes.custom_views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class SquareByWidthImageView extends AppCompatImageView {


    public SquareByWidthImageView(Context context) {
        super(context);
    }

    public SquareByWidthImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareByWidthImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        setMeasuredDimension(w, w);
    }
}