package com.vnpt.staffhddtpos58;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.support.v7.app.AlertDialog;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.common.ModelEvent;
import com.vnpt.controller.UserController;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.utils.DialogUtils;
import com.vnpt.utils.Helper;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.ToastMessageUtil;
import com.vnpt.view.SignatureView;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.vnpt.common.Common.BUNDLE_KEY_HTML_SIGNED_SUCCESS;
import static com.vnpt.utils.Helper.hideSoftKeyboard;

public class SinatureManualActivity extends BaseActivity {
    public static String TAG = SinatureManualActivity.class.getName();
    SignatureView signView;
    File file;
    InvoiceCadmin mHoaDon;
    String pathFileSign;
    String htmltSuccess;
    EditText txtName;
    EditText txtNameRoomNo;
    private AwesomeProgressDialog dg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinature_manual);
        setupUI(findViewById(R.id.layout_sinature_manual));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtNameRoomNo = (EditText) findViewById(R.id.txtNameRoomNo);
        txtName = (EditText) findViewById(R.id.txtName);

        signView = (SignatureView) findViewById(R.id.viewCusSingature);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            mHoaDon = (InvoiceCadmin) args.getSerializable(Common.KEY_DATA_ITEM_INVOICE);
        }
        loadInforInputCustomer2();
        Helper.getInstance().showLog("mhoadon:" + mHoaDon.toString());
