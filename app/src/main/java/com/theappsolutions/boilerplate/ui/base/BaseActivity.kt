package com.theappsolutions.boilerplate.ui.base

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.util.LongSparseArray
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Display
import android.view.Surface
import android.view.WindowManager
import android.widget.Toast

import com.annimon.stream.Optional
import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.TasBoilerplateApplication
import com.theappsolutions.boilerplate.customviews.CustomToolbar
import com.theappsolutions.boilerplate.injection.component.ActivityComponent
import com.theappsolutions.boilerplate.injection.component.ConfigPersistentComponent
import com.theappsolutions.boilerplate.injection.component.DaggerConfigPersistentComponent
import com.theappsolutions.boilerplate.injection.module.ActivityModule
import com.theappsolutions.boilerplate.ui.settings.SettingsActivity
import com.theappsolutions.boilerplate.util.ui.DialogFactory
import com.theappsolutions.boilerplate.util.ui.ViewUtils

import java.util.concurrent.atomic.AtomicLong

import butterknife.BindView
import butterknife.ButterKnife
import com.annimon.stream.function.Consumer
import kotlinx.android.synthetic.main.toolbar.*
import timber.log.Timber

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 *
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
abstract class BaseActivity : AppCompatActivity(), BaseView {
    private var activityComponent: ActivityComponent? = null
    private var activityId: Long = 0
    private var progressDlg = Optional.empty<ProgressDialog>()
    private var sComponentsMap = LongSparseArray<ConfigPersistentComponent>()

    var isRestored: Boolean = false
        private set
    private val y1: Float = 0.toFloat()
    private val y2: Float = 0.toFloat()
    private val timeStart: Long = 0
    private val timeFinish: Long = 0

    protected abstract fun getContentViewRes(): Int

    protected abstract fun getToolbarConfig(): CustomToolbar.Config?

    fun isPortrait(): Boolean = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    fun isPortraitOnly(): Boolean = resources.getBoolean(R.bool.is_portrait_only)

    override fun onCreate(savedInstanceState: Bundle?) {
        isRestored = savedInstanceState != null
        setupOrientation()
        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        activityId = savedInstanceState?.getLong(KEY_ACTIVITY_ID) ?: NEXT_ID.getAndIncrement()

        var configPersistentComponent = sComponentsMap.get(activityId, null);
        if (configPersistentComponent == null) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", activityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(TasBoilerplateApplication.get(this).getComponent())
                    .build();
            sComponentsMap.put(activityId, configPersistentComponent);
        }
        activityComponent = configPersistentComponent.activityComponent(ActivityModule(this))

        super.onCreate(if (doNotRestoreFragments()) null else savedInstanceState)
        ViewUtils.applyStatusBarWorkaroundForAndroid5(this)

        val contentViewRes = getContentViewRes()
        if (contentViewRes > 0) {
            setContentView(contentViewRes)
        }
        setupToolbar()
    }

    private fun setupOrientation() {
        if (isPortraitOnly()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_ACTIVITY_ID, activityId)
    }

    override fun onDestroy() {
        if (!isChangingConfigurations) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", activityId)
            sComponentsMap.remove(activityId)
        }
        super.onDestroy()
    }

    fun activityComponent(): ActivityComponent? {
        return activityComponent
    }

    override fun showToastMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun showToastMessage(textRes: Int) {
        Toast.makeText(this, getString(textRes), Toast.LENGTH_LONG).show()
    }

    fun animateActivityChangingToRight() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    fun animateActivityChangingToLeft() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out)
    }

    fun animateActivityChangingToTop() {
        overridePendingTransition(0, R.anim.top_out)
    }

    fun animateActivityChangingToBottom() {
        overridePendingTransition(0, R.anim.bottom_out)
    }

    fun setToolbarBackButtonVisibility(visible: Boolean) {
        if (toolbar != null) {
            if (visible) {
                toolbar.showBackButton()
            } else {
                toolbar.hideBackButton()
            }
        }
    }

    fun setToolbarSettingsButtonVisibility(visible: Boolean) {
        if (toolbar != null) {
            toolbar.setBtnSettingsIsVisible(visible)
        }
    }

    override fun goBack() {
        close()
        animateActivityChangingToLeft()
    }

    override fun close() {
        finish()
    }

    override fun onBackPressed() {
        // Overridden by goBack() method for handling default backPressed behaviour
        // super.onBackPressed();
        goBack()
    }

    fun onBackPressedNative() {
        super.onBackPressed()
    }

    private fun setupToolbar() {
        Optional.ofNullable<CustomToolbar>(toolbar).ifPresent { bar ->
            setSupportActionBar(bar)
            Optional.ofNullable<ActionBar>(supportActionBar).ifPresent { actionBar ->
                actionBar.setDisplayShowCustomEnabled(true)
                val config = getToolbarConfig()
                if (config != null) {
                    if (!config.isWithoutToolbar) {
                        toolbar?.setListener(object : CustomToolbar.OnToolbarItemsClickListener {
                            override fun onBackClick() {
                                onBackPressed()
                            }

                            override fun onSettingsClick() {
                                SettingsActivity.startWithAnimation(this@BaseActivity)
                            }
                        })
                        toolbar?.setBtnLeftIsVisible(config.isHasBackBtn)
                        toolbar?.setBtnSettingsIsVisible(config.isHasSettingsBtn)
                    }
                }
            }
        }
    }

    fun setToolbarTitle(title: String) {
        Optional.ofNullable<CustomToolbar>(toolbar).ifPresent { bar -> toolbar?.setToolbarTitle(title) }
    }

    override fun showProgress() {
        val dialog = DialogFactory.createProgressDialog(this, R.string.dialog_progress_msg)
        dialog.show()
        progressDlg = Optional.of(dialog)
    }

    override fun dismissProgress() {
        progressDlg.ifPresent(Consumer<ProgressDialog> { it.dismiss() })
    }

    protected fun doNotRestoreFragments(): Boolean {
        return false
    }

    fun lockOrientation() {
        val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (manager != null) {
            val display = manager.defaultDisplay
            val rotation = display.rotation
            val currentOrientation = resources.configuration.orientation
            var orientation = 0
            when (currentOrientation) {
                Configuration.ORIENTATION_LANDSCAPE -> if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                else
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                Configuration.ORIENTATION_PORTRAIT -> if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270)
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                else
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            }
            requestedOrientation = orientation
        }
    }

    fun unlockOrientation() {
        if (isPortraitOnly()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            requestedOrientation = ActivityInfo
                    .SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    companion object {

        private val KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID"
        private val NEXT_ID = AtomicLong(0)
        /**
         * Swipe top-bottom functionality
         */
        private val MIN_DISTANCE = 150
        private val MAX_TIME_INTERVAL = 250
    }
}
