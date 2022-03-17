
package com.vnpt.staffhddtpos58.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.vnpt.adapters.ListTermAdapter;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.staffhddtpos58.R;
import com.vnpt.view.SignaturexView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.vnpt.utils.Helper.hideSoftKeyboard;

public class CustomerSigningDialogFragment extends DialogFragment implements View.OnClickListener {
    public static String TAG = CustomerSigningDialogFragment.class.getName();
    static Context mContext;

    public OnEventControlListener getmListener() {
        return mListener;
    }

    public void setmListener(OnEventControlListener mListener) {
        this.mListener = mListener;
    }

    OnEventControlListener mListener;
    ListTermAdapter adapter;
    //    SignatureView signView;
    SignaturexView signView;
    Button btnCancel, btnOk, btnClear;

    File file;

    public ListTermAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ListTermAdapter adapter) {
        this.adapter = adapter;
    }

    public InvoiceCadmin getmHoaDon() {
        return mHoaDon;
    }

    public void setmHoaDon(InvoiceCadmin mHoaDon) {
        this.mHoaDon = mHoaDon;
    }

    InvoiceCadmin mHoaDon;
    String pathFileSign;
    String htmltSuccess;
    EditText txtName;
    EditText txtNameRoomNo;
    private AwesomeProgressDialog dg;

    //    public CustomerSigningDialogFragment(Context context, OnEventControlListener listener) {
//        mContext = context;
//        this.mListener = listener;
//    }
    public CustomerSigningDialogFragment() {

    }

    public static CustomerSigningDialogFragment newInstance(Context context, InvoiceCadmin hoaDon) {
        CustomerSigningDialogFragment frag = new CustomerSigningDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(Common.KEY_DATA_ITEM_INVOICE, hoaDon);
        mContext = context;
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_sinature_manual, null);
        setupUI(view);
        btnCancel = (Button) view.findViewById(R.id.action_cancel);
        btnOk = (Button) view.findViewById(R.id.action_ok);
        btnClear = (Button) view.findViewById(R.id.action_clear);
//        signView = (SignatureView) view.findViewById(R.id.viewCusSingature);
        signView = (SignaturexView) view.findViewById(R.id.signature_view);
        txtNameRoomNo = (EditText) view.findViewById(R.id.txtNameRoomNo);
        txtName = (EditText) view.findViewById(R.id.txtName);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle("Signature of customer");

//        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // on success
//            }
//        });
//        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
//            }
//
//        });


        return alertDialogBuilder.create();
    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            try{
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        hideSoftKeyboard(getActivity());
                        return false;
                    }
                });
            }catch (Exception ex){ex.printStackTrace();}

        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = super.onCreateView(inflater, container, savedInstanceState);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        setupUI(layout);
        if (isAdded()) {
            Bundle args = getArguments();
            if (args != null) {
                mHoaDon = (InvoiceCadmin) args.getSerializable(Common.KEY_DATA_ITEM_INVOICE);
            }
        }
//        btnCancel = (Button) layout.findViewById(R.id.action_cancel);
//        btnOk = (Button) layout.findViewById(R.id.action_ok);
//        btnClear = (Button) layout.findViewById(R.id.action_clear);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((WindowManager.LayoutParams) params).gravity = Gravity.RIGHT | Gravity.BOTTOM;
//        ((WindowManager.LayoutParams) params).x = 500;
//        ((WindowManager.LayoutParams) params).y = 800;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
//        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
//        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
//        getDialog().getWindow().setLayout(width, height);
        // Call super onResume after sizing
        super.onResume();
    }


    public String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.DEFAULT);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_cancel:
                // do something

                txtName.clearFocus();
                txtNameRoomNo.clearFocus();
                //hideSoftKeyboard(getActivity());
//                GeneralsUtils.turnOffKeyboard(getActivity());
                callhiddenkeyBoard();
                dismiss();
                mListener.onEvent(ActionEventConstant.ACTION_CHANGE_CANCEL_DIALOG_SIGNING, null, null);
                break;
            case R.id.action_clear:
                // do something
                callhiddenkeyBoard();
                signView.clearCanvas();
                break;
            case R.id.action_ok:
                // do something
                txtName.clearFocus();
                txtNameRoomNo.clearFocus();
                callhiddenkeyBoard();
                saveBitmapSigned();
                break;
            default:
                break;
        }
    }
    void callhiddenkeyBoard()
    {
        //View view = (View) txtName.getRootView().getWindowToken();
        //view.clearFocus();
        try{
            View windowToken = getDialog().getWindow().getDecorView().getRootView();
            InputMethodManager imm = (InputMethodManager)getDialog().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    void saveBitmapSigned() {
        String textRoomNo = txtNameRoomNo.getText().toString();
        if (textRoomNo == null) {
            textRoomNo = " ";
        }
        String nameCus = txtName.getText().toString();
        if (nameCus == null) {
            nameCus = " ";

        }
        Bitmap bitmap = signView.getSignatureBitmap();
        String strBase64 = "";
        try {
            strBase64 = convert(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (strBase64 == null || strBase64.equals("")) {
                    strBase64 = convert(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Bundle bundle  = new Bundle();
        bundle.putString(Common.BUNDLE_KEY_CUS_NAME, nameCus);
        bundle.putString(Common.BUNDLE_KEY_ROOM_NO, textRoomNo);
        bundle.putString(Common.BUNDLE_KEY_BASE64_IMAGE, strBase64);
        mListener.onEvent(ActionEventConstant.ACTION_CHANGE_OK_DIALOG_SIGNING, null, bundle);
        dismiss();
    }
}
