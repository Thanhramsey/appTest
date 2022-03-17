package com.vnpt.staffhddtpos58;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.common.ErrorConstants;
import com.vnpt.common.ModelEvent;
import com.vnpt.controller.UserController;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.staffhddtpos58.dialogs.CustomerSigningDialogFragment;
import com.vnpt.utils.DialogUtils;
import com.vnpt.utils.GeneralsUtils;
import com.vnpt.utils.Helper;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.ToastMessageUtil;

import static com.vnpt.common.Common.BUNDLE_KEY_HTML_SIGNED_SUCCESS;
import static com.vnpt.utils.Helper.hideSoftKeyboard;


public class ContentInvoiceActivity extends BaseActivity  {
    //http://stackoverflow.com/questions/6814268/android-share-on-facebook-twitter-mail-ecc
    // các action share qua tài khoản
    WebView webContent;
    String pathLoadHTML = "";
    LinearLayout layoutProccessing;
    InvoiceCadmin mHoadon;
    EditText txtName;
    EditText txtNameRoomNo;
    LinearLayout noDataLayout;
    LinearLayout layoutDataWebView;
    int statusPayment = 0;
    int statusViewMenu = 0;//2 - hiện đầy đủ ; 1- Hiện chỉ nút print; 0 - Không hiện nút chi hết
    String resultHTML_NotSign;
    String htmltSuccess;
    private AwesomeProgressDialog dg;
    CustomerSigningDialogFragment customerSigningDialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_invoice);
        setupUI(findViewById(R.id.layout_content_invoice));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dg = DialogUtils.showLoadingDialog(getString(R.string.text_proccessing), this,null);
        layoutProccessing = (LinearLayout) findViewById(R.id.layout_proccessing);
        webContent = (WebView) findViewById(R.id.webContent);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mHoadon = (InvoiceCadmin) bundle.getSerializable(Common.KEY_DATA_ITEM_INVOICE);
        }
        if(mHoadon!=null)
        {
            if(mHoadon.getStatus()==0)
            {
                int screen = StoreSharePreferences.getInstance(this).loadIntegerSavedPreferences(Common.KEY_FROM_WHICH_SCREEN);
                if(screen ==Common.STATUS_NOT_PAYMENT)
                {
                    statusViewMenu =2;
                }
                else
                    {
                        statusViewMenu =1;
                    }
            }
            else if (mHoadon.getStatus()==1||mHoadon.getStatus()==2||mHoadon.getStatus()==3||mHoadon.getStatus()==4||mHoadon.getStatus()==5)
                {
                    statusViewMenu =1;
                }
                else
                    {
                        statusViewMenu = 0;
                    }
        }
        else{
            statusViewMenu = 0;
        }

        txtNameRoomNo = (EditText) findViewById(R.id.txtNameRoomNo);
        txtName = (EditText) findViewById(R.id.txtName);
        noDataLayout = (LinearLayout)findViewById(R.id.noDataLayout);
        layoutDataWebView = (LinearLayout)findViewById(R.id.layoutDataWebView);

        WebSettings webSettings = webContent.getSettings();
