package com.vnpt.staffhddtpos58;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.common.ConstantsApp;
import com.vnpt.common.ModelEvent;
import com.vnpt.controller.UserController;
import com.vnpt.dto.BaseReponse;
import com.vnpt.dto.CerfiticateItem;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.dto.InvoiceHDDTDetails;
import com.vnpt.dto.InvoiceUpdatePaymentedRequest;
import com.vnpt.dto.ItemAuthorizeForApp;
import com.vnpt.dto.MethodConfirm;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.printproject.PrintReceipt;
import com.vnpt.printproject.woosim.BluetoothPrintService;
import com.vnpt.printproject.woosim.BluetoothPrinterActivity;
import com.vnpt.staffhddtpos58.dialogs.AuthenticationForAppDialog;
import com.vnpt.staffhddtpos58.dialogs.ReturnReceiptInvoiceDialog;
import com.vnpt.utils.Helper;
import com.vnpt.utils.ImageHelperUtil;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.ToastMessageUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailsActivity extends BaseActivity implements OnEventControlListener {
    InvoiceCadmin mHoadon;
    String methodAuthorize = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mHoadon = (InvoiceCadmin) bundle.getSerializable(Common.KEY_DATA_ITEM_INVOICE);
        showDetailsInvoice(mHoadon);
//        showOrHidenMenuReturnInvoice(false);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    //Show lại màn hình chưa trả tiền
    void showDetailsInvoice(InvoiceCadmin invoiceCadmin) {
        DetailsActivityFragment fragment = new DetailsActivityFragment();
        Bundle args = new Bundle();
        //mHoadon.setPaymentStatus(ConstantsApp.StatusPayment.NOT_PAYMENT);
        args.putSerializable(Common.KEY_DATA_ITEM_INVOICE, invoiceCadmin);
        fragment.setArguments(args);
        setFragmentContent(fragment, DetailsActivityFragment.TAG, R.id.root_layout);

    }
//    //Show lại màn hình đã trả tiền
//    void showDetailsPaymented() {
//        DetailsActivityFragment fragment = new DetailsActivityFragment();
//        Bundle args = new Bundle();
//        //mHoadon.setPaymentStatus(ConstantsApp.StatusPayment.PAYMENTED);
//        args.putSerializable(Common.KEY_DATA_ITEM_INVOICE, mHoadon);
//        fragment.setArguments(args);
//        setFragmentContent(fragment, DetailsActivityFragment.TAG, R.id.root_layout);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
//        MenuItem item = menu.findItem(R.id.action_view_content);
//        showOrHidenMenuReturnInvoice(false,item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent returnIntent = getIntent();
                setResult(RESULT_OK, returnIntent);
                finish();
                return true;
            case R.id.action_view_content:
                startActivityContentInvoice();
                return true;
            case R.id.action_refresh:
                // app icon in action bar clicked; go home
//                showDialogReturnInvoice();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void startActivityContentInvoice() {
        Intent itent = new Intent(this, ContentInvoiceActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(Common.KEY_DATA_ITEM_INVOICE, mHoadon);
        itent.putExtras(args);
        startActivity(itent);
    }

    protected void updateTypeCerfiticateInvoice() {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_UPDATE_SIGNED_INVOICE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putString(Common.BUNDLE_KEY_ID_INVOICE, mHoadon.getInvNum());
        String authorize = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REFF_KEY_METHOD_AUTHORIZE_USER);
       /* String finishAuthorize = authorize +"-"+Common.TYPE_VIEW_CERFITICATE_MANUAL;
        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.REFF_KEY_METHOD_AUTHORIZE_USER,finishAuthorize);*/
        bundle.putInt(Common.KEY_STATUS_PAYMENT_INVOICE, 1);
//        bundle.putString(Common.KEY_PATH_CERTIFICATE_USER_MOBILE, mHoadon.getPathImgSignedMobile());
//        bundle.putString(Common.KEY_PATH_CERTIFICATE_USER_SERVER, mHoadon.getPathImgSignedServer());
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }

    public void actionPrintNotificationToPaper(int type, InvoiceHDDTDetails invoiceHDDTDetails) {
        if (type == Common.PRINTER_WOOSIM) {
            ToastMessageUtil.showToastShort(this, "Chức năng cần có máy in nhiệt Woosim để thực hiện!");
//        PrintReceipt.printReceiptDemo_2inch();
//        PrintReceipt.printReceiptDemoVNPT_2inch(this,mHoadon);
//        return;
            if (BluetoothPrinterActivity.mPrintService == null || BluetoothPrinterActivity.mPrintService.getState() != BluetoothPrintService.STATE_CONNECTED) {
                showDialogConfirmConfigBluetooth(type, invoiceHDDTDetails);
                return;
            }
            try {
                PrintReceipt.printNotificationReceiptDemoVNPT_NEW(this, mHoadon);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (type == Common.PRINTER_ER58AI) {
            ToastMessageUtil.showToastShort(this, "Chức năng cần có máy in nhiệt để thực hiện!");
            if (com.vnpt.printproject.er58ai.BluetoothPrinterActivity.mPOSPrinter == null ||
                    !com.vnpt.printproject.er58ai.BluetoothPrinterActivity.mPOSPrinter.isBTAvailable() ||
                    com.vnpt.printproject.er58ai.BluetoothPrinterActivity.mPOSPrinter.getState() != com.vnpt.printproject.er58ai.BluetoothPrinterActivity.mPOSPrinter.STATE_CONNECTED) {
                showDialogConfirmConfigBluetooth(type, invoiceHDDTDetails);
                return;
            }
            try {
                com.vnpt.printproject.er58ai.BluetoothPrinterActivity.printInvoice(invoiceHDDTDetails);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (type == Common.PRINTER_POS58) {

        }
    }

    void showDialogConfirmConfigBluetooth(final int type, final InvoiceHDDTDetails invoiceHDDTDetails) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setIcon(R.drawable.logo_vnpt);
        alertDialogBuilder.setMessage("Bạn chưa thiết lập kết nối thiết bị in Bluetooth. Bạn có muốn kết nối đến thiết bị?");
        alertDialogBuilder.setTitle("Thông báo");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showActivityPrinterBT(type, invoiceHDDTDetails);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    public void showActivityPrinterBT(int type, InvoiceHDDTDetails invoiceHDDTDetails) {
        if (type == Common.PRINTER_WOOSIM) {
            Intent itent = new Intent(this, BluetoothPrinterActivity.class);
            startActivity(itent);
        } else if (type == Common.PRINTER_ER58AI) {
            Intent itent = new Intent(this, com.vnpt.printproject.er58ai.BluetoothPrinterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, invoiceHDDTDetails);
            itent.putExtras(bundle);
            startActivity(itent);
        } else if (type == Common.PRINTER_POS58) {
            Intent itent = new Intent(this, com.vnpt.printproject.er58ai.BluetoothPrinterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, invoiceHDDTDetails);
            itent.putExtras(bundle);
            startActivity(itent);
        }
    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2

        switch (requestCode) {
            case Common.REQUEST_CODE_ACTIVITY_SIGNATURE_MANUAL: {
                if (resultCode == RESULT_OK && data != null) {

                    InvoiceCadmin invoiceResult = (InvoiceCadmin) data.getSerializableExtra(Common.KEY_DATA_ITEM_INVOICE);
                    mHoadon = invoiceResult;
                    updateTypeCerfiticateInvoice();
//                    showDetailsNotPayment();
                    invalidateOptionsMenu();
                    ActivityCompat.invalidateOptionsMenu(this);
//                    Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_LONG);
                }
                break;
            }
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
        switch (eventType) {
            case ActionEventConstant.ACTION_CHANGE_VIEW_DETAILS_FRAGMENT_ACTIVITY: {
//                showDetailsNotPayment();
//                setFragmentContent(DetailsActivityFragment.newInstance(mHoadon), FinishTransactionFragment.TAG,R.id.fragment_details_replace);
                break;
            }
            case ActionEventConstant.ACTION_SHOW_VIEW_SINGNATURE_MANUAL: {
                startActivitySingatureManual();
                break;
            }
            case ActionEventConstant.ACTION_SHOW_VIEW_SINGNATURE_CAMERA: {
                startActivitySingatureCamera();
                break;
            }
            case ActionEventConstant.ACTION_SHOW_CERTIFIED_SINGNATURE_MANUAL: {
                startActivityShowCertifiedImage();
                break;
            }
            case ActionEventConstant.ACTION_SHOW_CERTIFIED_SINGNATURE_CAMERA: {
                startActivityShowCertifiedImageCamera();
                break;
            }
            case ActionEventConstant.ACTION_CONFIRM_CERFITICATE_INVOICE: {
                // showOrHidenMenuReturnInvoice(true);
                MethodConfirm method = (MethodConfirm) data;
                actionAuthorizeForUser(method);
                break;
            }
            case ActionEventConstant.ACTION_SELECTED_AUTHORIZE_FOR_APP_INVOICE: {
                ItemAuthorizeForApp item = (ItemAuthorizeForApp) data;
                switch (item.getIdItem()) {
                    case 1://ký manual
                    {
                        startActivitySingatureManual();
                        break;
                    }
                    case 2://qua record
                    {
                        break;
                    }
                    case 3://qua chụp hình
                    {
                        startActivitySingatureCamera();
                        break;
                    }
                    default: {//ký manual
                        startActivitySingatureManual();
                        break;
                    }
                }

                break;
            }
            case ActionEventConstant.ACTION_SHOW_DIALOG_RETURN_INVOICE: {
                ArrayList<InvoiceUpdatePaymentedRequest> arrData = (ArrayList<InvoiceUpdatePaymentedRequest>) data;
                showDialogReturnInvoice(arrData);
                break;
            }
            case ActionEventConstant.ACTION_CHANGE_STATUS_RETURN_INVOICE: {
                ArrayList<InvoiceUpdatePaymentedRequest> arrData = (ArrayList<InvoiceUpdatePaymentedRequest>) data;
                actionReturnPaymentInvoice(arrData);
                break;
            }
            case ActionEventConstant.ACTION_SHOW_DIALOG_CONFIRM_METHOD_INVOICE: {
                showDiaLogConfirmPayment();
                break;
            }
            case ActionEventConstant.ACTION_PRINT_INVOICE_NOTIFICATION: {
                actionPrintNotificationToPaper(Common.PRINTER_WOOSIM, null);
                break;
            }case ActionEventConstant.ACTION_PRINT_INVOICE_ER58AI: {
                InvoiceHDDTDetails invoiceHDDTDetails = (InvoiceHDDTDetails) data;
                actionPrintNotificationToPaper(Common.PRINTER_ER58AI, invoiceHDDTDetails);
                break;
            }
            default:
                break;
        }
    }

    void showDiaLogConfirmPayment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.message));
        builder.setMessage(getString(R.string.txt_action_confirm_payment))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateStatusPaymentInvoice();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                // this not working because multiplying white background (e.g. Holo Light) has no effect
                //negativeButton.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
                negativeButton.setTextColor(getResources().getColor(R.color.blue2));
                positiveButton.setTextColor(getResources().getColor(R.color.blue2));
                negativeButton.invalidate();
                positiveButton.invalidate();
            }
        });
        dialog.show();
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
            case ActionEventConstant.ACTION_UPATE_STATUS_PAYMENT_INVOICE: {
//                String idInvoicePayment = Common.idInvoiceUpdatedPaymented;
//                if (idInvoicePayment.length() > 0) {
//                    if (idInvoicePayment.substring(idInvoicePayment.length() - 1).equals("-")) {
//                        Common.idInvoiceUpdatedPaymented = idInvoicePayment.substring(0, idInvoicePayment.length() - 1);
//                    }
//                }
//                Common.arrInvoiceUpdatePaymented = new ArrayList<>();
                BaseReponse reponse = (BaseReponse) modelEvent
                        .getModelData();
                //reload lại màn hình detail
                if (reponse.getStatusCode() == ConstantsApp.StatusProgress.SUCCESS) {
                    mHoadon.setPaymentStatus(ConstantsApp.StatusPayment.PAYMENTED);
                    showDetailsInvoice(mHoadon);
                    ToastMessageUtil.showToastShort(DetailsActivity.this, "Cập nhật thành công!");
                } else {
                    ToastMessageUtil.showToastShort(DetailsActivity.this, "Cập nhật trạng thái không thành công!");
                }
                break;
            }
            case ActionEventConstant.ACTION_UPDATE_STATUS_RETURN_INVOICE: {
                BaseReponse reponse = (BaseReponse) modelEvent
                        .getModelData();
                if (reponse.getStatusCode() == Common.DATA_SUCCESS) {
                    ToastMessageUtil.showToastShort(DetailsActivity.this, "Cập nhật thành công!");
                    Intent returnIntent = getIntent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    ToastMessageUtil.showToastShort(DetailsActivity.this, "Cập nhật trạng thái không thành công!");
                }
                break;
            }
            case ActionEventConstant.ACTION_UPDATE_SIGNED_INVOICE: {
                String result = (String) modelEvent
                        .getModelData();
                Helper.getInstance().showLog("" + result);
                if (!result.equals("")) {
                    ToastMessageUtil.showToastShort(this, getString(R.string.txt_save_infor_success));
                } else {
                    ToastMessageUtil.showToastShort(this, getString(R.string.txt_save_infor_not_success));
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
            case ActionEventConstant.ACTION_UPATE_STATUS_PAYMENT_INVOICE: {
//                Common.idInvoiceUpdatedPaymented = "";
//                Common.arrInvoiceUpdatePaymented = new ArrayList<>();
                ToastMessageUtil.showToastShort(this, getString(R.string.txt_save_infor_not_success));
                break;
            }

            case ActionEventConstant.ACTION_UPDATE_STATUS_RETURN_INVOICE: {
                ToastMessageUtil.showToastShort(this, getString(R.string.txt_save_infor_not_success));
                break;
            }
            case ActionEventConstant.ACTION_UPDATE_SIGNED_INVOICE: {
                String result = (String) modelEvent
                        .getModelData();
                Helper.getInstance().showLog("" + result);
                if (!result.equals("")) {
                    ToastMessageUtil.showToastShort(this, getString(R.string.txt_save_infor_success));
                } else {
                    ToastMessageUtil.showToastShort(this, getString(R.string.txt_save_infor_not_success));
                }
                break;
            }
            default:
                Common.idInvoiceUpdatedPaymented = "";
                Common.arrInvoiceUpdatePaymented = new ArrayList<>();
                ToastMessageUtil.showToastShort(DetailsActivity.this, "Xảy ra lỗi trong quá trình xử lý!");
                break;
        }
    }

    void startActivityShowCertifiedImageCamera() {
        Intent intent = new Intent(this, ShowCertifiedImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, mHoadon);
        bundle.putInt(Common.KEY_TYPE_VIEW_CERFITICATE, Common.TYPE_VIEW_CERFITICATE_CAMERA);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void startActivityShowCertifiedImage() {
        Intent intent = new Intent(this, ShowCertifiedImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, mHoadon);
        bundle.putInt(Common.KEY_TYPE_VIEW_CERFITICATE, Common.TYPE_VIEW_CERFITICATE_MANUAL);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void startActivitySingatureManual() {
        Intent intent = new Intent(this, SinatureManualActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, mHoadon);
        intent.putExtras(bundle);
        startActivityForResult(intent, Common.REQUEST_CODE_ACTIVITY_SIGNATURE_MANUAL);
    }


    void startActivitySingatureCamera() {
        ImageHelperUtil.takePhoto(DetailsActivity.this, Common.REQUEST_CODE_ACTIVITY_SIGNATURE_CAMERA);
    }


    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFileCamera() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/"
                + Common.PACKAGE_NAME
                + "/SignCameraHDDT");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "CI_" + timeStamp + ".png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }


    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments >= 1) {
            Intent returnIntent = getIntent();
            setResult(RESULT_OK, returnIntent);
            finish();
            return;
        }
        super.onBackPressed();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        ivImage.setImageBitmap(thumbnail);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
