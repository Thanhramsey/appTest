package com.vnpt.staffhddtpos58;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.common.ModelEvent;
import com.vnpt.controller.UserController;
import com.vnpt.dto.FeeInvoice;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.dto.InvoiceHDDTDetails;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.staffhddtpos58.fragment.BaseFragment;
import com.vnpt.utils.ToastMessageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddUpdateInforCustomerFragment extends BaseFragment implements View.OnClickListener, OnEventControlListener {
    public static String TAG = AddUpdateInforCustomerFragment.class.getName();
    TextInputLayout inpLayoutCusName, inpLayoutCusAddress, inpLayoutCusPhone, inpLayoutCusCode;
    TextInputEditText errEdtCusName,errEdtCusAddress, errEdtCusPhone, errEdtCusCode;
    Spinner spnKindOfPayment, spnKindOfFee;
    RecyclerView recyclerViewProd;
    OnEventControlListener mListener;
    InvoiceCadmin mHoaDon;
    String kindOfPayment;
    FeeInvoice feeInvoice;
    public AddUpdateInforCustomerFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FinishTransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add_update_infor_invoice, container, false);
        if (isAdded()) {
            Bundle args = getArguments();
            if (args != null) {
//                mHoaDon = (InvoiceCadmin) args.getSerializable(Common.KEY_DATA_ITEM_INVOICE);
            }
            init(layout);
            setValueForMembers();
            setEventForMembers();
        }
        return layout;
    }

    /**
     * khởi tao cac thanh phan giao dien
     *
     * @param layout
     * @author: truonglt2
     * @return: MainActivityFragment
     * @throws:
     */
    @Override
    protected void init(View layout) {
        //label
//        txtCodeCus = (TextView) layout.findViewById(R.id.txt_code_cus);
        //button
        inpLayoutCusName =  (TextInputLayout) layout.findViewById(R.id.cusNameInputLayout);
        errEdtCusName =  (TextInputEditText) layout.findViewById(R.id.errorCusNameEditText);

        inpLayoutCusAddress = (TextInputLayout) layout.findViewById(R.id.cusAddressInputLayout);
        errEdtCusAddress = (TextInputEditText) layout.findViewById(R.id.errorCusAddressEditText);

        inpLayoutCusPhone = (TextInputLayout) layout.findViewById(R.id.cusPhoneInputLayout);
        errEdtCusPhone = (TextInputEditText) layout.findViewById(R.id.errorCusPhoneEditText);

        inpLayoutCusCode = (TextInputLayout) layout.findViewById(R.id.cusCodeInputLayout);
        errEdtCusCode = (TextInputEditText) layout.findViewById(R.id.errorCusCodeEditText);

        spnKindOfPayment = (Spinner) layout.findViewById(R.id.spn_kind_of_payment);
        spnKindOfFee = (Spinner) layout.findViewById(R.id.spn_kind_of_fee);
        recyclerViewProd = (RecyclerView) layout.findViewById(R.id.recyclerViewProd);


    }


    /**
     * set gia tri cho cac thanh phan
     *
     * @author: truonglt2
     * @return: MainActivityFragment
     * @throws:
     */
    @Override
    protected void setValueForMembers() {
        getListOrgInvoice();
        getListFeeInvoice();
    }

    protected void getListOrgInvoice() {
        /*ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_GET_DATA_ORG_INVOICE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, mHoaDon);
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);*/
        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.kind_of_payment_tm));
        list.add(getString(R.string.kind_of_payment_ck));
        list.add(getString(R.string.kind_of_payment_tmck));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnKindOfPayment.setAdapter(adapter);
        spnKindOfPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                kindOfPayment = (String) spnKindOfPayment.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        kindOfPayment = (String) spnKindOfPayment.getSelectedItem();
    }
    protected void getListFeeInvoice() {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_GET_DATA_FEE_INVOICE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, mHoaDon);
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }
    /**
     * set su kien cho cac thanh phan
     *
     * @author: truonglt2
     * @return: MainActivityFragment
     * @throws:
     */
    @Override
    protected void setEventForMembers() {
        // TODO Auto-generated method stub
        errEdtCusName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > inpLayoutCusName.getCounterMaxLength())
                    inpLayoutCusName.setError("Max character length is " + inpLayoutCusName.getCounterMaxLength());
                else
                    inpLayoutCusName.setError(null);

            }
        });
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
        switch (eventType) {
            case ActionEventConstant.ACTION_CHECK_ALL_DEBIT_INVOICE: {
                break;
            }
        }
    }

    protected void saveDataDetailInvoice() {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_SAVE_NEW_DATA_INVOICE;
        e.sender = this;
        String cusName = errEdtCusName.getText().toString();
        String cusAddress = errEdtCusAddress.getText().toString();
        String cusPhone = errEdtCusPhone.getText().toString();
        String cusCode = errEdtCusCode.getText().toString();
        kindOfPayment = (String) spnKindOfPayment.getSelectedItem();
        feeInvoice = (FeeInvoice) spnKindOfFee.getSelectedItem();
        Bundle bundle = new Bundle();
        bundle.putString(Common.BUNDLE_KEY_CUS_NAME, cusName);
        bundle.putString(Common.BUNDLE_KEY_CUS_ADD, cusAddress);
        bundle.putString(Common.BUNDLE_KEY_CUS_PHONE, cusPhone);
        bundle.putString(Common.BUNDLE_KEY_ID_CUSTOMER, cusCode);
        bundle.putDouble(Common.BUNDLE_KEY_FEE_VAL, feeInvoice.getFee());
        bundle.putString(Common.BUNDLE_KEY_FEE_NAME, feeInvoice.getName());
        bundle.putString(Common.BUNDLE_KEY_ORG_ID, kindOfPayment);
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }
    protected void getDataDetailInvoice(String tokenInvoice) {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_GET_DATA_DETAIL_INVOICE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putString(Common.BUNDLE_KEY_FKEY_INVOICE, tokenInvoice);
        bundle.putString(Common.BUNDLE_KEY_TOKEN_INVOICE, tokenInvoice);
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }

    /**
     * xu ly su kien cua model
     *
     * @param modelEvent
     * @author: truonglt2
     * @return: BaseFragment
     * @throws:
     */
    @SuppressWarnings("unchecked")
    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
        ActionEvent act = modelEvent.getActionEvent();
        switch (act.action) {
            case ActionEventConstant.ACTION_GET_DATA_DETAIL_INVOICE: {
                InvoiceHDDTDetails invoiceHDDTDetails = (InvoiceHDDTDetails) modelEvent
                        .getModelData();

                break;
            }
            case ActionEventConstant.ACTION_SAVE_NEW_DATA_INVOICE: {
//                ToastMessageUtil.showMessage(getActivity(), "", "Phát hành biên lai thành công", "Xác nhận");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setIcon(R.drawable.logo_vnpt);
                alertDialogBuilder.setMessage("Đã phát hành biên lai thành công, bạn có muốn trở về danh sách biên lai?");
                alertDialogBuilder.setTitle("Thông báo");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialogBuilder.create().show();
                break;
            }
            case ActionEventConstant.ACTION_GET_DATA_ORG_INVOICE: {
                break;
            }
            case ActionEventConstant.ACTION_GET_DATA_FEE_INVOICE: {
                List<FeeInvoice> listFee = (List<FeeInvoice>) modelEvent
                        .getModelData();
                ArrayAdapter<FeeInvoice> adapter = new ArrayAdapter<FeeInvoice>(getActivity(), android.R.layout.simple_spinner_item, listFee);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnKindOfFee.setAdapter(adapter);
                spnKindOfFee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        feeInvoice = (FeeInvoice) spnKindOfFee.getSelectedItem();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                feeInvoice = (FeeInvoice) spnKindOfFee.getSelectedItem();
                break;
            }
            default:
                break;
        }
    }


    /**
     * Mo ta chuc nang cua ham
     *
     * @param modelEvent
     * @author: apple
     * @return: BaseFragment
     * @throws:
     */
    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {
        ActionEvent act = modelEvent.getActionEvent();
        switch (act.action) {
            case ActionEventConstant.ACTION_GET_DATA_DETAIL_INVOICE: {
                ToastMessageUtil.showToastShort(getActivity(), "Không có dữ liệu!");
            }
            case ActionEventConstant.ACTION_GET_DATA_FEE_INVOICE: {
                ToastMessageUtil.showToastShort(getActivity(), "Không có dữ liệu!");
            }
            case ActionEventConstant.ACTION_SAVE_NEW_DATA_INVOICE: {
                ToastMessageUtil.showToastShort(getActivity(), "Lỗi khi tạo biên lai!");
            }
            break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnCollectingMoney: {
                mListener.onEvent(ActionEventConstant.ACTION_SHOW_DIALOG_CONFIRM_METHOD_INVOICE, null, null);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventControlListener) {
            mListener = (OnEventControlListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEventControlListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