//        webContent.setVisibility(View.GONE);
//        layoutProccessing.setVisibility(View.VISIBLE);
        showViewWebContent(false);

        webSettings.setDatabaseEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
        {
//            webSettings.setDatabasePath(getFilesDir().getPath() + "databases/");
        }
        if (Build.VERSION.SDK_INT >= 11)
        {
            webContent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        // set nhỏ view vừa đủ webview
        //webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        webContent.setInitialScale(140);
//        webContent.scrollTo(70,10);
//        if(mHoadon.getStatusPaymentedMobile()==0)
//        {


//        }
//        else
//        {
//            pathLoadHTML = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.KEY_PATH_DEFAULT_HTML_INVOICE)  + mHoadon.getPattern().replace("/","_")+ mHoadon.getSerial().replace("/","_")+"_"+mHoadon.getCheckNo()+"_"+mHoadon.getAmount()+".html";
//            webContent.loadUrl("file:///" + pathLoadHTML);
//        }


//        int fromScreen = StoreSharePreferences.getInstance(this).loadIntegerSavedPreferences(Common.KEY_FROM_WHICH_SCREEN);
//        if(fromScreen==Common.STATUS_NOT_PAYMENT)
//        {
//
//            new AsysncTaskProccessingDataService(this).execute();
//
//        }
//        else
//            {
//                pathLoadHTML = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.KEY_PATH_DEFAULT_HTML_INVOICE)  + mHoadon.getPattern().replace("/","_")+ mHoadon.getSerial().replace("/","_")+"_"+mHoadon.getCheckNo()+"_"+mHoadon.getAmount()+".html";
//                webContent.loadUrl("file:///" + pathLoadHTML);
//            }
//        if(statusPayment == Common.STATUS_NOT_PAYMENT)
//        {
//            StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_PATH_IMAGE_SIGN_SERVER_RECENTLY,"");
//        }
//        new AsysncTaskProccessingDataService(this).execute();
//        if(mHoadon.getStatusPaymentedMobile()==0)// nhớ sửa
//        {
////            txtName.setText(mHoadon.getCusName());
//            txtName.setText("");
//            txtNameRoomNo.setText(mHoadon.getInvoiceXml().getRoomNo());
//            txtNameRoomNo.setEnabled(true);
//            txtName.setEnabled(true);
//        }
//        else
//        {
//            String roomNo = StoreSharePreferences.getInstance(
//                    this).loadStringSavedPreferences(
//                    Common.KEY_DATA_ROOM_NO);
//            txtNameRoomNo.setText(roomNo);
//            txtNameRoomNo.setEnabled(false);
//            String cusName = StoreSharePreferences.getInstance(
//                    this).loadStringSavedPreferences(
//                    Common.KEY_DATA_NAME_CUS);
//            txtName.setText(cusName);
//            txtName.setEnabled(false);
//        }
//        showViewWebContent(true);
        getRequestInvoiceByFkey();
    }

    void showViewWebContent(boolean isShow)
    {
        if(isShow)
        {
            webContent.setVisibility(View.VISIBLE);
            layoutProccessing.setVisibility(View.GONE);
        }
        else
            {
                webContent.setVisibility(View.GONE);
                layoutProccessing.setVisibility(View.VISIBLE);
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSoftKeyboard(ContentInvoiceActivity.this);
        checkConnection();
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content_invoice, menu);
        MenuItem itemSign = menu.findItem(R.id.action_sign);
        MenuItem itemPrint = menu.findItem(R.id.action_print);
//        if(statusViewMenu == 2)// hiện đầy đủ menu
//        {
//            showOrHidenMenuReturnInvoice(true,itemSign);
//            showOrHidenMenuReturnInvoice(true,itemPrint);
//        }
//        else if (statusViewMenu==1)
//        {
//            //Chỉ hiện nút print
//            showOrHidenMenuReturnInvoice(false,itemSign);
//            showOrHidenMenuReturnInvoice(true,itemPrint);
//        }
//        else
//        {
//            //Không hiện gì cả
//            showOrHidenMenuReturnInvoice(false,itemSign);
//            showOrHidenMenuReturnInvoice(false,itemPrint);
//        }
        //Chỉ hiện nút print
        showOrHidenMenuReturnInvoice(false,itemSign);
        showOrHidenMenuReturnInvoice(true,itemPrint);
        return super.onCreateOptionsMenu(menu);
    }
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        if (menu.findItem(R.id.action_sign) != null)
//            menu.findItem(R.id.action_sign).setVisible(false);
//        if (menu.findItem(R.id.action_print) != null)
//            menu.findItem(R.id.action_print).setVisible(false);
//        return false;
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Common.BUNDLE_KEY_BACKLIST,1);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                return true;
            case R.id.action_print:
                // app icon in action bar clicked; go home
