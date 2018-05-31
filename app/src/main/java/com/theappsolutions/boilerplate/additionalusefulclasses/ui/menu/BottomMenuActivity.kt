package com.theappsolutions.boilerplate.additionalusefulclasses.ui.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle

import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.additionalusefulclasses.custom_views.bottom_menu.BottomMenuLayout
import com.theappsolutions.boilerplate.customviews.CustomToolbar
import com.theappsolutions.boilerplate.data.DataManager
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager
import com.theappsolutions.boilerplate.ui.base.BaseActivity

import javax.inject.Inject

import butterknife.BindView
import kotlinx.android.synthetic.main.activity_bottom_menu_holder.*

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
class BottomMenuActivity : BaseActivity(), BottomMenuLayout.BottomMenuCallback {

    @Inject
    lateinit var analyticsManager: AnalyticsManager
    @Inject
    lateinit var dataManager: DataManager

    override fun getContentViewRes(): Int {
        return R.layout.activity_bottom_menu_holder
    }

    override fun getToolbarConfig(): CustomToolbar.Config {
        return CustomToolbar.Config.defaultConfig()
                .hasBackBtn(false)
                .hasSettingsBtn(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*TODO uncomment for BottomMenu functionality*/
        /*activityComponent().inject(this);*/

        bottom_menu.setListener(this)
        savedInstanceState?.let {
            if (isRestored && it.containsKey(KEY_MENU_STATE)) {
                val prevMenuState = it.getInt(KEY_MENU_STATE)
                bottom_menu.highlightCorrectItem(prevMenuState)
            } else {
                bottom_menu.setInitialState()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_MENU_STATE, bottom_menu.menuState)
    }

    override fun onClause1Press() {

    }

    override fun onClause2Press() {

    }

    /**
     * We don't need to close activity while back stack is not empty.
     */
    override fun goBack() {

    }

    companion object {
        val FRAGMENT_HOLDER = R.id.fl_holder
        val KEY_MENU_STATE = "m_state"

        fun getStartIntent(context: Context): Intent {
            return Intent(context, BottomMenuActivity::class.java)
        }

        fun startClearTask(activity: BaseActivity) {
            val intent = getStartIntent(activity)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
            activity.animateActivityChangingToRight()
        }

        fun start(activity: BaseActivity) {
            activity.startActivity(getStartIntent(activity))
            activity.animateActivityChangingToRight()
        }
    }
}
