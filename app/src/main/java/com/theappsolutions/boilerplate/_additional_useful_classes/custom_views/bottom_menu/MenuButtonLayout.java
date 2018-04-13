package com.theappsolutions.boilerplate._additional_useful_classes.custom_views.bottom_menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.theappsolutions.boilerplate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dmytro Yakovlev.
 */

@SuppressLint("ViewConstructor")
public class MenuButtonLayout extends LinearLayout {

    @BindView(R.id.iv_progress)
    protected ImageView ivIcon;
    @BindView(R.id.tv_caption)
    protected TextView tvCaption;
    private MenuItemHolder menuItemHolder;
    private boolean isEnabled = false;

    public MenuButtonLayout(Context context, MenuItemHolder menuItemHolder) {
        super(context);
        this.menuItemHolder = menuItemHolder;
        initViews();
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    private void initViews() {
        inflate(getContext(), R.layout.menu_btn_layout, this);
        ButterKnife.bind(this);

        ivIcon.setBackgroundResource(menuItemHolder.getIconResId());
        tvCaption.setText(menuItemHolder.getCaptionResId());
        tvCaption.setSelected(true);
    }

    public void setEnabling(boolean enabled) {
        if (enabled) {
            enableButton();
        } else {
            disableButton();
        }
    }

    public void enableButton() {
        isEnabled = true;
        changeColorForViews(R.color.bottom_menu_text_color_active,
            R.dimen.bt_menu_text_size_active,
            R.dimen.bt_menu_icon_margin_top_active);
    }

    public void disableButton() {
        isEnabled = false;
        changeColorForViews(R.color.bottom_menu_text_color_inactive,
            R.dimen.bt_menu_text_size_inactive,
            R.dimen.bt_menu_icon_margin_top_inactive);
    }

    private void changeColorForViews(int colorResId, int textSizeDimen,
                                     int topMargin) {
        int col = ContextCompat.getColor(getContext(), colorResId);
        setUpImageView(topMargin, col);
        setUpTextView(textSizeDimen, col);
    }

    private void setUpTextView(int textSizeDimen, int col) {
        tvCaption.setTextColor(col);
        tvCaption.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimension(textSizeDimen));
    }

    private void setUpImageView(int topMargin, int col) {
        DrawableCompat.setTint(ivIcon.getBackground(), col);

        int pixels = getResources().getDimensionPixelSize(
            R.dimen.bt_menu_icon_size);
        RelativeLayout.LayoutParams params =
            new RelativeLayout.LayoutParams(pixels, pixels);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        int margin = getResources().getDimensionPixelSize(topMargin);

        params.setMargins(0, margin, 0, 0);
        ivIcon.setLayoutParams(params);
    }

}
