package com.vnpt.printproject.woosim;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mocoo.hang.rtprinter.driver.HsBluetoothPrintDriver;
import com.vnpt.printproject.DeviceListActivity;
import com.vnpt.printproject.PrintReceipt;
import com.vnpt.printproject.StaticValue;
import com.vnpt.staffhddtpos58.R;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * @author shohrab.uddin, RONGTA
 *         This class is responsible to connect with a bluetooth printer
 */
public class BluetoothPrinterActivity extends AppCompatActivity {
    private static String address;
    private static final String TAG = "BloothPrinterActivity";
    private static BluetoothDevice device;
    public static Context CONTEXT;
    private AlertDialog.Builder alertDlgBuilder;

    private BluetoothAdapter mBluetoothAdapter = null;
    public static HsBluetoothPrintDriver BLUETOOTH_PRINTER = null;

    private static Button mBtnConnetBluetoothDevice = null;
    private static Button mBtnPrint = null;
    private static TextView txtPrinterStatus = null;
    private static ImageView mImgPosPrinter = null;

    public static BluetoothPrintService mPrintService = null;
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
    private static final int REQUEST_CONNECT_DEVICE = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "+++ ON CREATE +++");

        setContentView(R.layout.bluetooth_printer_activity);

        CONTEXT = getApplicationContext();
        alertDlgBuilder = new AlertDialog.Builder(BluetoothPrinterActivity.this);

        // Get device's Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not available in your device
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        //Initialize widgets
        InitUIControl();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.ab_bg_black);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void InitUIControl() {
        txtPrinterStatus = (TextView) findViewById(R.id.txtPrinerStatus);
        mBtnConnetBluetoothDevice = (Button) findViewById(R.id.btn_connect_bluetooth_device);
        mBtnConnetBluetoothDevice.setOnClickListener(mBtnConnetBluetoothDeviceOnClickListener);
        mBtnPrint = (Button) findViewById(R.id.btn_print);
        mBtnPrint.setOnClickListener(mBtnPrintOnClickListener);

        mImgPosPrinter = (ImageView) findViewById(R.id.printer_imgPOSPrinter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that to be enabled.
        // initializeBluetoothDevice() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (mPrintService == null) {
                // Initialize the BluetoothPrintService to perform bluetooth connections
                mPrintService = new BluetoothPrintService(mHandler);
            } else {
                if (mPrintService.getState() != BluetoothPrintService.STATE_CONNECTED) {
                    mImgPosPrinter.setImageResource(R.drawable.pos_printer_offliine);
                    StaticValue.isPrinterConnected = false;
                } else {
                    txtPrinterStatus.setText(R.string.title_connected_to);
                    txtPrinterStatus.append(device.getName());
                    StaticValue.isPrinterConnected = true;
                    mImgPosPrinter.setImageResource(R.drawable.pos_printer);
                    mBtnConnetBluetoothDevice.setText("Ngắt kết nối");
                }
            }
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D) Log.i(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was not enabled during onStart(),
        // so we were paused to enable it.
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.

        if (mPrintService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            // then start the bluetooth print services
            if (mPrintService.getState() == BluetoothPrintService.STATE_NONE) {
                mPrintService.start();
            }
        }
    }

