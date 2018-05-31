package com.theappsolutions.boilerplate.additionalusefulclasses.custom_views.bottom_menu

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.annimon.stream.Optional
import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.additionalusefulclasses.ui.menu.MenuStates
import com.theappsolutions.boilerplate.util.ui.ViewUtils

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
class BottomMenuLayout : LinearLayout, View.OnClickListener {

    @MenuStates
    @get:MenuStates
    var menuState: Int = 0
        private set

    private var btnClause1: com.theappsolutions.boilerplate.additionalusefulclasses.custom_views.bottom_menu.MenuButtonLayout? = null
    private var btnClause2: com.theappsolutions.boilerplate.additionalusefulclasses.custom_views.bottom_menu.MenuButtonLayout? = null
    private var callbackOpt: BottomMenuCallback? = null

    private fun getLayoutParamsForButtons(): LinearLayout.LayoutParams {
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.weight = 1f
        return params
    }

    constructor(context: Context) : super(context) {
        appendButtons()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        appendButtons()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        appendButtons()
    }

    fun setListener(callback: BottomMenuCallback) {
        this.callbackOpt = callback
    }

    fun setInitialState() {
        processFirstItemClick()
    }

    private fun appendButtons() {
        val mhClause1 = com.theappsolutions.boilerplate.additionalusefulclasses.custom_views.bottom_menu.MenuItemHolder(
                R.drawable.ic_processor_menu, 0)

        val mhClause2 = com.theappsolutions.boilerplate.additionalusefulclasses.custom_views.bottom_menu.MenuItemHolder(
                R.drawable.ic_settings_menu, 0)

        btnClause1 = com.theappsolutions.boilerplate.additionalusefulclasses.custom_views.bottom_menu.MenuButtonLayout(context, mhClause1)
        btnClause1?.id = ID_CLAUSE_1

        btnClause2 = com.theappsolutions.boilerplate.additionalusefulclasses.custom_views.bottom_menu.MenuButtonLayout(context, mhClause2)
        btnClause2?.id = ID_CLAUSE_2

        val params = getLayoutParamsForButtons()

        btnClause1?.layoutParams = params
        btnClause2?.layoutParams = params

        addView(btnClause1)
        addView(btnClause2)

        btnClause1?.setOnClickListener(this)
        btnClause2?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val vId = v.id
        if (vId == ID_CLAUSE_1) {
            processFirstItemClick()
        } else if (vId == ID_CLAUSE_2) {
            renderClause2()
            callbackOpt.let { it?.onClause2Press() }
        }
    }

    fun processFirstItemClick() {
        renderClause1()
        callbackOpt.let { it?.onClause1Press() }
    }

    private fun renderClause1() {
        applyEnablingForControls(true, false, MenuStates.M_ID_CLAUSE_1)
    }

    private fun renderClause2() {
        applyEnablingForControls(false, true, MenuStates.M_ID_CLAUSE_2)
    }

    private fun applyEnablingForControls(isFirstEnabled: Boolean,
                                         isSecondEnabled: Boolean, @MenuStates state: Int) {
        menuState = state
        btnClause1?.setEnabling(isFirstEnabled)
        btnClause2?.setEnabling(isSecondEnabled)
    }

    fun highlightCorrectItem(prevMenuState: Int) {
        when (prevMenuState) {
            MenuStates.M_ID_CLAUSE_1 -> renderClause1()
            MenuStates.M_ID_CLAUSE_2 -> renderClause2()
        }
    }

    interface BottomMenuCallback {
        fun onClause1Press()
        fun onClause2Press()
    }

    companion object {
        val ID_CLAUSE_1 = ViewUtils.generateViewId()
        val ID_CLAUSE_2 = ViewUtils.generateViewId()
    }
}
