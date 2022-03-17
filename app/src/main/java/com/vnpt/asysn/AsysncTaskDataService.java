package com.vnpt.asysn;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.google.gson.Gson;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.dto.InvoiceCadminBL;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.staffhddtpos58.R;
import com.vnpt.utils.DateTimeUtil;
import com.vnpt.utils.DialogUtils;
import com.vnpt.utils.StoreInvoiceSharePreferences;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.webservice.AppServices;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * lop down load file (interface)
 *
 * @author: truonglt2
 * @version: 1.0
 * @since: 1.0
 */
public class AsysncTaskDataService extends AsyncTask<String, String, Boolean> {
    /**
     * khoi tao contructor
     *
     * @author: truonglt2
     * @version: 1.0
     * @since: 1.0
     */
    CharSequence contentTitle;
    CharSequence tickerText;
    CharSequence contentText;
    JSONArray mJsonArray;
    OnEventControlListener mListener;
    private AwesomeProgressDialog dg;
    private int mTotalRecords = 15;
    private int countSaved = 0;
    int icon;
    long time;
    PendingIntent contentIntent;
    InvoiceCadminBL mFirstResult;
    NotificationManager notificationManager;
    Notification notification;
    int HELLO_ID = 1;

    /**
     * ham thuc hien down notification
     *
     * @author: truonglt2
     * @return: void
     * @throws:
     */
    @SuppressWarnings("deprecation")
    public void downloadNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        notificationManager = (NotificationManager) mContext
                .getSystemService(ns);
        icon = R.mipmap.ic_launcher;
        // the text that appears first on the status bar
        tickerText = "Progressing...";
        time = System.currentTimeMillis();

        notification = new Notification(icon, tickerText, time);

        // the bold font
        contentTitle = "Tiến trình tải dữ liệu biên lai đang xử lý...";
        // the text that needs to change
        contentText = "0% complete";
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setType("audio/*");
        contentIntent = PendingIntent.getActivity(mContext, 0,
                notificationIntent, 0);
        notificationManager.notify(HELLO_ID, notification);

    }

    public AsysncTaskDataService(Context context, OnEventControlListener listener) {
        mContext = context;
        mListener = listener;
//        dialog = new ProgressDialog(mContext) ;
    }

    Context mContext;
//    ProgressDialog dialog;

    protected void onPreExecute() {
        dg = DialogUtils.showLoadingDialog(mContext.getString(R.string.text_loading_data_invoice), mContext, null);
        downloadNotification();
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage(mContext.getResources().getString(R.string.text_proccessing));
//        dialog.show();
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                onCancelled();
//            }
//        });
    }

    @Override
    protected Boolean doInBackground(String... params) {
        //release list view
        if (this.isCancelled()) {
            return false;
        }
        try {
//            MetropoleServices service = new MetropoleServices(mContext);
//            JSONArray jsonArray = service.requestListOutletName();
//            if(jsonArray==null||jsonArray.length()==0)
//            {
//                return  false;
//            }
//            else
//                {
//                    mJsonArray = jsonArray;
//                    return true;
//                }
            //            String mSerial =  "HC-19E";
            //              String mPattern =
            String mPattern = StoreSharePreferences.getInstance(mContext).loadStringSavedPreferences(Common.KEY_DEFAULT_PATTERN_INVOICES);
            String mSerial = StoreSharePreferences.getInstance(mContext).loadStringSavedPreferences(Common.KEY_DEFAULT_SERIAL_INVOICES);
            String fromDate = StoreSharePreferences.getInstance(mContext).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC_FROM);
            String toDate = StoreSharePreferences.getInstance(mContext).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC_TO);
            //page đầu tiên là 0
            int pageIndex = 0;
            int pageSize = Common.PAGE_SIZE;
            int orgId = 0;
            AppServices service = new AppServices(mContext);
            System.out.println("Đầu vào:" + mPattern + " + " + mSerial + " + " + fromDate + " + " + toDate + " + " + pageIndex + " + " + pageSize + " + " + orgId);
            InvoiceCadminBL result = service.listInvoice(fromDate, toDate, mPattern, mSerial, orgId, pageSize, pageIndex);
            if (result.getStatusCode() == 0) {
                return false;
            } else //result.getStatusCode()==1
            {
                if (result.getArrData() != null) {
                    mFirstResult = result;

                    saveToDatabaseHelper(result.getArrData());
                }
                mTotalRecords = result.totalRecords;
                int loop = 0;
                loop = (mTotalRecords / pageSize);
                if ((mTotalRecords % pageSize) > 0) {
                    loop += 1;
                }
                for (int i = 0; i < loop; i++) {
                    publishProgress("" + (int) (i * 100 / loop));
                    pageIndex += 1;
                    Log.d("SAVE INVOICE loop", "" + pageIndex);
                    Log.d("SAVE INVOICE pageIndex", "" + pageIndex);
                    result = service.listInvoice(fromDate, toDate, mPattern, mSerial, orgId, pageSize, pageIndex);
                    if (result.getArrData() != null) {
                        saveToDatabaseHelper(result.getArrData());
                    }
                }
                String dateCurr = DateTimeUtil.getCurrentDate();
                StoreSharePreferences.getInstance(mContext).saveStringPreferences(Common.REF_KEY_DATE_SYNC, dateCurr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    void saveToDatabaseHelper(ArrayList<InvoiceCadmin> arrayList) {
        // convert User object user to JSON format
        Gson gson = new Gson();
        for (int i = 0; i < arrayList.size(); i++) {
            String invoice_json = gson.toJson(arrayList.get(i));
            String id = "" + arrayList.get(i).getFkey(); // get storage key
            StoreInvoiceSharePreferences.getInstance(mContext).saveStringPreferences(id, invoice_json);
            Log.d("SAVE INVOICE", "" + id);
            Log.d("SAVE INVOICE", "count:" + countSaved++);

        }
    }

    @Override
    public void onProgressUpdate(String... progress) {
        contentText = Integer.parseInt(progress[0]) + "% complete";
        notificationManager.notify(HELLO_ID, notification);
        super.onProgressUpdate(progress);
    }

    @Override
    protected void onPostExecute(final Boolean success) {
//        if (dialog.isShowing())
//            //dismiss dialog loading
//            dialog.dismiss();
//        if (success) {
////            fillSpaDetailForList(arrDetail);
//            mListener.onEvent(ActionEventConstant.ACTION_GET_INFOR_OUTLET, null, mJsonArray);
//        }
//        else
//            {
//                dialog.dismiss();
//            }
        if (success) {
            mListener.onEvent(ActionEventConstant.ACTION_GET_DATA_INVOICE_SYNC_SUCESSS, null, mFirstResult);
        } else {
            mListener.onEvent(ActionEventConstant.ACTION_GET_DATA_INVOICE_SYNC_FAILED, null, null);
        }
        if (dg != null) {
            //if (dg.isShowing())
            dg.hide();
        }
        onCancelled();

    }
}