//        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
//        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    protected void updateTypeCerfiticateInvoiceCamera() {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_UPATE_CERTIFIED_ON_INVOICE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putString(Common.KEY_ID_INVOICE_CERTIFICATE, mHoadon.getInvNum());
        String authorize = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REFF_KEY_METHOD_AUTHORIZE_USER);
        String finishAuthorize = authorize + "-" + Common.TYPE_VIEW_CERFITICATE_CAMERA;
        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.REFF_KEY_METHOD_AUTHORIZE_USER, finishAuthorize);
        bundle.putString(Common.KEY_TYPE_UPDATE_CERTIFICATE, finishAuthorize);
        bundle.putInt(Common.KEY_STATUS_PAYMENT_INVOICE, 1);
        String pathImage = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.KEY_PATH_IMAGE_SING_CAMERA);
        bundle.putString(Common.KEY_PATH_CERTIFICATE_USER, pathImage);
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }

    protected void updateTypeCerfiticateInvoiceSMS() {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_UPATE_CERTIFIED_ON_INVOICE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putString(Common.KEY_ID_INVOICE_CERTIFICATE, mHoadon.getInvNum());
        bundle.putInt(Common.KEY_TYPE_UPDATE_CERTIFICATE, Common.TYPE_VIEW_CERFITICATE_SMS);
        bundle.putInt(Common.KEY_STATUS_PAYMENT_INVOICE, 1);
        String pathImage = "Gửi Tin nhắn qua số 01216644955";
        bundle.putString(Common.KEY_PATH_CERTIFICATE_USER, pathImage);
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }

    protected void updateDBTypeCerfiticateInvoice(String typeCertificate) {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_UPATE_CERTIFIED_ON_INVOICE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putString(Common.KEY_ID_INVOICE_CERTIFICATE, mHoadon.getInvNum());
        bundle.putString(Common.KEY_TYPE_UPDATE_CERTIFICATE, typeCertificate);
        bundle.putInt(Common.KEY_STATUS_PAYMENT_INVOICE, 1);
//        String pathImage = "Gửi Tin nhắn qua số 01216644955";
//        bundle.putString(Common.KEY_PATH_CERTIFICATE_USER, pathImage);
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }

    protected void updateStatusPaymentInvoice() {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_UPATE_STATUS_PAYMENT_INVOICE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putString(Common.BUNDLE_KEY_FKEY_INVOICE, mHoadon.getFkey());
        if (mHoadon.getPaymentStatus() == ConstantsApp.StatusPayment.PAYMENTED) {
            bundle.putInt(Common.KEY_STATUS_PAYMENT_INVOICE, ConstantsApp.StatusPayment.NOT_PAYMENT);
        } else {
            bundle.putInt(Common.KEY_STATUS_PAYMENT_INVOICE, ConstantsApp.StatusPayment.PAYMENTED);
        }
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }


    protected void actionAuthorizeForUser(MethodConfirm method) {
        methodAuthorize = "";
        ArrayList<CerfiticateItem> arrCerfiticate = new ArrayList<>();
        if (method.isSendSMS()) {
            methodAuthorize += Common.TYPE_VIEW_CERFITICATE_SMS;
            methodAuthorize += "-";

            CerfiticateItem itemCert = new CerfiticateItem();
            itemCert.setIdTypeCerfiticate(Common.TYPE_VIEW_CERFITICATE_SMS);
            itemCert.setPathCerfiticate("");
            arrCerfiticate.add(itemCert);
        }
        if (method.isPrintToPaper()) {
            methodAuthorize += Common.TYPE_VIEW_CERFITICATE_PRINT_PAPER;
            methodAuthorize += "-";

            CerfiticateItem itemCert = new CerfiticateItem();
            itemCert.setIdTypeCerfiticate(Common.TYPE_VIEW_CERFITICATE_PRINT_PAPER);
            itemCert.setPathCerfiticate("");
            arrCerfiticate.add(itemCert);

        }
        if (method.isSendToEmail()) {
            methodAuthorize += Common.TYPE_VIEW_CERFITICATE_EMAIL;
            methodAuthorize += "-";

            CerfiticateItem itemCert = new CerfiticateItem();
            itemCert.setIdTypeCerfiticate(Common.TYPE_VIEW_CERFITICATE_EMAIL);
            itemCert.setPathCerfiticate("");
            arrCerfiticate.add(itemCert);
        }
        if (method.isSendToAppUser()) {
            methodAuthorize += Common.TYPE_VIEW_CERFITICATE_APP_USER;
            methodAuthorize += "-";

            CerfiticateItem itemCert = new CerfiticateItem();
            itemCert.setIdTypeCerfiticate(Common.TYPE_VIEW_CERFITICATE_APP_USER);
            itemCert.setPathCerfiticate("");
            arrCerfiticate.add(itemCert);
        }
        // remove "-" cuoi cung
        if (methodAuthorize.substring(methodAuthorize.length() - 1).equals("-")) {
            methodAuthorize = methodAuthorize.substring(0, methodAuthorize.length() - 1);
        }
        Log.e("Truonglt", "methodAuthorize:" + methodAuthorize);
        if (Common.arrInvoiceUpdatePaymented == null) {
            return;
        }
        for (int i = 0; i < Common.arrInvoiceUpdatePaymented.size(); i++) {
            InvoiceUpdatePaymentedRequest itemInvoice = Common.arrInvoiceUpdatePaymented.get(i);
            itemInvoice.setArrUserUpdateCerfiticate(arrCerfiticate);
        }
        updateStatusPaymentInvoice();
        handleAuthorizeForCustomer(methodAuthorize);
        // showOrHidenMenuReturnInvoice(true);
    }

    //handle request data server
