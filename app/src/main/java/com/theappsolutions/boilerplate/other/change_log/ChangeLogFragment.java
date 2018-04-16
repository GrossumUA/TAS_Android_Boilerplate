package com.theappsolutions.boilerplate.other.change_log;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.theappsolutions.boilerplate.R;

import it.gmariotti.changelibs.library.view.ChangeLogRecyclerView;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Fragment for ChangeLog view
 */
public class ChangeLogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity()
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ChangeLogRecyclerView chgList = (ChangeLogRecyclerView) layoutInflater.inflate(
            R.layout.changelog_fragment_dialog, null);

        return new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
            .setTitle(R.string.change_log_title)
            .setView(chgList)
            .setPositiveButton(R.string.change_log_confirm_button,
                (dialog, whichButton) -> dialog.dismiss()
            )
            .create();
    }

    public static ChangeLogFragment newInstance() {
        Bundle args = new Bundle();
        ChangeLogFragment fragment = new ChangeLogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static void show(FragmentManager fm) {
        ChangeLogFragment fragment = ChangeLogFragment.newInstance();
        fragment.show(fm, null);
    }
}
