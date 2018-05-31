package com.theappsolutions.boilerplate.customviews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

import com.annimon.stream.Optional

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
class RecyclerViewWithEmptyMode : RecyclerView {

    private var onUpdateListener: OnUpdateListener? = null
    private lateinit var emptyView: View

    internal fun getObserver() = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            checkIfEmpty()
            onUpdateListener?.onUpdate()
        }
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    private fun checkIfEmpty() {
        if (emptyView != null) {
            emptyView.visibility = if (adapter == null || adapter.itemCount > 0) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(getObserver())
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(getObserver())
    }

    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
        checkIfEmpty()
    }

    interface OnUpdateListener {
        fun onUpdate()
    }

    fun setOnUpdateListener(onUpdateListener: OnUpdateListener) {
        this.onUpdateListener = onUpdateListener
    }
}
