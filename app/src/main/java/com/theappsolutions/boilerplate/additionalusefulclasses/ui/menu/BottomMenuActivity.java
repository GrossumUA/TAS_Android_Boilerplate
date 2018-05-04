package com.theappsolutions.boilerplate.additionalusefulclasses.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate.additionalusefulclasses.custom_views.bottom_menu.BottomMenuLayout;
import com.theappsolutions.boilerplate.customviews.CustomToolbar;
import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager;
import com.theappsolutions.boilerplate.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class BottomMenuActivity extends BaseActivity
        implements BottomMenuLayout.BottomMenuCallback {

    public static final int FRAGMENT_HOLDER = R.id.fl_holder;
    public static final String KEY_MENU_STATE = "m_state";

    @BindView(R.id.bottom_menu)
    BottomMenuLayout bottomMenu;

    @Inject
    AnalyticsManager analyticsManager;

    @Inject
    DataManager dataManager;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, BottomMenuActivity.class);
    }

    public static void startClearTask(BaseActivity activity) {
        Intent intent = getStartIntent(activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.animateActivityChangingToRight();
    }

    public static void start(BaseActivity activity) {
        activity.startActivity(getStartIntent(activity));
        activity.animateActivityChangingToRight();
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_bottom_menu_holder;
    }

    @Override
    protected CustomToolbar.Config getToolbarConfig() {
        return CustomToolbar.Config.defaultConfig()
                .hasBackBtn(false)
                .hasSettingsBtn(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*TODO uncomment for BottomMenu functionality*/
        /*activityComponent().inject(this);*/

        bottomMenu.setListener(this);
        if (isRestored() && savedInstanceState.containsKey(KEY_MENU_STATE)) {
            int prevMenuState = savedInstanceState.getInt(KEY_MENU_STATE);
            bottomMenu.highlightCorrectItem(prevMenuState);
        } else {
            bottomMenu.setInitialState();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_MENU_STATE, bottomMenu.getMenuState());
    }

    @Override
    public void onClause1Press() {

    }

    @Override
    public void onClause2Press() {

    }

    /**
     * We don't need to close activity while back stack is not empty.
     */
    @Override
    public void goBack() {

    }
}
