package com.theappsolutions.boilerplate.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.util.ui.ViewUtils

import com.theappsolutions.boilerplate.R.id.*
import kotlinx.android.synthetic.main.layout_toolbar_items.view.*


/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
class CustomToolbar : Toolbar {
    private var listener: OnToolbarItemsClickListener? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics))
            layoutParams = params
        }
        setBackgroundColor(ViewUtils.getColor(context, R.color.primary))

        @SuppressLint("InflateParams")
        val cntMain = LayoutInflater.from(context).inflate(R.layout.layout_toolbar_items, null) as ViewGroup
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        cntMain.layoutParams = params
        addView(cntMain)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar)
        val withBackBtn = typedArray.getBoolean(R.styleable.CustomToolbar_withBackBtn, false)
        val titleText = typedArray.getString(R.styleable.CustomToolbar_titleText)

        setBtnLeftIsVisible(withBackBtn)
        if (titleText != null) {
            tv_title.text = titleText
        }

        typedArray.recycle()

        btn_settings.setOnClickListener { onBtnSettingsClick() }
        btn_left.setOnClickListener { onBtnLeftClick() }
    }

    fun onBtnLeftClick() {
        listener?.onBackClick()
    }

    fun onBtnSettingsClick() {
        listener?.onSettingsClick()
    }

    fun setBtnLeftIsVisible(isVisible: Boolean) {
        btn_left?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setToolbarTitle(title: String) {
        tv_title.text = title
    }

    fun setListener(listener: OnToolbarItemsClickListener) {
        this.listener = listener
    }

    fun hideBackButton() {
        btn_left?.visibility = View.INVISIBLE
    }

    fun showBackButton() {
        btn_left?.visibility = View.VISIBLE
    }

    fun setBtnSettingsIsVisible(isVisible: Boolean) {
        btn_settings?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    interface OnToolbarItemsClickListener {
        fun onBackClick()
        fun onSettingsClick()
    }

    class Config {

        var isWithoutToolbar = false
            private set
        var isHasBackBtn = true
            private set
        var isHasSettingsBtn = false
            private set

        fun hasBackBtn(hasBackBtn: Boolean): Config {
            this.isHasBackBtn = hasBackBtn
            return this
        }

        fun hasSettingsBtn(hasSettingsBtn: Boolean): Config {
            this.isHasSettingsBtn = hasSettingsBtn
            return this
        }

        companion object {

            fun defaultConfig(): Config {
                return Config()
            }

            fun withoutToolbar(): Config {
                val config = Config()
                config.isWithoutToolbar = true
                return config
            }
        }
    }
}
