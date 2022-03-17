package com.vnpt.staffhddtpos58;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vnpt.adapters.ListExpandableInvoiceAdapter;
import com.vnpt.adapters.ListProdInvAdapter;
import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.common.ModelEvent;
import com.vnpt.controller.UserController;
import com.vnpt.dto.InvoiceDetails;
import com.vnpt.dto.InvoiceTrG;
import com.vnpt.dto.ProductInvoiceDetails;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.staffhddtpos58.fragment.BaseFragment;
import com.vnpt.utils.Helper;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.ToastMessageUtil;

import java.io.File;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.vnpt.utils.Helper.hideSoftKeyboard;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment2 extends BaseFragment implements View.OnClickListener,OnEventControlListener {
    public static String TAG = DetailsActivityFragment2.class.getName();
    InvoiceTrG mHoaDon;
    EditText txtName;
    TextView txtPhone, txtAddress, txtNumberInvoice;
    Button btnConfirmSign;
    OnEventControlListener mListener;
    int previousItem = -1;
    //    ListDebitAdapter adapterDebit;
    ViewStub viewStubImport;
    //View sublayout - telecom
    TextView txtCheckNo, txtTableNo, txtPriceAmount;
    TextView txtTotal, txtServiceCharge, txtVATAMount30, txtVATRate, txtVATAmount, txtSumAmount, txtSumAmountWords;

    ListView listDataService;
    //View sublayout - water
    //View sublayout - eletric
    //View sublayout - environment
    TextView txtMonth;
    ImageButton btnCollapse;
    TextView  txtCodeTaxCus;
    LinearLayout layoutCollapse;
    PhotoViewAttacher mAttacher;
    /**
     * phần thêm nợ
     */
//    TextView txtCusName;
    //    ListView listDataDebit;
    LinearLayout layoutCustomerDebit1, layoutCustomerDebit2;
    ListExpandableInvoiceAdapter adapterDebit;
    InvoiceDetails mItemDebitDetails;
    EditText txtNameRoomNo;
    TextView txtNameOutlet, txtNameTableNo, txtNameCheckNo, txtNameCodeCus ;
    TextView txtStatusSign;
    LinearLayout layoutSignedImage;
    ImageButton imgSignCustomer;
    RelativeLayout layoutMainDetail;
    //View sublayout - television
    public DetailsActivityFragment2() {
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
        View layout = inflater.inflate(R.layout.fragment_details2, container, false);
        layoutMainDetail = (RelativeLayout) layout.findViewById(R.id.layout_main_detail);
        setupUI(layoutMainDetail);
        if (isAdded()) {
            Bundle args = getArguments();
            if (args != null) {
                mHoaDon = (InvoiceTrG) args.getSerializable(Common.KEY_DATA_ITEM_INVOICE);
            }
            init(layout);
            setValueForMembers();
            setEventForMembers();
            if (mHoaDon != null) {
                setDataForViewDetailsNew(mHoaDon);
            }
        }
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupUI(layoutMainDetail);
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
        txtName = (EditText) layout.findViewById(R.id.txtName);
        txtPhone = (TextView) layout.findViewById(R.id.txtPhone);
        txtAddress = (TextView) layout.findViewById(R.id.txtAddress);
        txtNumberInvoice = (TextView) layout.findViewById(R.id.txtNumberInvoice);
//        txtPrice = (TextView) layout.findViewById(R.id.txtPrice);
        txtSumAmount = (TextView) layout.findViewById(R.id.txtSumAmount);
        txtSumAmountWords = (TextView) layout.findViewById(R.id.txtSumAmountWords);
        ///
        txtNameOutlet = (TextView) layout.findViewById(R.id.txtNameOutlet);
        txtNameTableNo = (TextView) layout.findViewById(R.id.txtNameTableNo);
        txtNameCheckNo = (TextView) layout.findViewById(R.id.txtNameCheckNo);
        txtNameCodeCus = (TextView) layout.findViewById(R.id.txtNameCodeCus);
        txtNameRoomNo = (EditText) layout.findViewById(R.id.txtNameRoomNo);


        btnCollapse = (ImageButton) layout.findViewById(R.id.btn_collapse);
//        txtCodeCus = (TextView) layout.findViewById(R.id.txt_code_cus);
        txtCodeTaxCus = (TextView) layout.findViewById(R.id.txt_code_tax_cus);
        layoutCollapse = (LinearLayout) layout.findViewById(R.id.layout_collapse);
        //button
        btnConfirmSign = (Button) layout.findViewById(R.id.btnConfirmSign);
//        txtCusName = (TextView) layout.findViewById(R.id.txtCusName);
        layoutSignedImage = (LinearLayout) layout.findViewById(R.id.layout_signed_image);
        imgSignCustomer = (ImageButton) layout.findViewById(R.id.imgSignCustomer);
        txtStatusSign = (TextView) layout.findViewById(R.id.txtStatusSign);
        //phần nợ
//        listDataDebit= (ListView) layout.findViewById(R.id.listDataDebit);
        layoutCustomerDebit1 = (LinearLayout) layout.findViewById(R.id.layout_customer_debit1);



        viewStubImport = (ViewStub) layout.findViewById(R.id.stub_import);
        viewStubImport.setLayoutResource(R.layout.viewstub_content_detail_telecome);
        View inflatedViewStub = viewStubImport.inflate();
        initLayoutViewStub(inflatedViewStub);
    }

    void initLayoutViewStub(View stubView) {
        txtCheckNo = (TextView) stubView.findViewById(R.id.txtCheckNo);
        txtTableNo = (TextView) stubView.findViewById(R.id.txtTableNo);
        txtPriceAmount = (TextView) stubView.findViewById(R.id.txtPriceAmount);
        txtTotal = (TextView) stubView.findViewById(R.id.txtTotal);
        txtServiceCharge = (TextView) stubView.findViewById(R.id.txtServiceCharge);
        txtVATAMount30 = (TextView) stubView.findViewById(R.id.txtVATAMount30);
        txtVATRate = (TextView) stubView.findViewById(R.id.txtVATRate);
        txtVATAmount = (TextView) stubView.findViewById(R.id.txtVATAmount);

        listDataService = (ListView) stubView.findViewById(R.id.listDataService);
    }


    private void setListViewHeight(ListView listView) {
        ListProdInvAdapter listAdapter = (ListProdInvAdapter) listView.getAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View groupItem = listAdapter.getView(i, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();


        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        if (height < 50)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

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
//        int idCustomer = mHoaDon.getId_customer();
//        int idInvoice = mHoaDon.getIdInvoices();
//        getDataDetailInvoice(0, 0);
        if(mHoaDon.getStatusPaymentedMobile()==0)
        {
            txtStatusSign.setText(R.string.text_status_comfirmed_customer_not_signed);
            layoutSignedImage.setVisibility(View.GONE);
            imgSignCustomer.setImageResource(R.drawable.img_no_photo_to_show);
            btnConfirmSign.setVisibility(View.VISIBLE);
            txtNameRoomNo.setEnabled(true);
            txtName.setEnabled(true);
        }
        else{
            txtStatusSign.setText(R.string.text_status_comfirmed_customer_signed);
            layoutSignedImage.setVisibility(View.VISIBLE);
            btnConfirmSign.setVisibility(View.GONE);
            String roomNo = StoreSharePreferences.getInstance(
                    getActivity()).loadStringSavedPreferences(
                    Common.KEY_DATA_ROOM_NO);
            Helper.getInstance().showLog("xxxxxxxxxxxxxxxx"+roomNo);
            txtNameRoomNo.setText(roomNo);
            txtNameRoomNo.setEnabled(false);
            String cusName = StoreSharePreferences.getInstance(
                    getActivity()).loadStringSavedPreferences(
                    Common.KEY_DATA_NAME_CUS);
            Helper.getInstance().showLog("xxxxxxxxxxxxxxxx:"+cusName);
            txtName.setText(cusName);
            txtName.setEnabled(false);
            loadImage(imgSignCustomer);
        }
        imgSignCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialogViewsigned();
            }
        });
    }
    void loadImage(Object imageSigned)
    {
        String pathImage = StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_PATH_DEFAULT_IMAGE_SIGN_MANUAL)+mHoaDon.getPattern().replace("/","_")+ mHoaDon.getSerial().replace("/","_")+"_"+mHoaDon.getCheckNo()+"_"+mHoaDon.getAmount()+".png";
        if(imageSigned instanceof ImageButton)
        {
            if (pathImage != null && !pathImage.equals("")) {
                Uri uri = Uri.fromFile(new File(pathImage));
                Picasso.with(getContext())
                        .load(uri).placeholder(R.drawable.img_no_photo_to_show)
                        .into((ImageButton)imageSigned);
            }
        }
        else if(imageSigned instanceof PhotoView)
        {
            if (pathImage != null && !pathImage.equals("")) {
                Uri uri = Uri.fromFile(new File(pathImage));
                Picasso.with(getContext())
                        .load(uri).placeholder(R.drawable.img_no_photo_to_show)
                        .into((PhotoView)imageSigned);
            }
        }

    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }



    void callDialogViewsigned()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_view_image, null);
        dialog.setView(dialogLayout);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle(getString(R.string.title_image_sign_customer_));
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        PhotoView viewImage = (PhotoView) dialogLayout.findViewById(R.id.goProDialogImage);
        loadImage(viewImage);
        dialog.show();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {

            }
        });
    }
    Callback imageLoadedCallback = new Callback() {

        @Override
        public void onSuccess() {
            if(mAttacher!=null){
                mAttacher.update();
            }else{
                mAttacher = new PhotoViewAttacher(imgSignCustomer);
            }
        }

        @Override
        public void onError() {
            // TODO Auto-generated method stub

        }
    };
    void setDataForViewDetailsNew(InvoiceTrG invoice) {
//        txtName.setText(invoice.getCusName());
        txtPhone.setText(invoice.getInvoiceXml().getCusPhone());
        txtAddress.setText(invoice.getInvoiceXml().getCusAddress());
        txtCodeTaxCus.setText(invoice.getInvoiceXml().getCusTaxCode());
        txtSumAmount.setText("" + Helper.formatPrice(invoice.getInvoiceXml().getAmount()));
        txtSumAmountWords.setText("" + invoice.getInvoiceXml().getAmountInWords());


        txtNameOutlet.setText("" + StoreSharePreferences.getInstance(
                getActivity()).loadStringSavedPreferences(
                Common.KEY_LOGIN_USER_OUTLET));
        txtNameTableNo.setText(invoice.getTableNo());
        txtNameCheckNo.setText(invoice.getCheckNo());
        txtNameCodeCus.setText(invoice.getInvoiceXml().getCusCode());
//        txtNameRoomNo.setText(invoice.getInvoiceXml().getRoomNo());


        ArrayList<ProductInvoiceDetails> arrProductInvoice = invoice.getInvoiceXml().getArrayListProduct();
        if(arrProductInvoice==null)
        {
            arrProductInvoice = new ArrayList<>();
        }
        ListProdInvAdapter adapter = new ListProdInvAdapter(getActivity(), arrProductInvoice);
        listDataService.setAdapter(adapter);
        setListViewHeight(listDataService);

        txtCheckNo.setText(invoice.getCheckNo());
        txtTableNo.setText(invoice.getTableNo());
        txtPriceAmount.setText("" + Helper.formatPrice(invoice.getInvoiceXml().getAmount()));
        txtTotal.setText("" + Helper.formatPrice(invoice.getInvoiceXml().getTotal()));
        txtServiceCharge.setText("" + Helper.formatPrice(invoice.getInvoiceXml().getServicechargeAmount()));
        txtVATAMount30.setText("" + Helper.formatPrice(invoice.getInvoiceXml().getVatAmount30()));
        txtVATRate.setText("" + Helper.formatPrice(invoice.getInvoiceXml().getVAT_Rate()));
        txtVATAmount.setText("" + Helper.formatPrice(invoice.getInvoiceXml().getVAT_Amount()));

        if(invoice.getStatusPaymentedMobile()==0)
        {
            txtStatusSign.setText(R.string.text_status_comfirmed_customer_not_signed);
            layoutSignedImage.setVisibility(View.GONE);
            imgSignCustomer.setImageResource(R.drawable.img_no_photo_to_show);
            btnConfirmSign.setVisibility(View.VISIBLE);
            txtName.setText(invoice.getCusName());
            txtNameRoomNo.setText(invoice.getInvoiceXml().getRoomNo());
            txtNameRoomNo.setEnabled(true);
            txtName.setEnabled(true);
        }
        else{
            txtStatusSign.setText(R.string.text_status_comfirmed_customer_signed);
            layoutSignedImage.setVisibility(View.VISIBLE);
            btnConfirmSign.setVisibility(View.GONE);
            String roomNo = StoreSharePreferences.getInstance(
                    getActivity()).loadStringSavedPreferences(
                    Common.KEY_DATA_ROOM_NO);
            Helper.getInstance().showLog("xxxxxxxxxxxxxxxx"+roomNo);
            txtNameRoomNo.setText(roomNo);
            txtNameRoomNo.setEnabled(false);
            String cusName = StoreSharePreferences.getInstance(
                    getActivity()).loadStringSavedPreferences(
                    Common.KEY_DATA_NAME_CUS);
            Helper.getInstance().showLog("xxxxxxxxxxxxxxxx:"+cusName);
            txtName.setText(cusName);
            txtName.setEnabled(false);
            loadImage(imgSignCustomer);
        }
    }

    void setDataForViewDetails(InvoiceDetails itemData) {
        txtName.setText(itemData.getName());
        txtPhone.setText(itemData.getPhone());
        txtAddress.setText(itemData.getAddress());
        txtCodeTaxCus.setText(itemData.getMa_sms());
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
        btnConfirmSign.setOnClickListener(this);
        txtPhone.setOnClickListener(this);
        btnCollapse.setOnClickListener(this);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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


    protected void getDataDetailInvoice(int idInvoice, int idCustomer) {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_GET_DATA_DETAIL_INVOICE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putInt(Common.BUNDLE_KEY_ID_INVOICE, idInvoice);
        bundle.putInt(Common.BUNDLE_KEY_ID_CUSTOMER, idCustomer);
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
                InvoiceDetails itemDebitDetails = (InvoiceDetails) modelEvent
                        .getModelData();

                if (itemDebitDetails != null) {
                    mItemDebitDetails = itemDebitDetails;
                    setDataForViewDetails(itemDebitDetails);
                } else {
                    showDiaLogConfirmBackMainScreen();
                }
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

            break;
            default:
                break;
        }
    }

    void showDiaLogConfirmBackMainScreen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Không tồn tại chi tiết hóa đơn. Xin vui lòng liên hệ bộ phận quản trị hệ thống.")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        ((DetailsActivity) getActivity()).finish();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnConfirmSign: {
                String textRoomNo = txtNameRoomNo.getText().toString();
                if (textRoomNo==null)
                {
                    textRoomNo =" ";
                }
                StoreSharePreferences.getInstance(
                        getActivity()).saveStringPreferences(
                        Common.KEY_DATA_ROOM_NO,textRoomNo);
                String nameCus = txtName.getText().toString();
                if (nameCus==null)
                {
                    nameCus =" ";
                }
                StoreSharePreferences.getInstance(
                        getActivity()).saveStringPreferences(
                        Common.KEY_DATA_NAME_CUS,nameCus);
                mListener.onEvent(ActionEventConstant.ACTION_SHOW_VIEW_SINGNATURE_MANUAL, null, null);
                break;
            }
//            case R.id.txtPhone: {
//                if (mHoaDon.getPhone() != null)
//                    Helper.actionCallPhoneCustomer(getActivity(), mHoaDon.getPhone());
//                break;
//            }

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