//                ToastMessageUtil.showToastLong(ContentInvoiceActivity.this,"Chức năng này sẽ hỗ trợ in qua máy in nhiệt hoặc Google Cloud Print");
                createWebPrintJob(webContent);
                return true;
            case R.id.action_sign:
                // app icon in action bar clicked; go home
//                showDiaLogConfirmSign();
//                startActivitySingatureManual();
                showDialogSignCustomer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void showDialogSignCustomer()
    {
        FragmentManager fm = getSupportFragmentManager();
        if(customerSigningDialogFragment==null)
        {
            customerSigningDialogFragment = CustomerSigningDialogFragment.newInstance(ContentInvoiceActivity.this,mHoadon);
            customerSigningDialogFragment.setmListener(this);
            customerSigningDialogFragment.show(fm, CustomerSigningDialogFragment.TAG);
        }


    }
    void startActivitySingatureManual() {
        String textRoomNo = txtNameRoomNo.getText().toString();
        if (textRoomNo==null)
        {
            textRoomNo =" ";
        }
        StoreSharePreferences.getInstance(
                this).saveStringPreferences(
                Common.KEY_DATA_ROOM_NO,textRoomNo);
        String nameCus = txtName.getText().toString();
        if (nameCus==null)
        {
            nameCus =" ";
        }
        StoreSharePreferences.getInstance(
                this).saveStringPreferences(
                Common.KEY_DATA_NAME_CUS,nameCus);
        Intent intent = new Intent(this, SinatureManualActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, mHoadon);
        intent.putExtras(bundle);
        startActivityForResult(intent, Common.REQUEST_CODE_ACTIVITY_SIGNATURE_MANUAL);
    }
    private void createWebPrintJob(WebView webView) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
            PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
            String jobName = getString(R.string.app_name) + " Print Test";
            printManager.print(jobName, printAdapter,new PrintAttributes.Builder().build());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSION_REQUEST_CODE);
                }
            }
            return;
        }
    }
    void showDiaLogConfirmSign() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.txt_confirm_sign_app))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startActivitySingatureManual();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        switch (requestCode) {
            case Common.REQUEST_CODE_ACTIVITY_SIGNATURE_MANUAL: {
                if (resultCode == RESULT_OK && data != null) {
                    dg = DialogUtils.showLoadingDialog(getString(R.string.text_proccessing), this,null);
                    Bundle bundleSuccess= data.getExtras();
                    resultHTML_NotSign = bundleSuccess.getString(BUNDLE_KEY_HTML_SIGNED_SUCCESS,resultHTML_NotSign);
                    statusViewMenu = 1;
                    if (resultHTML_NotSign != null && !resultHTML_NotSign.equals("")) {
//                        webContent.loadData(resultHTML_NotSign, "text/html", "UTF-8");
                        webContent.loadDataWithBaseURL(null,resultHTML_NotSign,"text/html", "utf-8", null);
                        showViewWebContent(true);
                    }
                    else{
                        showViewNoDataInvoice();
                    }
                    invalidateOptionsMenu();
                    ActivityCompat.invalidateOptionsMenu(this);
//                    Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_LONG);
                    if (dg != null) {
                        //if (dg.isShowing())
                        dg.hide();
                    }
                }
                break;
            }
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }
    protected void getRequestInvoiceByFkey() {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_FKEY;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putString(Common.BUNDLE_KEY_FKEY_INVOICE, mHoadon.getFkey());
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }

