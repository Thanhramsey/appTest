
package com.vnpt.staffhddtpos58.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.vnpt.common.ActionEventConstant;
import com.vnpt.dto.InvoiceUpdatePaymentedRequest;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.staffhddtpos58.R;

import java.util.ArrayList;

public class ReturnReceiptInvoiceDialog extends DialogFragment {
    public static String TAG = ReturnReceiptInvoiceDialog.class.getName();
    EditText edtReason;
    Context mContext;
    OnEventControlListener mListener;
    ArrayList<InvoiceUpdatePaymentedRequest> mArrData;
    public ReturnReceiptInvoiceDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public ReturnReceiptInvoiceDialog(Context context, OnEventControlListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public ReturnReceiptInvoiceDialog(Context context, OnEventControlListener listener, ArrayList<InvoiceUpdatePaymentedRequest> arrData) {
        this.mContext = context;
        this.mArrData = arrData;
        this.mListener = listener;
    }


    public static ReturnReceiptInvoiceDialog newInstance(String title) {
        ReturnReceiptInvoiceDialog frag = new ReturnReceiptInvoiceDialog();
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
        View view = inflater.inflate(R.layout.dialog_return_receipt_invoice, null);
        edtReason = (EditText) view.findViewById(R.id.edt_reason);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setIcon(R.drawable.logo_vnpt);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle("Khách hàng lấy lại tiền");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strReason = edtReason.getText().toString();
                for (int i = 0; i < mArrData.size(); i++) {
                    InvoiceUpdatePaymentedRequest itemRequest = mArrData.get(i);
                    itemRequest.setReason(strReason);
                }
                mListener.onEvent(ActionEventConstant.ACTION_CHANGE_STATUS_RETURN_INVOICE, null, mArrData);
                dialog.dismiss();
            }
        });
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
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        getDialog().setCanceledOnTouchOutside(false);
        // Call super onResume after sizing
        super.onResume();
    }
}