//    protected void updateStatusPaymentInvoice() {
//        ActionEvent e = new ActionEvent();
//        e.action = ActionEventConstant.ACTION_UPATE_STATUS_PAYMENT_INVOICE_NEW;
//        e.sender = this;
//        Bundle bundle = new Bundle();
//        e.viewData = bundle;
//        UserController.getInstance().handleViewEvent(e);
//    }

    //handle request data server
    protected void actionReturnPaymentInvoice(ArrayList<InvoiceUpdatePaymentedRequest> arrData) {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_UPDATE_STATUS_RETURN_INVOICE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Common.BUNDLE_KEY_ARR_ITEM_REQUEST_UPADTE_STATUS_PAYMENT, arrData);
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }

    protected void handleAuthorizeForCustomer(String methodAuthorize) {
        if (methodAuthorize.equals("")) {
            return;
        }
        String[] arrMethod = methodAuthorize.split("-");
        for (int i = 0; i < arrMethod.length; i++) {
            int method = Integer.parseInt(arrMethod[i]);
            if (method == Common.TYPE_VIEW_CERFITICATE_SMS) {
                actionSendSMS();
            }
            if (method == Common.TYPE_VIEW_CERFITICATE_PRINT_PAPER) {
                actionPrintToPaper();
            }
            if (method == Common.TYPE_VIEW_CERFITICATE_EMAIL) {
                actionSendEmail();
            }
            if (method == Common.TYPE_VIEW_CERFITICATE_APP_USER) {
//                actionShowDialogCerfiticationForApp();
            }
        }
        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.REFF_KEY_METHOD_AUTHORIZE_USER, methodAuthorize);