//    protected void updateTypeCerfiticateInvoice() {
//        ActionEvent e = new ActionEvent();
//        e.action = ActionEventConstant.ACTION_UPDATE_SIGNED_INVOICE;
//        e.sender = this;
//        Bundle bundle = new Bundle();
//        bundle.putInt(Common.BUNDLE_KEY_ID_INVOICE, mHoadon.getId());
//        String authorize = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REFF_KEY_METHOD_AUTHORIZE_USER);
//       /* String finishAuthorize = authorize +"-"+Common.TYPE_VIEW_CERFITICATE_MANUAL;
//        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.REFF_KEY_METHOD_AUTHORIZE_USER,finishAuthorize);*/
//        bundle.putInt(Common.KEY_STATUS_PAYMENT_INVOICE,1);
//        bundle.putString(Common.KEY_PATH_CERTIFICATE_USER_MOBILE, mHoadon.getPathImgSignedMobile());
//        bundle.putString(Common.KEY_PATH_CERTIFICATE_USER_SERVER, mHoadon.getPathImgSignedServer());
//        e.viewData = bundle;
//        UserController.getInstance().handleViewEvent(e);
//    }
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
//            case ActionEventConstant.ACTION_UPDATE_SIGNED_INVOICE: {
//               String result = (String)modelEvent
//                        .getModelData();
//                if (result != null && !result.equals("")) {
//                    mHoadon.setStatusPaymentedMobile(1);
//                    mHoadon.setPaymentStatus(1);
//                    mHoadon.setStatusCusSigned(1);
//                    new AsysncTaskProccessingDataService(this).execute();
//                        String roomNo = StoreSharePreferences.getInstance(
//                                this).loadStringSavedPreferences(
//                                Common.KEY_DATA_ROOM_NO);
//                        txtNameRoomNo.setText(roomNo);
//                        txtNameRoomNo.setEnabled(false);
//                        String cusName = StoreSharePreferences.getInstance(
//                                this).loadStringSavedPreferences(
//                                Common.KEY_DATA_NAME_CUS);
//                        txtName.setText(cusName);
//                        txtName.setEnabled(false);
//
//                }
//
//            }
            case ActionEventConstant.ACTION_UPDATE_SIGNED_INVOICE: {

                htmltSuccess = (String) modelEvent.getModelData();
                statusViewMenu = 1;
                if (htmltSuccess != null && !htmltSuccess.equals("")) {
//                        webContent.loadData(resultHTML_NotSign, "text/html", "UTF-8");
                    webContent.loadDataWithBaseURL(null,htmltSuccess,"text/html", "utf-8", null);
                    showViewWebContent(true);
//                    webContent.setInitialScale(140);
//                    webContent.scrollTo(50,10);
                }
                else{
                    showViewNoDataInvoice();
                }
                invalidateOptionsMenu();
                ActivityCompat.invalidateOptionsMenu(this);
                showDialogUpdate(ContentInvoiceActivity.this);
                if (dg != null) {
                    //if (dg.isShowing())
                    dg.hide();
                }
                customerSigningDialogFragment = null;
                break;
            }
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_FKEY: {
                resultHTML_NotSign = (String)modelEvent
                        .getModelData();
                if (resultHTML_NotSign != null && !resultHTML_NotSign.equals("")) {
//                    webContent.loadData(resultHTML_NotSign, "text/html", "UTF-8");
                    webContent.loadDataWithBaseURL(null,resultHTML_NotSign,"text/html", "utf-8", null);
                    showViewWebContent(true);
                }
                else{
                    showViewNoDataInvoice();
                }
                break;
            }
            default:
                break;
        }
        if (dg != null) {
            //if (dg.isShowing())
            dg.hide();
        }
    }
    public void showDialogUpdate(final Context mContext) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(mContext.getString(R.string.message));
        alertDialog.setMessage(mContext.getString(R.string.txt_update_singature_customer_successful));
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setPositiveButton(mContext.getString(R.string.OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_PATH_IMAGE_SING_MANUAL,file.getAbsolutePath());
                        GeneralsUtils.forceHideKeyboard(ContentInvoiceActivity.this);
                        hideSoftKeyboard(ContentInvoiceActivity.this);
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
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
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_FKEY: {
                ToastMessageUtil.showToastShort(ContentInvoiceActivity.this,  modelEvent.getModelMessage());
                // stopping swipe refresh
                showViewWebContent(false);
                showViewNoDataInvoice();
                statusViewMenu = 1;
                invalidateOptionsMenu();
                ActivityCompat.invalidateOptionsMenu(this);
                break;
            }

            case ActionEventConstant.ACTION_UPDATE_SIGNED_INVOICE: {
                ToastMessageUtil.showToastShort(ContentInvoiceActivity.this,  getString(R.string.text_process_err_sys));
                // stopping swipe refresh
                showViewNoDataInvoice();
            }
            break;
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST: {
                ToastMessageUtil.showToastShort(ContentInvoiceActivity.this, getString(R.string.text_no_data));
                // stopping swipe refresh
                showViewNoDataInvoice();
            }
            break;
            case ErrorConstants.ERROR_COMMON: {
                ToastMessageUtil.showToastShort(ContentInvoiceActivity.this, getString(R.string.text_process_err_sys));
                // stopping swipe refresh
                showViewNoDataInvoice();
            }
            break;
            default:
                ToastMessageUtil.showToastShort(ContentInvoiceActivity.this, getString(R.string.text_process_err_sys));
                showViewNoDataInvoice();
                break;
        }
        if (dg != null) {
            //if (dg.isShowing())
            dg.hide();
        }
    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(ContentInvoiceActivity.this);
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
    void showViewNoDataInvoice()
    {
        layoutDataWebView.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.VISIBLE);
    }
    @Override
    public void onEvent(int eventType, View control, Object data) {
//        Album album = (Album) data;
        switch (eventType) {
            case ActionEventConstant.ACTION_CHANGE_CANCEL_DIALOG_SIGNING:// down load book
//                getSupportFragmentManager()
//                ToastMessageUtil.showToastShort(this,"Cancel");
                GeneralsUtils.forceHideKeyboardUseToggle(ContentInvoiceActivity.this);
                customerSigningDialogFragment = null;
                //hideSoftKeyboard(ContentInvoiceActivity.this);
                //GeneralsUtils.turnOffKeyboard(ContentInvoiceActivity.this);

                break;
            case ActionEventConstant.ACTION_CHANGE_OK_DIALOG_SIGNING://
//                ToastMessageUtil.showToastShort(this,"OK");

                dg = DialogUtils.showLoadingDialog(getString(R.string.text_proccessing), this,null);
                Bundle bundleT = (Bundle) data;
                String nameCus = bundleT.getString(Common.BUNDLE_KEY_CUS_NAME," ");
                String textRoomNo = bundleT.getString(Common.BUNDLE_KEY_ROOM_NO," ");
                String strBase64 = bundleT.getString(Common.BUNDLE_KEY_BASE64_IMAGE);
                if (!Helper.isStringEmpty(strBase64)) {
                    String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Inv>" +
                            "<CusName>" + nameCus + "</CusName>" +
                            "<RoomNo>" + textRoomNo + "</RoomNo>" +
                            "<CusSignBase64>" + strBase64 + "</CusSignBase64>" +
                            "</Inv>";
                    ActionEvent e = new ActionEvent();
                    e.action = ActionEventConstant.ACTION_UPDATE_SIGNED_INVOICE;
                    e.sender = this;
                    Bundle bundle = new Bundle();
                    bundle.putString(Common.BUNDLE_KEY_FKEY_INVOICE, mHoadon.getFkey());
//                    bundle.putString(Common.BUNDLE_KEY_CHECKNO, mHoadon.getCheckNo());
                    bundle.putString(Common.BUNDLE_KEY_PATTERN, mHoadon.getPattern());
                    bundle.putString(Common.BUNDLE_KEY_SERIAL, mHoadon.getSerial());
                    bundle.putString(Common.BUNDLE_KEY_XMLINVDATA, xmlString);
                    e.viewData = bundle;
                    UserController.getInstance().handleViewEvent(e);
                } else {
                    if (dg != null) {
                        //if (dg.isShowing())
                        dg.hide();
                    }
                    customerSigningDialogFragment = null;
                    ToastMessageUtil.showToastShort(ContentInvoiceActivity.this, getString(R.string.text_warning_sign_input_infor));
                }
                break;
            case ActionEventConstant.ACTION_CHANGE_CLEAR_DIALOG_SIGNING:
                ToastMessageUtil.showToastShort(this,"CLEAR");
                break;
            default:
                break;
        }
    }


}
