package com.theappsolutions.boilerplate._additional_useful_classes.custom_views.bottom_menu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.annimon.stream.Optional;
import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate._additional_useful_classes.ui.menu.MenuStates;
import com.theappsolutions.boilerplate.util.ui_utils.ViewUtils;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class BottomMenuLayout extends LinearLayout
        implements View.OnClickListener {

    public static final int ID_CLAUSE_1 = ViewUtils.generateViewId();
    public static final int ID_CLAUSE_2 = ViewUtils.generateViewId();

    @MenuStates
    private int menuState;
    private MenuButtonLayout btnClause1, btnClause2;
    private Optional<BottomMenuCallback> callbackOpt = Optional.empty();

    public BottomMenuLayout(Context context) {
        super(context);
        appendButtons();
    }

    public BottomMenuLayout(Context context,
                            @Nullable AttributeSet attrs) {
        super(context, attrs);
        appendButtons();
    }

    public BottomMenuLayout(Context context,
                            @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        appendButtons();
    }

    public void setListener(BottomMenuCallback callback) {
        this.callbackOpt = Optional.of(callback);
    }

    public void setInitialState() {
        processFirstItemClick();
    }

    private void appendButtons() {
        MenuItemHolder mhClause1 = new MenuItemHolder(
                R.drawable.ic_processor_menu, 0);

        MenuItemHolder mhClause2 = new MenuItemHolder(
                R.drawable.ic_settings_menu, 0);

        btnClause1 = new MenuButtonLayout(getContext(), mhClause1);
        btnClause1.setId(ID_CLAUSE_1);

        btnClause2 = new MenuButtonLayout(getContext(), mhClause2);
        btnClause2.setId(ID_CLAUSE_2);

        LayoutParams params = getLayoutParamsForButtons();

        btnClause1.setLayoutParams(params);
        btnClause2.setLayoutParams(params);

        addView(btnClause1);
        addView(btnClause2);

        btnClause1.setOnClickListener(this);
        btnClause2.setOnClickListener(this);
    }

    @NonNull
    private LayoutParams getLayoutParamsForButtons() {
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        return params;
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == ID_CLAUSE_1) {
            processFirstItemClick();
        } else if (vId == ID_CLAUSE_2) {
            renderClause2();
            callbackOpt.ifPresent(BottomMenuCallback::onClause2Press);
        }
    }

    public void processFirstItemClick() {
        renderClause1();
        callbackOpt.ifPresent(BottomMenuCallback::onClause1Press);
    }

    private void renderClause1() {
        applyEnablingForControls(true, false, MenuStates.M_ID_CLAUSE_1);
    }

    private void renderClause2() {
        applyEnablingForControls(false, true, MenuStates.M_ID_CLAUSE_2);
    }

    private void applyEnablingForControls(boolean isFirstEnabled,
                                          boolean isSecondEnabled, @MenuStates int state) {
        menuState = state;
        btnClause1.setEnabling(isFirstEnabled);
        btnClause2.setEnabling(isSecondEnabled);
    }

    @MenuStates
    public int getMenuState() {
        return menuState;
    }

    public void highlightCorrectItem(int prevMenuState) {
        switch (prevMenuState) {
            case MenuStates.M_ID_CLAUSE_1:
                renderClause1();
                break;
            case MenuStates.M_ID_CLAUSE_2:
                renderClause2();
                break;
        }
    }

    public interface BottomMenuCallback {
        void onClause1Press();

        void onClause2Press();
    }
}
