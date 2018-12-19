package com.theappsolutions.boilerplate.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Optional;
import com.theappsolutions.boilerplate.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public abstract class BaseDialog extends DialogFragment {

    private Unbinder unbinder;
    private Optional<DismissListener> dismissListenerOpt = Optional.empty();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(getViewResId(), container);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            DismissListener listener = (DismissListener) context;
            dismissListenerOpt = Optional.ofNullable(listener);
        } catch (Exception e) {
            Timber.i(e);
        }
    }

    public abstract int getViewResId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface DismissListener {
        void onDialogDismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dismissListenerOpt.ifPresent(DismissListener::onDialogDismiss);
    }
}
