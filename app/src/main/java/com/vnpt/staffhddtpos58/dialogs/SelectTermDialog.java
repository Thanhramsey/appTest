
package com.vnpt.staffhddtpos58.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vnpt.adapters.ListTermAdapter;
import com.vnpt.common.Common;
import com.vnpt.dto.Term;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.staffhddtpos58.R;
import com.vnpt.utils.StoreSharePreferences;

import java.util.ArrayList;

public class SelectTermDialog extends DialogFragment {
    public static String TAG = SelectTermDialog.class.getName();
    ListView lvData;
    Context mContext;
    OnEventControlListener mListener;
    ListTermAdapter adapter;
    ArrayList<Term> mArrTerm;
    public SelectTermDialog(ArrayList<Term> arrTerm) {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
        this.mArrTerm = arrTerm;
    }

    public SelectTermDialog(Context context, OnEventControlListener listener) {
        mContext = context;
        this.mListener = listener;
    }


    public static SelectTermDialog newInstance(ArrayList<Term> arrTerm) {
        SelectTermDialog frag = new SelectTermDialog(arrTerm);
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_contents_authorize_sign_for_app, null);
        lvData = (ListView) view.findViewById(R.id.lvData);
        adapter = new ListTermAdapter(mContext, mArrTerm);
        adapter.notifyDataSetChanged();
        lvData.setAdapter(adapter);
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Term item = adapter.getItem(position);
                StoreSharePreferences.getInstance(
                        getActivity()).saveIntPreferences(
                        Common.REFF_KEY_DATA_TERM, item.getId_term());
                dismiss();
            }
        });
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setIcon(R.drawable.logo_vnpt);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle("Các phương thức xác nhận qua ứng dụng người dùng");
        return alertDialogBuilder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        return inflater.inflate(R.layout.dialog_contents_authorize_sign_for_app, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        // Call super onResume after sizing
        super.onResume();
    }
}
