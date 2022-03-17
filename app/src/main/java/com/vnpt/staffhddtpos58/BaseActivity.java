package com.vnpt.staffhddtpos58;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.vnpt.broadcast.ConnectivityChangeReceiver;
import com.vnpt.common.ConstantsApp;
import com.vnpt.common.ModelEvent;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.utils.DialogUtils;
import com.woosim.printer.WoosimService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description: lop activity co so
 * @author:truonglt2
 * @since:Feb 7, 2014 3:54:50 PM
 * @version: 1.0
 * @since: 1.0
 */
public class BaseActivity extends AppCompatActivity implements OnEventControlListener, ConnectivityChangeReceiver.ConnectivityReceiverListener {
    private static final String TAG = BaseActivity.class.getSimpleName();

    /**
     * Phan print
     */
    // Debugging
    private static final boolean D = true;

    // Message types sent from the BluetoothPrintService Handler
    public static final int MESSAGE_DEVICE_NAME = 1;
    public static final int MESSAGE_TOAST = 2;
    public static final int MESSAGE_READ = 3;

    // Key names received from the BluetoothPrintService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the print services
    private WoosimService mWoosim = null;


    // chuoi action cua cac broadcast message
    public static final String GGYOULOOK_ACTION = "viettel.com.bookstore.BROADCAST";

    // kiem tra activity da finish hay chua
    public boolean isFinished = false;
    MyTimerTask timerTask;
    Timer timer = new Timer();
    public static final int TIME_DELAY = 1000;
    // public static final int TIME_PERIOD = 1000 * 60 * 5;
    public static final int TIME_PERIOD = 1000 * 30;
    // Progress dialog type (0 - for Horizontal progress bar)
    // File url to download
    private static String file_url = "http://api.androidhive.info/progressdialog/hive.jpg";