//    @Override
//    public void onDestroy() {
//        if (D) Log.i(TAG, "--- ON DESTROY ---");
//        // Stop the Bluetooth print services
//        if (mPrintService != null) mPrintService.stop();
//        super.onDestroy();
//    }

    // The handler that gets information back from the BluetoothPrintService
    private final BluetoothHandler mHandler = new BluetoothHandler(this);

    /**
     * The Handler that gets information back from Bluetooth Devices
     */
    static class BluetoothHandler extends Handler {
        private final WeakReference<BluetoothPrinterActivity> myWeakReference;

        //Creating weak reference of BluetoothPrinterActivity class to avoid any leak
        BluetoothHandler(BluetoothPrinterActivity weakReference) {
            myWeakReference = new WeakReference<BluetoothPrinterActivity>(weakReference);
        }

        @Override
        public void handleMessage(Message msg) {
            BluetoothPrinterActivity bluetoothPrinterActivity = myWeakReference.get();
            if (bluetoothPrinterActivity != null) {
                switch (msg.what) {
                    case MESSAGE_DEVICE_NAME:
                        // save the connected device's name
                        String deviceName = msg.getData().getString(DEVICE_NAME);
                        txtPrinterStatus.setText(R.string.title_connected_to);
                        txtPrinterStatus.append(deviceName);
                        Toast.makeText(CONTEXT, "Connected to " + deviceName + " successful!", Toast.LENGTH_SHORT).show();
                        StaticValue.isPrinterConnected = true;
                        mImgPosPrinter.setImageResource(R.drawable.pos_printer);
                        mBtnConnetBluetoothDevice.setText("Ngắt kết nối");
                        break;
                    case MESSAGE_TOAST:
                        if(D)
                        Log.e("ket qua",""+msg.getData().getInt(TOAST));
                        Toast.makeText(CONTEXT, msg.getData().getInt(TOAST), Toast.LENGTH_SHORT).show();
                        break;
                }

            }

            ;
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
//        Log.d(TAG, "onActivityResult " + resultCode);
//        switch (requestCode) {
//            case REQUEST_CONNECT_DEVICE:
//                // When DeviceListActivity returns with a device to connect
//                if (resultCode == Activity.RESULT_OK) {
//                    // Get the device MAC address
//                    String address = data.getExtras()
//                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//                    // Get the BLuetoothDevice object
//                    device = mBluetoothAdapter.getRemoteDevice(address);
//                    // Attempt to connect to the device
//                    BLUETOOTH_PRINTER.start();
//                    BLUETOOTH_PRINTER.connect(device);
//                }
//                break;
//            case REQUEST_ENABLE_BT:
//                // When the request to enable Bluetooth returns
//                if (resultCode == Activity.RESULT_OK) {
//                    // Bluetooth is now enabled, so set up a chat session
//                    initializeBluetoothDevice();
//                } else {
//                    // User did not enable Bluetooth or an error occured
//                    Log.d(TAG, "BT not enabled");
//                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//        }
//    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (D) Log.d(TAG, "onActivityResult: " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up bluetooth connections
                    mPrintService = new BluetoothPrintService(mHandler);
                } else {
                    // User did not enable Bluetooth or an error occurred
                    if (D) Log.e(TAG, "BT is not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BLuetoothDevice object
        device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mPrintService.connect(device, secure);


    }

    OnClickListener mBtnQuitOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // Stop the Bluetooth chat services
            if (!PrintReceipt.printBillFromOrder(getApplicationContext())) {
                Toast.makeText(BluetoothPrinterActivity.this, "No printer is connected!!", Toast.LENGTH_LONG).show();
            }

        }

        ;
    };

    OnClickListener mBtnPrintOnClickListener = new OnClickListener() {
        public void onClick(View arg0) {
            actionPrinter();
        }
    };

    void actionPrinter()  {
        try {
            PrintReceipt.printReceiptDemoVNPT(this);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    OnClickListener mBtnConnetBluetoothDeviceOnClickListener = new OnClickListener() {
        Intent serverIntent = null;

        public void onClick(View arg0) {
            String text = mBtnPrint.getText().toString();
            if (text.equals("Ngắt kết nối")) {
                if (mPrintService != null) mPrintService.stop();
                mImgPosPrinter.setImageResource(R.drawable.pos_printer_offliine);
                mBtnPrint.setText(R.string.connect_bluetooth_device_btn);
            } else {
                //If bluetooth is disabled then ask user to enable it again
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                } else {//If the connection is lost with last connected bluetooth printer
                    if (mPrintService == null) {
                        // Initialize the BluetoothPrintService to perform bluetooth connections
                        mPrintService = new BluetoothPrintService(mHandler);
                    } else {
                        if (mPrintService.getState() != BluetoothPrintService.STATE_CONNECTED) {
                            mImgPosPrinter.setImageResource(R.drawable.pos_printer_offliine);
                            serverIntent = new Intent(BluetoothPrinterActivity.this, DeviceListActivity.class);
                            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                        } else {
                            txtPrinterStatus.setText(R.string.title_connected_to);
                            txtPrinterStatus.append(device.getName());
                            mImgPosPrinter.setImageResource(R.drawable.pos_printer);
//                        alertDlgBuilder.setTitle(getResources().getString(R.string.alert_title));
//                        alertDlgBuilder.setMessage(getResources().getString(R.string.alert_message));
//                        alertDlgBuilder.setNegativeButton(getResources().getString(R.string.alert_btn_negative), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                }
//                        );
//                        alertDlgBuilder.setPositiveButton(getResources().getString(R.string.alert_btn_positive), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        BLUETOOTH_PRINTER.stop();
//                                        serverIntent = new Intent(BluetoothPrinterActivity.this, DeviceListActivity.class);
//                                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
//                                    }
//                                }
//                        );
//                        alertDlgBuilder.show();
                        }
                    }
                }
            }


        }
    };


}