//        /data/data/com.vnpt.staffhddt/files/2016-06-22_sign.jpg
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(SinatureManualActivity.this);
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

    void loadInforInputCustomer2() {
        txtNameRoomNo.setText("");
        txtName.setText("");
    }

    void loadInforInputCustomer() {
        try {
            String roomNo = StoreSharePreferences.getInstance(
                    this).loadStringSavedPreferences(
                    Common.KEY_DATA_ROOM_NO);
            Helper.getInstance().showLog("xxxxxxxxxxxxxxxx" + roomNo);
            txtNameRoomNo.setText(roomNo);
            txtNameRoomNo.setEnabled(false);
            String cusName = StoreSharePreferences.getInstance(
                    this).loadStringSavedPreferences(
                    Common.KEY_DATA_NAME_CUS);
            Helper.getInstance().showLog("xxxxxxxxxxxxxxxx:" + cusName);
            txtName.setText(cusName);
            txtName.setEnabled(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_singnature_manual, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
//                actionSaveImageSingnatureManual();
                finish();
                return true;
            case R.id.action_save:
                // app icon in action bar clicked;
                dg = DialogUtils.showLoadingDialog(getString(R.string.text_proccessing), this,null);
                hideSoftKeyboard(SinatureManualActivity.this);
                updateCerfiticateInvoice();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void actionSaveImageSingnatureManual() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(c.getTime());

        signView.setDrawingCacheEnabled(true);
        signView.buildDrawingCache();
        signView.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(signView.getDrawingCache());
        //luu file
        pathFileSign = storeImage(bitmap);
        signView.setDrawingCacheEnabled(false);
        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_PATH_IMAGE_SIGN_MANUAL_RECENTLY, pathFileSign);
        Helper.getInstance().showLog("path2:" + pathFileSign);
        showDialogUpdate(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkConnection();
    }
    public String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.DEFAULT);
    }

    private String storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return "";
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
            return "";
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
            return "";
        }
        Helper.getInstance().showLog("path2:" + pictureFile.getAbsolutePath());
        return pictureFile.getAbsolutePath();
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/"
                + getApplicationContext().getPackageName()
                + "/SignManualHDDT");
        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_PATH_DEFAULT_IMAGE_SIGN_MANUAL, mediaStorageDir.getPath() + File.separator);

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
        String mImageName = "";
        mImageName = mHoaDon.getPattern().replace("/", "_") + mHoaDon.getSerial().replace("/", "_") + "_" + mHoaDon.getInvNum() + "_" + mHoaDon.getAmount() + ".png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        Helper.getInstance().showLog("path3:" + mediaFile.getAbsolutePath());
        return mediaFile;
    }

    //    void actionSaveImageSingnatureManual() {
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String date = sdf.format(c.getTime());
//
//        signView.setDrawingCacheEnabled(true);
//        signView.buildDrawingCache();
//        signView.buildDrawingCache(true);
//        Bitmap bitmap = Bitmap.createBitmap(signView.getDrawingCache());
//        int BUFFER_SIZE = 1024 * 8;
//        file = new File(Environment.getExternalStorageDirectory()+"/"+
//                Common.PACKAGE_NAME + "/" +"HDDTSign"+ "/"+ date
//                + "_sign.jpg");
//        try {
//            file.createNewFile();
//            FileOutputStream fos = new FileOutputStream(file);
//            final BufferedOutputStream bos = new BufferedOutputStream(fos,
//                    BUFFER_SIZE);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            bos.flush();
//            bos.close();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        bitmap.recycle();
//
//        Log.i("Path", "Path" + file.getAbsolutePath());
//        signView.setDrawingCacheEnabled(false);
//        showDialogUpdate(this);
//    }
    public void showDialogUpdate(final Context mContext) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(mContext.getString(R.string.message));
        alertDialog.setMessage(mContext.getString(R.string.txt_update_singature_customer_successful));
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setPositiveButton(mContext.getString(R.string.OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_PATH_IMAGE_SING_MANUAL,file.getAbsolutePath());
                        Intent returnIntent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putString(BUNDLE_KEY_HTML_SIGNED_SUCCESS, htmltSuccess);
                        returnIntent.putExtras(bundle);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                });

        alertDialog.show();
    }

    private static String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.encodeBase64(bytes).toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encodedfile;
    }

    protected void updateCerfiticateInvoice() {
        String xml = "";
        String textRoomNo = txtNameRoomNo.getText().toString();
        if (textRoomNo == null) {
            textRoomNo = " ";
        }
        StoreSharePreferences.getInstance(
                this).saveStringPreferences(
                Common.KEY_DATA_ROOM_NO, textRoomNo);
        String nameCus = txtName.getText().toString();
        if (nameCus == null) {
            nameCus = " ";

        }
        StoreSharePreferences.getInstance(
                this).saveStringPreferences(
                Common.KEY_DATA_NAME_CUS, nameCus);
        signView.setDrawingCacheEnabled(true);
        signView.buildDrawingCache();
        signView.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(signView.getDrawingCache());
        //Chuyen base64
        String strBase64 = convert(bitmap);
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
            bundle.putString(Common.BUNDLE_KEY_FKEY_INVOICE, mHoaDon.getFkey());
            bundle.putString(Common.BUNDLE_KEY_CHECKNO, mHoaDon.getInvNum());
            bundle.putString(Common.BUNDLE_KEY_PATTERN, mHoaDon.getPattern());
            bundle.putString(Common.BUNDLE_KEY_SERIAL, mHoaDon.getSerial());
            bundle.putString(Common.BUNDLE_KEY_XMLINVDATA, xmlString);
            e.viewData = bundle;
            UserController.getInstance().handleViewEvent(e);
        } else {
            if (dg != null) {
                //if (dg.isShowing())
                dg.hide();
            }
            ToastMessageUtil.showToastShort(this, getString(R.string.text_warning_sign_input_infor));
        }
    }

    /**
     * xu ly su kien tu model tra ve view
     *
     * @param modelEvent
     * @author: truonglt2
     * @return: void
     * @throws:
     */
    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
        ActionEvent act = modelEvent.getActionEvent();
        switch (act.action) {
            case ActionEventConstant.ACTION_UPDATE_SIGNED_INVOICE: {
                htmltSuccess = (String) modelEvent.getModelData();
                if (htmltSuccess != null) {
                    actionSaveImageSingnatureManual();
                }
                if (dg != null) {
                    //if (dg.isShowing())
                    dg.hide();
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

    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {
        ActionEvent act = modelEvent.getActionEvent();
        switch (act.action) {
            case ActionEventConstant.ACTION_UPDATE_SIGNED_INVOICE: {
                ToastMessageUtil.showToastShort(this, modelEvent.getModelMessage());
                // stopping swipe refresh.
                break;
            }


            default:
                break;
        }
//        ToastMessageUtil.showToastShort(this,"Lưu thông tin không thành công!");
        if (dg != null) {
            //if (dg.isShowing())
            dg.hide();
        }
    }
}