    BroadcastReceiver receiver = new BroadcastReceiver() {
        /**
         * ham nhan receive broadcast
         *
         * @author: truonglt2
         * @param context
         * @param intent
         * @return:
         * @throws:
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getExtras().getInt(
                    ConstantsApp.ACTION_BROADCAST);
            int hasCode = intent.getExtras().getInt(
                    ConstantsApp.HASHCODE_BROADCAST);
            if (hasCode != BaseActivity.this.hashCode()) {
                receiveBroadcast(action, intent.getExtras());
            }
        }
    };

    /**
     * khoi tao activity
     *
     * @param savedInstanceState
     * @author: truonglt2
     * @return: BaseActivity
     * @throws:
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            IntentFilter filter = new IntentFilter(GGYOULOOK_ACTION);
            registerReceiver(receiver, filter);
        } catch (Exception e) {
        }

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        // this.broadcast = broadcast;

    }

    /**
     * Nhan cac broadcast
     *
     * @param action
     * @param bundle
     * @author: truonglt2
     * @return: void
     * @throws:
     */
    public void receiveBroadcast(int action, Bundle bundle) {
    }

    /**
     * huy activity
     *
     * @author: truonglt2
     * @return: BaseActivity
     * @throws:
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        unregisterReceiver(receiver);
        System.gc();
        System.runFinalization();
        super.onDestroy();
    }

    /**
     * ket thuc activity
     *
     * @author: truonglt2
     * @return: BaseActivity
     * @throws:
     */
    @Override
    public void finish() {
        isFinished = true;
        super.finish();
    }

    /**
     * finish activity
     *
     * @param requestCode
     * @author: truonglt2
     * @return: BaseActivity
     * @throws:
     */
    @Override
    public void finishActivity(int requestCode) {
        isFinished = true;
        super.finishActivity(requestCode);
    }

    /**
     * dung activity
     *
     * @author: truonglt2
     * @return: BaseActivity
     * @throws:
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    /**
     * resume trang thai cua activity
     *
     * @author: truonglt2
     * @return: BaseActivity
     * @throws:
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // register connection status listener
        App.getInstance().setConnectivityListener(this);
    }

    /**
     * This method is used to set value for members
     *
     * @author: truonglt2
     * @return: void
     * @throws:
     */
    public void setValue() {

    }

    /**
     * This method is used to set event for members
     *
     * @author: truonglt2
     * @return: void
     * @throws:
     */
    public void setEvent() {

    }

    /*
     * public void onBackPressed() { Log.d("CDA", "onBackPressed Called");
     * super.onBackPressed(); }
     */

    /**
     * xu ly su kien tu model tra ve view
     *
     * @param modelEvent
     * @author: truonglt2
     * @return: void
     * @throws:
     */
    public void handleModelViewEvent(ModelEvent modelEvent) {
        System.out.println("co di qua day");
    }

    /**
     * xu ly loi su kien tu model tra ve view
     *
     * @param modelEvent
     * @author: truonglt2
     * @return: void
     * @throws:
     */
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {
        switch (modelEvent.getModelCode()) {
//            case ErrorConstants.ERROR_COMMON: //
//                if (modelEvent.getActionEvent().action != //
//                        ActionEventConstant.ACTION_SYN_SYNDATA) {
//                    showDialog("Lỗi trong quá trình xử lý"); // } break;
//                }

        }
    }

    // Method to manually check connection status
    public void checkConnection() {
        boolean isConnected = ConnectivityChangeReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = getString(R.string.txt_internet_good);
//            color = Color.WHITE;
            DialogUtils.showWarningDialog(getBaseContext(), getString(R.string.message), message);
        }
//        } else {
            message = getString(R.string.txt_internet_bad);
////            color = Color.RED;
            DialogUtils.showInfoDialog(getBaseContext(), getString(R.string.message),message);
//        }
//        String message;
//        int color;
//        if (isConnected) {
//            message = "Good! Connected to Internet";
//            color = Color.WHITE;
//        } else {
//            message = "Sorry! Not connected to internet";
//            color = Color.RED;
//        }
//
//        Snackbar snackbar = Snackbar
//                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);
//
//        View sbView = snackbar.getView();
//        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(color);
//        snackbar.show();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    /**
     * @author: truonglt2
     * @since:Feb 7, 2014 1:25:51 AM
     * @Description: thread chay dialog
     */
    public class MyTimerTask extends TimerTask {
        public MyTimerTask() {
        }

        @Override
        public void run() {
            runThreadDialog();
        }
    }

    /**
     * khoi tao timestask de show dialog
     *
     * @author: truonglt2
     * @return: void
     * @throws:
     */

    void showDialogWithTimeTask() {
        timerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(timerTask, TIME_DELAY, TIME_PERIOD);
    }

    /**
     * thread hien thi dialog
     *
     * @author: truonglt2
     * @return: void
     * @throws:
     */
    void runThreadDialog() {
        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                        } catch (Exception e) {
                            stopTask();
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    /**
     * xu ly su kien phat sinh tu cac view
     *
     * @param eventType
     * @param control
     * @param data
     * @author: truonglt2
     * @return: BaseActivity
     * @throws:
     */
    @Override
    public void onEvent(int eventType, View control, Object data) {
        // TODO Auto-generated method stub
//		switch (eventType) {
//		case ActionEventConstant.ACTION_DOWN_LOAD:// down load book
//			if (mDialog.isShowing())
//				mDialog.dismiss();
//			break;
//		case ActionEventConstant.ACTION_CANCEL_DOWNLOAD:// cancel download
//			stopTask();
//			break;
//		case ActionEventConstant.ACTION_LATER_DOWNLOAD:// dismiss dialog and
//														// show dialog after 1
//														// minute
//			if (mDialog.isShowing())
//				mDialog.dismiss();
//			break;
//
//		default:
//			break;
//		}
    }

    /**
     * Mo ta chuc nang stop timeTask
     *
     * @author: truonglt2
     * @return: void
     * @throws:
     */
    public void stopTask() {
        if (timerTask != null) {
            Log.d("TIMER", "timer download canceled");
            timerTask.cancel();
        }
    }

    /**
     * This method is used to set fragment content
     *
     * @param fragment
     * @param tag
     */
    public void setFragmentContent(Fragment fragment, String tag, int idPlace) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(idPlace, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
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


}