//        updateDBTypeCerfiticateInvoice(methodAuthorize);
//        showViewDetailsPaymentedWithCertificated(methodAuthorize);
    }

    //https://mobiforge.com/design-development/sms-messaging-android
    public void actionSendSMS() {
        String phoneNo = "+841216644955";
        String sms = "HDDT-VNPT: Quý khách đã thanh toán hóa đơn T6";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, phoneNo, sms, null, null);
            Toast.makeText(this, "Đã gửi thông tin qua SMS thành công!",
                    Toast.LENGTH_LONG).show();
//            updateTypeCerfiticateInvoiceSMS();
//            showDetailsPaymentedWithCertificated(Common.TYPE_VIEW_CERFITICATE_SMS);
            //            mListener.onEvent(ActionEventConstant.ACTION_SHOW_CERTIFIED_SINGNATURE_SMS, null, null);
        } catch (Exception e) {
            Toast.makeText(this,
                    "Gửi SMS thất bại, vui lòng thử lại sau!",
                    Toast.LENGTH_LONG).show();
//            updateTypeCerfiticateInvoiceSMS();
//            showDetailsPaymentedWithCertificated(Common.TYPE_VIEW_CERFITICATE_SMS);
            e.printStackTrace();
        }

    }

    public void actionPrintToPaper() {
//        PrintReceipt.printReceiptDemo_2inch();
//        PrintReceipt.printReceiptDemoVNPT_2inch(this,mHoadon);
        try {
//            PrintReceipt.printReceiptDemoVNPT_NEW(this, mHoadon);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ToastMessageUtil.showToastShort(this, "Chức năng hỗ trợ in hóa đơn qua máy in nhiệt!");
    }

    void actionSendEmail() {
        ToastMessageUtil.showToastShort(this, "Email thông tin hóa đơn của khách hàng đã được gửi!");
    }

    void actionShowDialogCerfiticationForApp() {
        AuthenticationForAppDialog dialog = new AuthenticationForAppDialog(this, this);
        dialog.show(getSupportFragmentManager(), AuthenticationForAppDialog.TAG);
    }

    void showDialogReturnInvoice(ArrayList<InvoiceUpdatePaymentedRequest> arrData) {
        ReturnReceiptInvoiceDialog dialog = new ReturnReceiptInvoiceDialog(this, this, arrData);
        dialog.show(getSupportFragmentManager(), ReturnReceiptInvoiceDialog.TAG);
    }

    void showOrHidenMenuReturnInvoice(boolean isShow, MenuItem item) {
        if (item != null) {
            if (isShow) {
                item.setVisible(true);
            } else {
                item.setVisible(false);
            }
            invalidateOptionsMenu();
            ActivityCompat.invalidateOptionsMenu(this);
        }
    }

}
