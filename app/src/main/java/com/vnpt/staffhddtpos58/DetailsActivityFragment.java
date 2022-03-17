package com.vnpt.staffhddtpos58;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vnpt.adapters.ListProdInvAdapter;
import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.common.ConstantsApp;
import com.vnpt.common.ModelEvent;
import com.vnpt.controller.UserController;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.dto.InvoiceHDDTDetails;
import com.vnpt.dto.ProductInvoiceDetails;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.staffhddtpos58.fragment.BaseFragment;
import com.vnpt.utils.Helper;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.ToastMessageUtil;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends BaseFragment implements View.OnClickListener, OnEventControlListener {
    public static String TAG = DetailsActivityFragment.class.getName();
    InvoiceCadmin mHoaDon;
    TextView txtName, txtPhone,  txtAddress, txtNumberInvoice;
    Button btnCollectingMoney, btnViewInvoicePrevious, btnPrintInvoiceNotification;
    OnEventControlListener mListener;
    ListView listDataProInv;
    ImageButton btnCollapse;
    TextView txtCodeCus, txtCodeTaxCus,txtCusBankNo,txtCusBankName;
    TextView txtTypeInvoice,txtPatternSerialNumInv,txtStatusPayment,txtArisingDate,txtKindOfPayment,txtKindOfFee;
    TextView txtVatRate,txtVatAmount,txtAmount,txtAmountInWords;
    LinearLayout layoutCollapse;
    LinearLayout layoutKindOfFee;

    /**
     * phần thêm nợ
     */
//    TextView  txtSumMoneyDebit;
//    CheckedTextView cbxCheckAll;
    //    ListView listDataDebit;
//    LinearLayout layoutCustomerDebit1;
    ListProdInvAdapter adapterProInv;
    InvoiceHDDTDetails mInvoiceDetails;
    //View sublayout - television
    public DetailsActivityFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FinishTransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static DetailsActivityFragment newInstance(HoaDon hoaDon) {
//        if (fragment==null)
//        {
//            fragment = new DetailsActivityFragment();
//        }
//        Bundle args = new Bundle();
//        args.putSerializable(Common.KEY_DATA_ITEM_INVOICE,hoaDon);
//        fragment.setArguments(args);
//        return fragment;
//    }
//    public void setDataForFragment(HoaDon hoaDon)
//    {
//        mHoaDon = hoaDon;
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_details, container, false);
        if (isAdded()) {
            Bundle args = getArguments();
            if (args != null) {
                mHoaDon = (InvoiceCadmin) args.getSerializable(Common.KEY_DATA_ITEM_INVOICE);
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
     *
     * @author: truonglt2
     * @return: MainActivityFragment
     * @throws:
     */
    @Override
    protected void init(View layout) {
        //label
        layoutKindOfFee = (LinearLayout)layout.findViewById(R.id.ln_kind_of_fee);
        txtCodeCus = (TextView) layout.findViewById(R.id.txt_code_cus);
        txtCodeTaxCus = (TextView) layout.findViewById(R.id.txt_code_tax_cus);
        txtCusBankNo = (TextView) layout.findViewById(R.id.txt_cus_bank_no);
        txtCusBankName = (TextView) layout.findViewById(R.id.txt_cus_bank_name);
        txtName = (TextView) layout.findViewById(R.id.txtName);
        txtPhone = (TextView) layout.findViewById(R.id.txtPhone);
        txtAddress = (TextView) layout.findViewById(R.id.txtAddress);
        txtNumberInvoice = (TextView) layout.findViewById(R.id.txtNumberInvoice);


        btnCollapse = (ImageButton) layout.findViewById(R.id.btn_collapse);
        layoutCollapse = (LinearLayout) layout.findViewById(R.id.layout_collapse);
        //khai id cho phan hoa don
        txtTypeInvoice = (TextView) layout.findViewById(R.id.txt_type_invoice);
        txtPatternSerialNumInv = (TextView) layout.findViewById(R.id.txt_pattern_serial_num_inv);
        txtStatusPayment = (TextView) layout.findViewById(R.id.txt_status_payment);
        txtArisingDate = (TextView) layout.findViewById(R.id.txt_arising_date);
        txtKindOfPayment = (TextView) layout.findViewById(R.id.txt_kind_of_payment);
        txtKindOfFee = (TextView)layout.findViewById(R.id.txt_kind_of_fee);
        //khai id cho gia tri hoa don
        txtVatRate = (TextView) layout.findViewById(R.id.txt_vat_rate);
        txtVatAmount = (TextView) layout.findViewById(R.id.txt_vat_amount);
        txtAmount = (TextView) layout.findViewById(R.id.txt_amount);
        txtAmountInWords = (TextView) layout.findViewById(R.id.txt_amount_in_words);

        //button
        btnCollectingMoney = (Button) layout.findViewById(R.id.btnCollectingMoney);
        btnViewInvoicePrevious = (Button) layout.findViewById(R.id.btnViewInvoicePrevious);
        btnPrintInvoiceNotification = (Button) layout.findViewById(R.id.btnPrintInvoiceNotification);

        //phần nợ
//        txtSumMoneyDebit = (TextView) layout.findViewById(R.id.txtSumMoneyDebit);
//        listDataDebit= (ListView) layout.findViewById(R.id.listDataDebit);
        listDataProInv = (ListView) layout.findViewById(R.id.listDataProInv);
//        layoutCustomerDebit1 = (LinearLayout) layout.findViewById(R.id.layout_customer_debit1);

//        viewStubImport = (ViewStub) layout.findViewById(R.id.stub_import);
//        int demoType = StoreSharePreferences.getInstance(getActivity()).loadIntegerSavedPreferences(Common.REFF_KEY_TYPE_DATA_DEMO);
//        if (demoType == Common.VALUE_REFF_KEY_TYPE_DATA_DEMO_ENVIRONMENT) {
//            viewStubImport.setLayoutResource(R.layout.viewstub_content_detail_environtment);
//        }
//        View inflatedViewStub = viewStubImport.inflate();
//        initLayoutViewStub(inflatedViewStub, demoType);
    }

//    void initLayoutViewStub(View stubView, int demoType) {
//       if (demoType == Common.VALUE_REFF_KEY_TYPE_DATA_DEMO_ENVIRONMENT) {
//            txtUnitConsume = (TextView) stubView.findViewById(R.id.txtUnitConsume);
//            txtContentConsume = (TextView) stubView.findViewById(R.id.txtContentConsume);
//            txtPrice = (TextView) stubView.findViewById(R.id.txtPrice);
//            txtMonth = (TextView) stubView.findViewById(R.id.txtMonth);
//            txtMonth.setText("" + ((new Date().getMonth()) + 1));
//        }
//
//
//    }


    private void setListViewExpanableHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    private void setListviewHeight(ListView listView)
    {
        ListProdInvAdapter listadp = (ListProdInvAdapter)listView.getAdapter();
        if (listadp != null) {
            int totalHeight = 0;
            for (int i = 0; i < listadp.getCount(); i++) {
                View listItem = listadp.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listadp.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
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
        String tokenInvoice = mHoaDon.getPattern()+";"+mHoaDon.getSerial()+";"+mHoaDon.getInvNum();
        String fkey = mHoaDon.getFkey();
        getDataDetailInvoice(fkey);
    }

    void setDataForViewDetailsCustomer(InvoiceHDDTDetails itemData) {
        txtName.setText(itemData.getCusName());
        txtPhone.setText(itemData.getComPhone());
        txtAddress.setText(itemData.getCusAddress());
        txtCodeCus.setText(itemData.getCusCode());
        txtCodeTaxCus.setText(itemData.getCusTaxCode());
        txtCusBankNo.setText(itemData.getCusBankNo());
        txtCusBankName.setText(itemData.getCusBankName());
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
        btnCollectingMoney.setOnClickListener(this);
        btnViewInvoicePrevious.setOnClickListener(this);
        btnPrintInvoiceNotification.setOnClickListener(this);
        txtPhone.setOnClickListener(this);
        btnCollapse.setOnClickListener(this);
//        cbxCheckAll.setOnClickListener(this);
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
        switch (eventType) {
            case ActionEventConstant.ACTION_CHECK_ALL_DEBIT_INVOICE: {
//                updateValueDebit();
                break;
            }
        }
    }

//    void updateValueDebit() {
//        int countChecked = adapterDebit.getItemChecked();
//        if (countChecked == adapterDebit.getGroupCount()) {
//            cbxCheckAll.setChecked(true);
//        } else {
//            cbxCheckAll.setChecked(false);
//        }
//        double moneyCurrent = adapterDebit.getMoneyItemChecked();
//        txtSumMoneyDebit.setText("" + Helper.formatPrice(moneyCurrent) + Common.CURRENCY);
//    }

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
     *
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

                if (invoiceHDDTDetails != null) {
                    mInvoiceDetails = invoiceHDDTDetails;
                    setDataForViewDetailsCustomer(invoiceHDDTDetails);
                    setDataForViewDetailsInvoice(invoiceHDDTDetails);
                } else {
                    showDiaLogConfirmBackMainScreen();
                }
                break;
            }

            default:
                break;
        }
    }



    void setDataForViewDetailsInvoice(InvoiceHDDTDetails itemData) {
        int typeSys = StoreSharePreferences.getInstance(getActivity()).loadIntegerSavedPreferences(Common.KEY_TYPE_SYS_MOBILE);
        switch (typeSys)
        {
            case ConstantsApp.TypeSysInvMobile.INVOICE:
            {
                txtTypeInvoice.setText(getString(R.string.type_app_invoice)+" - "+itemData.getInvoiceName());
                layoutKindOfFee.setVisibility(View.GONE);
                break;
            }
            case ConstantsApp.TypeSysInvMobile.RECEIPT:
            {
                txtTypeInvoice.setText(getString(R.string.type_app_receipt)+" - "+itemData.getInvoiceName());
                // set gia tri rieng cho bien lai
                layoutKindOfFee.setVisibility(View.VISIBLE);
//                txtKindOfFee.setText(""+itemData.getExtra());
                txtKindOfFee.setText(""+itemData.getArrayListProduct().get(0).getProdName());
                for (ProductInvoiceDetails itemProData : itemData.getArrayListProduct()  ) {
//                    itemProData.setProdName(""+itemData.getExtra());
                    itemProData.setProdName(""+itemProData.getProdName());
                }
                break;
            }
            default:
                txtTypeInvoice.setText(getString(R.string.type_app_invoice)+" - "+itemData.getInvoiceName());
                break;
        }

        txtPatternSerialNumInv.setText(itemData.getInvoicePattern()+" - "+itemData.getSerialNo()+" - "+mHoaDon.getInvNum());
        if(mHoaDon.getPaymentStatus() == ConstantsApp.StatusPayment.NOT_PAYMENT){
            txtStatusPayment.setText(""+getString(R.string.title_status_not_payment));
            txtStatusPayment.setTextColor(getResources().getColor(R.color.red));
            btnCollectingMoney.setVisibility(View.VISIBLE);
        }
        else{
            txtStatusPayment.setText(""+getString(R.string.title_status_paymented));
            txtStatusPayment.setTextColor(getResources().getColor(R.color.green));
            btnCollectingMoney.setVisibility(View.GONE);
        }
        adapterProInv = new ListProdInvAdapter(getActivity(),itemData.getArrayListProduct());
        listDataProInv.setAdapter(adapterProInv);
        adapterProInv.notifyDataSetChanged();
        setListviewHeight(listDataProInv);
        txtArisingDate.setText(""+itemData.getArisingDate());
        txtKindOfPayment.setText(""+itemData.getKind_of_Payment());
        //khai id cho gia tri hoa don
        txtVatRate.setText(""+itemData.getVAT_Rate()+"%");
        txtVatAmount.setText(""+Helper.formatPrice(itemData.getVAT_Amount())+" "+itemData.getCurrencyUnit());
        txtAmount.setText(""+Helper.formatPrice(itemData.getAmount())+" "+itemData.getCurrencyUnit());
        txtAmountInWords.setText(""+ itemData.getAmountInWords());
//        layoutCustomerDebit1.setVisibility(View.VISIBLE);
//        adapterDebit = new ListExpandableInvoiceAdapter(getActivity(), this);
//        adapterDebit.addItem(itemData);
//        adapterDebit.notifyDataSetChanged();
//        listDataDebit.setAdapter(adapterDebit);
//        previousItem = adapterDebit.getGroupCount() - 1;
//        listDataDebit.expandGroup(previousItem);
//
//        listDataDebit.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//        listDataDebit.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                if (groupPosition != previousItem)
//                    listDataDebit.collapseGroup(previousItem);
//                previousItem = groupPosition;
//            }
//        });
//
//        listDataDebit.setGroupIndicator(null);
//        listDataDebit.setDivider(null);
//        listDataDebit.setChildDivider(null);
//        updateValueDebit();
    }

    /**
     * Mo ta chuc nang cua ham
     *
     * @param modelEvent
     *
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
                showDiaLogConfirmBackMainScreen();
            }
            break;
            default:
                break;
        }
    }

    void showDiaLogConfirmBackMainScreen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Không tồn tại chi tiết biên nhận. Xin vui lòng liên hệ bộ phận quản trị hệ thống.")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((DetailsActivity) getActivity()).finish();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ((DetailsActivity) getActivity()).finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnCollectingMoney: {
//                showDiaLogFinishTransaction();
                Common.idInvoiceUpdatedPaymented = "";
                Common.arrInvoiceUpdatePaymented = new ArrayList<>();
//                if (isPaymentMoreMonth) {
//                    double moneyCurrent = adapterDebit.getMoneyItemChecked();
//                    if (moneyCurrent == 0) {
//                        ToastMessageUtil.showToastShort(getActivity(), "Xin vui lòng chọn tháng thanh toán cước!");
//                        return;
//                    }
//
//                    ArrayList<Integer> arrInt = adapterDebit.getAllIdInvoiceChecked();
//                    if (arrInt != null || arrInt.size() > 0) {
//                        Common.arrInvoiceUpdatePaymented = new ArrayList<>();
//                        for (int i = 0; i < arrInt.size(); i++) {
//                            InvoiceUpdatePaymentedRequest itemInvoice = new InvoiceUpdatePaymentedRequest();
//                            itemInvoice.setIdCustomer(mItemDebitDetails.getId_customer());
//                            itemInvoice.setIdInvoice(arrInt.get(i));
//                            Common.idInvoiceUpdatedPaymented += arrInt.get(i) + "-";
//                            Common.arrInvoiceUpdatePaymented.add(itemInvoice);
//                        }
//                    } else {
//                        ToastMessageUtil.showToastShort(getActivity(), "Xin vui lòng chọn tháng thanh toán cước!");
//                        return;
//                    }
//                } else {
//                    InvoiceUpdatePaymentedRequest itemInvoice = new InvoiceUpdatePaymentedRequest();
//                    itemInvoice.setIdCustomer(mItemDebitDetails.getId_customer());
//                    itemInvoice.setIdInvoice(mItemDebitDetails.getArrData().get(0).getIdInvoices());
//                    Common.idInvoiceUpdatedPaymented = "" + mItemDebitDetails.getArrData().get(0).getIdInvoices();
//                    Common.arrInvoiceUpdatePaymented = new ArrayList<>();
//                    Common.arrInvoiceUpdatePaymented.add(itemInvoice);
//                }
                mListener.onEvent(ActionEventConstant.ACTION_SHOW_DIALOG_CONFIRM_METHOD_INVOICE, null, null);
                break;
            }
            case R.id.btnViewInvoicePrevious: {
                showActivityPreviousInvoice();
                break;
            }
            case R.id.txtPhone: {
                if (mInvoiceDetails.getCusPhone() != null)
                    Helper.actionCallPhoneCustomer(getActivity(), mInvoiceDetails.getCusPhone());
                break;
            }
            case R.id.btnPrintInvoiceNotification: {
//                mListener.onEvent(ActionEventConstant.ACTION_PRINT_INVOICE_NOTIFICATION, null, null);
                mListener.onEvent(ActionEventConstant.ACTION_PRINT_INVOICE_ER58AI, null, mInvoiceDetails);
                break;
            }

            case R.id.btn_collapse: {
                boolean isVisible = (layoutCollapse.getVisibility() == View.VISIBLE);
                if (isVisible) {
                    btnCollapse.setImageResource(R.drawable.toggle_collapse_blue);
                    layoutCollapse.setVisibility(View.GONE);
                } else {
                    btnCollapse.setImageResource(R.drawable.toggle_expand_blue);
                    layoutCollapse.setVisibility(View.VISIBLE);
                }
                break;
            }
//            case R.id.cbxCheckAll: {
//                if (cbxCheckAll.isChecked()) {
//                    adapterDebit.setCheckAll(false);
//                    adapterDebit.notifyDataSetChanged();
//                } else {
//                    adapterDebit.setCheckAll(true);
//                    adapterDebit.notifyDataSetChanged();
//                }
//                break;
//            }
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

    void showActivityPreviousInvoice() {
//        Intent itent = new Intent(getActivity(), PreviousInvoiceActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, mHoaDon);
//        itent.putExtras(bundle);
//        startActivity(itent);
    }
    //    void showDiaLogFinishTransaction() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage("Bạn đã thu tiền của khách hàng " + "" + "?")
//                .setCancelable(false)
//                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // Change to finish transaction
//                        mListener.onEvent(ActionEventConstant.ACTION_CHANGE_VIEW_FINISH_TRANSACTION, null, null);
//                         dialog.cancel();
//                    }
//                })
//                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
}
