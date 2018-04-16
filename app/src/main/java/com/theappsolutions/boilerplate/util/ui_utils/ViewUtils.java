package com.theappsolutions.boilerplate.util.ui_utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate._additional_useful_classes.custom_views.CustomTypeFaceSpan;
import com.theappsolutions.boilerplate.other.CustomFont;
import com.theappsolutions.boilerplate.util.data_utils.index_search.IndexWrapper;
import com.theappsolutions.boilerplate.util.data_utils.index_search.OccurrencesIndexFinder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Set of static methods with common view operations and calculations
 */
public final class ViewUtils {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void applyStatusBarWorkaroundForAndroid5(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.status_bar_color_for_pre_23ver));
        }
    }

    public static int getColor(Context context, int colorRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(colorRes, context.getTheme());
        } else {
            return context.getResources().getColor(colorRes);
        }
    }

    public static void imageViewAnimatedChange(Context c, final ImageView v, final Bitmap newImage) {
        final Animation animOut = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation animIn = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageBitmap(newImage);
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                v.startAnimation(animIn);
            }
        });
        v.startAnimation(animOut);
    }

    public static void setBoldTextWithSpanStringAndColor(TextView textView,
                                                         String text, String spanText, int color) {
        SpannableStringBuilder sb = new SpannableStringBuilder(text);
        int start = text.indexOf(spanText);
        int end = start + spanText.length();

        CustomTypeFaceSpan typeFaceSpan =
                makeCustomTypeFaceSpan(textView.getContext());
        sb.setSpan(typeFaceSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(color), start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(sb);
    }

    @NonNull
    private static CustomTypeFaceSpan makeCustomTypeFaceSpan(
            Context context) {
        CustomFont fontPath = CustomFont.CLANT_OT_NARR_BOLD;
        return new CustomTypeFaceSpan(
                "my typeface span", fontPath.asTypeface(context));
    }

    public static void setBoldTextWithSpanStringAndColorCaseIndependent(
            TextView textView, String inputText, String keyword, int color) {
        Context c = textView.getContext();
        String lowerCase = inputText.toLowerCase();
        keyword = keyword.toLowerCase();

        SpannableStringBuilder sb = new SpannableStringBuilder(inputText);

        OccurrencesIndexFinder finder = new OccurrencesIndexFinder(lowerCase);
        List<IndexWrapper> indexes = finder.findIndexesForKeyword(keyword);
        for (IndexWrapper index : indexes) {
            applySpansByPosition(sb, c, index.getStart(), index.getEnd(), color);
        }

        textView.setText(sb);
    }

    private static void applySpansByPosition(SpannableStringBuilder sb,
                                             Context context, int start, int end, int color) {
        CustomTypeFaceSpan typeFaceSpan =
                makeCustomTypeFaceSpan(context);
        sb.setSpan(typeFaceSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(color), start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    @SuppressLint("NewApi")
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < 17) {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to
                // the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }

    }
}
