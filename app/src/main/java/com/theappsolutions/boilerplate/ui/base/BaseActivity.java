package com.theappsolutions.boilerplate.ui.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import com.annimon.stream.Optional;
import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate.TasBoilerplateApplication;
import com.theappsolutions.boilerplate.custom_views.CustomToolbar;
import com.theappsolutions.boilerplate.injection.component.ActivityComponent;
import com.theappsolutions.boilerplate.injection.component.ConfigPersistentComponent;
import com.theappsolutions.boilerplate.injection.component.DaggerConfigPersistentComponent;
import com.theappsolutions.boilerplate.injection.module.ActivityModule;
import com.theappsolutions.boilerplate.ui.settings.SettingsActivity;
import com.theappsolutions.boilerplate.util.ui_utils.DialogFactory;
import com.theappsolutions.boilerplate.util.ui_utils.ViewUtils;

import java.util.concurrent.atomic.AtomicLong;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent>
            sComponentsMap = new LongSparseArray<>();
    /**
     * Swipe top-bottom functionality
     */
    private static final int MIN_DISTANCE = 150;
    private static final int MAX_TIME_INTERVAL = 250;
    @Nullable
    @BindView(R.id.toolbar)
    CustomToolbar toolbar;
    private ActivityComponent activityComponent;
    private long activityId;
    private Optional<ProgressDialog> progressDlg = Optional.empty();
    private boolean isRestored;
    private float y1, y2;
    private long timeStart, timeFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isRestored = savedInstanceState != null;
        setupOrientation();
        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        activityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();

        ConfigPersistentComponent configPersistentComponent = sComponentsMap.get(activityId, null);

        if (configPersistentComponent == null) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", activityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(TasBoilerplateApplication.get(this).getComponent())
                    .build();
            sComponentsMap.put(activityId, configPersistentComponent);
        }
        activityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));

        super.onCreate(doNotRestoreFragments() ? null : savedInstanceState);
        ViewUtils.applyStatusBarWorkaroundForAndroid5(this);

        int contentViewRes = getContentViewRes();
        if (contentViewRes > 0) {
            setContentView(getContentViewRes());
        }
        ButterKnife.bind(this);

        setupToolbar();
    }

    @Nullable
    public Optional<CustomToolbar> getToolbar() {
        return Optional.ofNullable(toolbar);
    }

    private void setupOrientation() {
        if (isPortraitOnly()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    protected abstract int getContentViewRes();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, activityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", activityId);
            sComponentsMap.remove(activityId);
        }
        super.onDestroy();
    }

    public ActivityComponent activityComponent() {
        return activityComponent;
    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastMessage(int textRes) {
        Toast.makeText(this, getString(textRes), Toast.LENGTH_LONG).show();
    }

    public void animateActivityChangingToRight() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void animateActivityChangingToLeft() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    public void animateActivityChangingToTop() {
        overridePendingTransition(0, R.anim.top_out);
    }

    public void animateActivityChangingToBottom() {
        overridePendingTransition(0, R.anim.bottom_out);
    }

    public void setToolbarBackButtonVisibility(boolean visible) {
        if (toolbar != null) {
            if (visible) {
                toolbar.showBackButton();
            } else {
                toolbar.hideBackButton();
            }
        }
    }

    public void setToolbarSettingsButtonVisibility(boolean visible) {
        if (toolbar != null) {
            toolbar.setBtnSettingsIsVisible(visible);
        }
    }

    @Override
    public void goBack() {
        close();
        animateActivityChangingToLeft();
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void onBackPressed() {
        // Overridden by goBack() method for handling default backPressed behaviour
        // super.onBackPressed();
        goBack();
    }

    public void onBackPressedNative() {
        super.onBackPressed();
    }

    private void setupToolbar() {
        Optional.ofNullable(toolbar).ifPresent(bar -> {
            setSupportActionBar(bar);
            Optional.ofNullable(getSupportActionBar()).ifPresent(actionBar -> {
                        actionBar.setDisplayShowCustomEnabled(true);
                        CustomToolbar.Config config = getToolbarConfig();
                        if (config != null) {
                            if (!config.isWithoutToolbar()) {
                                toolbar.setListener(new CustomToolbar.OnToolbarItemsClickListener() {
                                    @Override
                                    public void onBackClick() {
                                        onBackPressed();
                                    }

                                    @Override
                                    public void onSettingsClick() {
                                        SettingsActivity.startWithAnimation(BaseActivity.this);
                                    }
                                });
                                toolbar.setBtnLeftIsVisible(config.isHasBackBtn());
                                toolbar.setBtnSettingsIsVisible(config.isHasSettingsBtn());
                            }
                        }
                    }
            );
        });
    }

    public void setToolbarTitle(String title) {
        Optional.ofNullable(toolbar).ifPresent(bar -> toolbar.setToolbarTitle(title));
    }

    protected abstract CustomToolbar.Config getToolbarConfig();

    @Override
    public void showProgress() {
        ProgressDialog dialog = DialogFactory.createProgressDialog(this, R.string.dialog_progress_msg);
        dialog.show();
        progressDlg = Optional.of(dialog);
    }

    @Override
    public void dismissProgress() {
        progressDlg.ifPresent(Dialog::dismiss);
    }

    public boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public boolean isPortraitOnly() {
        return getResources().getBoolean(R.bool.is_portrait_only);
    }

    public boolean isRestored() {
        return isRestored;
    }

    protected boolean doNotRestoreFragments() {
        return false;
    }

    public void lockOrientation() {
        WindowManager manager =
                (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            Display display = manager.getDefaultDisplay();
            int rotation = display.getRotation();
            int currentOrientation = getResources().getConfiguration().orientation;
            int orientation = 0;
            switch (currentOrientation) {
                case Configuration.ORIENTATION_LANDSCAPE:
                    if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
                        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    else
                        orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Configuration.ORIENTATION_PORTRAIT:
                    if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270)
                        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    else
                        orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
            setRequestedOrientation(orientation);
        }
    }

    public void unlockOrientation() {
        if (isPortraitOnly()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo
                    .SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }
}
