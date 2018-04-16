package com.theappsolutions.boilerplate.custom_views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate.util.ui_utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public class CustomToolbar extends Toolbar {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.btn_left)
    View btnLeft;

    @BindView(R.id.btn_settings)
    View btnSettings;

    private OnToolbarItemsClickListener listener;

    public CustomToolbar(Context context) {
        super(context);
        init(null);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics()));
            setLayoutParams(params);
        }
        setBackgroundColor(ViewUtils.getColor(getContext(), R.color.primary));

        @SuppressLint("InflateParams")
        ViewGroup cntMain = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_toolbar_items, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cntMain.setLayoutParams(params);
        addView(cntMain);

        ButterKnife.bind(this);

        TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.CustomToolbar);

        boolean withBackBtn = typedArray.getBoolean(R.styleable.CustomToolbar_withBackBtn, false);
        String titleText = typedArray.getString(R.styleable.CustomToolbar_titleText);

        setBtnLeftIsVisible(withBackBtn);
        if (titleText != null) {
            tvTitle.setText(titleText);
        }

        typedArray.recycle();
    }

    @OnClick(R.id.btn_left)
    public void onBtnLeftClick() {
        if (listener != null) {
            listener.onBackClick();
        }
    }

    @OnClick(R.id.btn_settings)
    public void onBtnSettingsClick() {
        if (listener != null) {
            listener.onSettingsClick();
        }
    }

    public void setBtnLeftIsVisible(boolean isVisible) {
        btnLeft.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setToolbarTitle(String title) {
        tvTitle.setText(title);
    }

    public void setListener(OnToolbarItemsClickListener listener) {
        this.listener = listener;
    }

    public void hideBackButton() {
        btnLeft.setVisibility(INVISIBLE);
    }

    public void showBackButton() {
        btnLeft.setVisibility(VISIBLE);
    }

    public void setBtnSettingsIsVisible(boolean isVisible) {
        btnSettings.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public View getBtnSettings() {
        return btnSettings;
    }

    public interface OnToolbarItemsClickListener {
        void onBackClick();

        void onSettingsClick();
    }

    public static class Config {

        private boolean withoutToolbar = false;
        private boolean hasBackBtn = true;
        private boolean hasSettingsBtn = false;

        public Config() {
        }

        public static Config defaultConfig() {
            return new Config();
        }

        public static Config withoutToolbar() {
            Config config = new Config();
            config.withoutToolbar = true;
            return config;
        }

        public Config hasBackBtn(boolean hasBackBtn) {
            this.hasBackBtn = hasBackBtn;
            return this;
        }

        public Config hasSettingsBtn(boolean hasSettingsBtn) {
            this.hasSettingsBtn = hasSettingsBtn;
            return this;
        }

        public boolean isHasBackBtn() {
            return hasBackBtn;
        }

        public boolean isHasSettingsBtn() {
            return hasSettingsBtn;
        }

        public boolean isWithoutToolbar() {
            return withoutToolbar;
        }
    }
}
