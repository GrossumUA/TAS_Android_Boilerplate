package com.theappsolutions.boilerplate._additional_useful_classes.custom_views.bottom_menu;

/**
 * Created by Dmytro Yakovlev.
 */

public class MenuItemHolder {

    private int iconResId;
    private int captionResId;

    public MenuItemHolder(int iconResId, int captionResId) {
        this.iconResId = iconResId;
        this.captionResId = captionResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getCaptionResId() {
        return captionResId;
    }

    public void setCaptionResId(int captionResId) {
        this.captionResId = captionResId;
    }
}
