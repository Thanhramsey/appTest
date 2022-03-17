package com.vnpt.staffhddtpos58.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.vnpt.staffhddtpos58.R;

/**
 * Created by apple on 6/28/16.
 */

public class SingleChoiceDialog extends DialogFragment implements DialogInterface.OnClickListener {
    public static final String TAG = "SelectorDialog";

    static CharSequence[] mResourceArray;
    static int mSelectedIndex;
    static int mDialogView;
    static OnDialogSelectorListener mDialogSelectorCallback;
    static String mTilte;
    public interface OnDialogSelectorListener {
        void onSelectedOption(int typeSelected, int dialogView);
    }

    public static SingleChoiceDialog newInstance(CharSequence[] res, int selected,String tilte, int dialogView) {
        final SingleChoiceDialog dialog  = new SingleChoiceDialog();
        mResourceArray = res;
        mSelectedIndex = selected;
        mTilte = tilte;
        mDialogView = dialogView;
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mDialogSelectorCallback = (OnDialogSelectorListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDialogSelectorListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        builder.setTitle(mTilte);
        builder.setSingleChoiceItems(mResourceArray, mSelectedIndex, this);
        builder.setPositiveButton(R.string.OK, this);
        builder.setNegativeButton(R.string.cancel, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case Dialog.BUTTON_NEGATIVE:
                dialog.cancel();
                break;

            case Dialog.BUTTON_POSITIVE:
                dialog.dismiss();
                // message selected value to registered calbacks
                mDialogSelectorCallback.onSelectedOption(mSelectedIndex,mDialogView);
                break;

            default: // choice selected click
                mSelectedIndex = which;
                break;
        }

    }
}