package com.theappsolutions.boilerplate.additionalusefulclasses.custom_views.bottom_menu

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout

import com.theappsolutions.boilerplate.R

import kotlinx.android.synthetic.main.menu_btn_layout.view.*

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
@SuppressLint("ViewConstructor")
class MenuButtonLayout(context: Context, private val menuItemHolder: MenuItemHolder) : LinearLayout(context) {

    private var isMenuEnabled = false
        get() = field

    init {
        initViews()
    }

    private fun initViews() {
        View.inflate(context, R.layout.menu_btn_layout, this)
        iv_progress?.setBackgroundResource(menuItemHolder.iconResId)
        tv_caption?.setText(menuItemHolder.captionResId)
        tv_caption?.isSelected = true
    }

    fun setEnabling(enabled: Boolean) {
        if (enabled) {
            enableButton()
        } else {
            disableButton()
        }
    }

    fun enableButton() {
        isMenuEnabled = true
        changeColorForViews(R.color.bottom_menu_text_color_active,
                R.dimen.bt_menu_text_size_active,
                R.dimen.bt_menu_icon_margin_top_active)
    }

    fun disableButton() {
        isMenuEnabled = false
        changeColorForViews(R.color.bottom_menu_text_color_inactive,
                R.dimen.bt_menu_text_size_inactive,
                R.dimen.bt_menu_icon_margin_top_inactive)
    }

    private fun changeColorForViews(colorResId: Int, textSizeDimen: Int,
                                    topMargin: Int) {
        val col = ContextCompat.getColor(context, colorResId)
        setUpImageView(topMargin, col)
        setUpTextView(textSizeDimen, col)
    }

    private fun setUpTextView(textSizeDimen: Int, col: Int) {
        tv_caption.setTextColor(col)
        tv_caption.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(textSizeDimen))
    }

    private fun setUpImageView(topMargin: Int, col: Int) {
        DrawableCompat.setTint(iv_progress.background, col)

        val pixels = resources.getDimensionPixelSize(
                R.dimen.bt_menu_icon_size)
        val params = RelativeLayout.LayoutParams(pixels, pixels)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL)

        val margin = resources.getDimensionPixelSize(topMargin)

        params.setMargins(0, margin, 0, 0)
        iv_progress.layoutParams = params
    }
}
