package com.theappsolutions.boilerplate.other.changelog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater

import com.theappsolutions.boilerplate.R

import it.gmariotti.changelibs.library.view.ChangeLogRecyclerView

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Fragment for ChangeLog view
 */
class ChangeLogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val chgList = layoutInflater.inflate(
                R.layout.changelog_fragment_dialog, null) as ChangeLogRecyclerView


        return AlertDialog.Builder(activity!!, R.style.AppCompatAlertDialogStyle)
                .setTitle(R.string.change_log_title)
                .setView(chgList)
                .setPositiveButton(R.string.change_log_confirm_button
                ) { dialog,
                    _ -> dialog.dismiss()
                }
                .create()
    }

    companion object {
        fun newInstance(): ChangeLogFragment {
            val args = Bundle()
            val fragment = ChangeLogFragment()
            fragment.arguments = args
            return fragment
        }

        fun show(fm: FragmentManager) {
            val fragment = ChangeLogFragment.newInstance()
            fragment.show(fm, null)
        }
    }
}
