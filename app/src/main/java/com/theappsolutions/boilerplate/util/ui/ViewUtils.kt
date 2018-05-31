package com.theappsolutions.boilerplate.util.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatDialog
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView

import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.additionalusefulclasses.custom_views.CustomTypeFaceSpan
import com.theappsolutions.boilerplate.other.CustomFont
import com.theappsolutions.boilerplate.util.data.index_search.IndexWrapper
import com.theappsolutions.boilerplate.util.data.index_search.OccurrencesIndexFinder
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Set of static methods with common view operations and calculations
 */
object ViewUtils {

    private val sNextGeneratedId = AtomicInteger(1)

    fun pxToDp(px: Float): Float {
        val densityDpi = Resources.getSystem().displayMetrics.densityDpi.toFloat()
        return px / (densityDpi / 160f)
    }

    fun dpToPx(dp: Int): Int {
        val density = Resources.getSystem().displayMetrics.density
        return Math.round(dp * density)
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }

    fun applyStatusBarWorkaroundForAndroid5(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.statusBarColor = activity.resources.getColor(R.color.status_bar_color_for_pre_23ver)
        }
    }

    fun getColor(context: Context, colorRes: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(colorRes, context.theme)
        } else {
            context.resources.getColor(colorRes)
        }
    }

    fun imageViewAnimatedChange(c: Context, v: ImageView, newImage: Bitmap) {
        val animOut = AnimationUtils.loadAnimation(c, android.R.anim.fade_out)
        val animIn = AnimationUtils.loadAnimation(c, android.R.anim.fade_in)
        animOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                v.setImageBitmap(newImage)
                animIn.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}

                    override fun onAnimationRepeat(animation: Animation) {}

                    override fun onAnimationEnd(animation: Animation) {}
                })
                v.startAnimation(animIn)
            }
        })
        v.startAnimation(animOut)
    }

    fun setBoldTextWithSpanStringAndColor(textView: TextView, text: String, spanText: String, color: Int) {
        val sb = SpannableStringBuilder(text)
        val start = text.indexOf(spanText)
        val end = start + spanText.length

        val typeFaceSpan = makeCustomTypeFaceSpan(textView.context)
        sb.setSpan(typeFaceSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.setSpan(ForegroundColorSpan(color), start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        textView.text = sb
    }

    private fun makeCustomTypeFaceSpan(
            context: Context): CustomTypeFaceSpan {
        val fontPath = CustomFont.CLANT_OT_NARR_BOLD
        return CustomTypeFaceSpan("my typeface span", fontPath.asTypeface(context))
    }

    fun setBoldTextWithSpanStringAndColorCaseIndependent(textView: TextView,
                                                         inputText: String, keyword: String, color: Int) {
        var keyword = keyword
        val c = textView.context
        val lowerCase = inputText.toLowerCase()
        keyword = keyword.toLowerCase()
        val sb = SpannableStringBuilder(inputText)

        val finder = OccurrencesIndexFinder(lowerCase)
        val indexes = finder.findIndexesForKeyword(keyword)
        for ((start, end) in indexes) {
            applySpansByPosition(sb, c, start, end, color)
        }
        textView.text = sb
    }

    private fun applySpansByPosition(sb: SpannableStringBuilder,
                                     context: Context, start: Int, end: Int, color: Int) {
        val typeFaceSpan = makeCustomTypeFaceSpan(context)
        sb.setSpan(typeFaceSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        sb.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    }

    fun enableDisableView(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            for (idx in 0 until view.childCount) {
                enableDisableView(view.getChildAt(idx), enabled)
            }
        }
    }

    @SuppressLint("NewApi")
    fun generateViewId(): Int {
        if (Build.VERSION.SDK_INT < 17) {
            while (true) {
                val result = sNextGeneratedId.get()
                // aapt-generated IDs have the high byte nonzero; clamp to
                // the range under that.
                var newValue = result + 1
                if (newValue > 0x00FFFFFF)
                    newValue = 1 // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result
                }
            }
        } else {
            return View.generateViewId()
        }

    }
}
