
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

import com.vnpt.adapters.ListAuthorizeForAppAdapter;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.dto.ItemAuthorizeForApp;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.staffhddtpos58.R;

public class AuthenticationForAppDialog extends DialogFragment {
    public static String TAG = AuthenticationForAppDialog.class.getName();
    ListView lvData;
    Context mContext;
    OnEventControlListener mListener;
    ListAuthorizeForAppAdapter adapter;

    public AuthenticationForAppDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public AuthenticationForAppDialog(Context context, OnEventControlListener listener) {
        mContext = context;
        this.mListener = listener;
    }


    public static AuthenticationForAppDialog newInstance(String title) {
        AuthenticationForAppDialog frag = new AuthenticationForAppDialog();
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
        adapter = new ListAuthorizeForAppAdapter(mContext);
        ItemAuthorizeForApp item = new ItemAuthorizeForApp();
        item.setIdItem(1);
        item.setNameAuthorize("Chữ ký");
        ItemAuthorizeForApp item1 = new ItemAuthorizeForApp();
        item1.setIdItem(2);
        item1.setNameAuthorize("Ghi âm");
        ItemAuthorizeForApp item2 = new ItemAuthorizeForApp();
        item2.setIdItem(3);
        item2.setNameAuthorize("Chụp hình");
        adapter.addItem(item);
        adapter.addItem(item1);
        adapter.addItem(item2);
        adapter.notifyDataSetChanged();
        lvData.setAdapter(adapter);
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemAuthorizeForApp item = adapter.getItem(position);
                mListener.onEvent(ActionEventConstant.ACTION_SELECTED_AUTHORIZE_FOR_APP_INVOICE, null, item);
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
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        getDialog().setCanceledOnTouchOutside(false);
        // Call super onResume after sizing
        super.onResume();
    }
}
