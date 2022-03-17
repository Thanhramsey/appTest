package com.vnpt.staffhddtpos58;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;
import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.common.ErrorConstants;
import com.vnpt.common.ModelEvent;
import com.vnpt.controller.UserController;
import com.vnpt.dto.Term;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.printproject.pos58bus.BluetoothService;
import com.vnpt.printproject.pos58bus.DeviceListActivity;
import com.vnpt.staffhddtpos58.dialogs.SingleChoiceDialog;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.ToastMessageUtil;

import java.util.ArrayList;

import static com.vnpt.utils.Helper.hideSoftKeyboard;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SingleChoiceDialog.OnDialogSelectorListener, DatePickerDialog.OnDateSetListener, View.OnClickListener, OnEventControlListener {
    public static final String TAG = MainActivity.class.getName();
    Menu mMenu;
    public static FragmentManager fragmentManager;
    // them calendar item
    MenuItem searchItem, calendarItem;
    TextView txtName, txtPhone, txtPosition;
    public TextView txtCountLocaion;
    public LinearLayout layoutProccessing;
    public static int TYPE_CHOOSE = 0;
    FloatingActionButton btnScan;
    SmoothDateRangePickerFragment mSmoothDateRangePickerFragment;

    /******************************************************************************************************/
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    /*******************************************************************************************************/

    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    //xuat bien lai
    public static com.vnpt.printproject.pos58bus.BluetoothService mPOSPrinter = null;
    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            TYPE_CHOOSE = bundle.getInt(Common.KEY_DATA_ITEM_INVOICE);
        fragmentManager = getSupportFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
//        btnAddCustomer = (FloatingActionButton) findViewById(R.id.btn_add_customer);
//        btnAddCustomer.setOnClickListener(this);
        //xuat bien lai
        btnScan = (FloatingActionButton) findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        txtName = (TextView) header.findViewById(R.id.txt_name);
        txtPhone = (TextView) header.findViewById(R.id.txt_phone);
        txtPosition = (TextView) header.findViewById(R.id.txt_position);
        txtCountLocaion = (TextView) findViewById(R.id.txtCountLocaion);
        layoutProccessing = (LinearLayout) findViewById(R.id.layout_proccessing);
        //Restore the fragment's state here
        navigationView.getMenu().clear(); //clear old inflated items.
        navigationView.inflateMenu(R.menu.activity_main_drawer_staff);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
        try {
            String name = StoreSharePreferences.getInstance(
                    this).loadStringSavedPreferences(
                    Common.KEY_USER_FULLNAME);
            if (name == null || name.equals("") || name.equals("null")) {
                name = StoreSharePreferences.getInstance(
                        this).loadStringSavedPreferences(
                        Common.KEY_USER_NAME);
            }
            txtName.setText("" + name);
            txtPhone.setText("" + StoreSharePreferences.getInstance(
                    this).loadStringSavedPreferences(
                    Common.KEY_USER_LOGINLAST));
            txtPosition.setText("" + StoreSharePreferences.getInstance(
                    this).loadStringSavedPreferences(
                    Common.KEY_LOGIN_USER_OUTLET));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        showHome(Common.STATUS_NOT_PAYMENT);

        //xuat bien lai
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /****************************************************************************************************/
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case com.vnpt.printproject.pos58bus.BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "Đã kết nối",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case com.vnpt.printproject.pos58bus.BluetoothService.STATE_CONNECTING:
                            Toast.makeText(getApplicationContext(), "Đang kết nối",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case com.vnpt.printproject.pos58bus.BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Toast.makeText(getApplicationContext(), "Đang lắng nghe",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MainActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.mMenu = menu;
        searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
        calendarItem = menu.findItem(R.id.action_calendar);
        calendarItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onResume() {
        int paymented = StoreSharePreferences.getInstance(this).loadIntegerSavedPreferences(Common.KEY_FROM_WHICH_SCREEN);
//        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_DATA_ROOM_NO, "");
//        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_DATA_NAME_CUS, "");
        showHome(paymented);
        super.onResume();
    }

    /**
     * On selecting action bar icons
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:
                // search action
                return true;
//            case R.id.action_location_found:
//                // action Map
//                showProccessbar(true);
//                showContentMain(isShowFragmentMap);
//                isShowFragmentMap = !isShowFragmentMap;
////                showActivityMap();
//                return true;
            case R.id.action_calendar: {
                // xuat bien lai, an calendar
//                if (mSmoothDateRangePickerFragment != null && mSmoothDateRangePickerFragment.isVisible()) {
//                    return true;
//                }
//                Calendar calendar = Calendar.getInstance();
//                mSmoothDateRangePickerFragment = DialogUtils.showFromDayToDayPickerDialogEvent(getFragmentManager(),
//                        null, calendar, "Chọn từ ngày đến ngày", this);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showProccessbar(boolean isShow) {
        if (isShow) {
            layoutProccessing.setVisibility(View.VISIBLE);
        } else {
            layoutProccessing.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
        int id = item.getItemId();
        if (id == R.id.nav_statistics) {
            // Handle action to Home
            showStatictis();
        } else if (id == R.id.nav_home) {
            showHome(1);
        } else if (id == R.id.nav_guide) {
            Intent intent = new Intent(MainActivity.this, GuideUserActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
//            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            Intent intent = new Intent(MainActivity.this, ConfigSettingActivity.class);
            startActivity(intent);
        } else if ( id == R.id.nav_reprint) {
            checkUserAuthen();
        }
        if (id == R.id.nav_exit) {
            showDiaLogExit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkUserAuthen() {
        String userName = StoreSharePreferences.getInstance(
                this).loadStringSavedPreferences(
                Common.KEY_USER_NAME);

        if (!TextUtils.isEmpty(userName)) {
            if (userName.equals("ttvhttpleikuadmin")) {
                InLaiFragment fragment = new InLaiFragment();
                setFragmentContent(fragment, InLaiFragment.TAG, R.id.root_layout);
            } else {
                Toast.makeText(this, "Bạn không đủ quyền truy cập", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Bạn không đủ quyền truy cập", Toast.LENGTH_SHORT).show();
        }
    }

    void showStatictis() {
//        Intent intent = new Intent(this, StatictisActivity.class);
//        startActivity(intent);
        StatictisFragment fragment = new StatictisFragment();
        setFragmentContent(fragment, StatictisFragment.TAG, R.id.root_layout);
    }

    public void actionLoadAllTerm() {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_GET_ALL_TERM;
        e.sender = this;
        Bundle bundle = new Bundle();
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }

    @Override
    public void onStart() {
        super.onStart();

        // If Bluetooth is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        } else {
            if (mPOSPrinter == null)
                KeyListenerInit();
        }
    }

    private void KeyListenerInit() {
        mPOSPrinter = new com.vnpt.printproject.pos58bus.BluetoothService(this, mHandler);
    }

    void showDiaLogExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.message));
        builder.setMessage(getString(R.string.txt_sign_out_app))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StoreSharePreferences.getInstance(
                                MainActivity.this).saveBooleanPreferences(
                                Common.KEY_IS_LOGIN_INFOR_USER, false);
                        actionShowLoginScreen();
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

    public void actionShowLoginScreen() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

//        if (!searchView.isIconified()) {
//            searchView.setIconified(true);
//            GeneralsUtils.forceHideKeyboard(MainActivity.this);
//            searchItem.collapseActionView();
//            searchView.onActionViewCollapsed();
//            showDataSearch("");
////            findViewById(R.id.default_title).setVisibility(View.VISIBLE);
//            return;
//        }
        showDiaLogExit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
    }

    void showHome(int paymented) {
//        StoreSharePreferences.getInstance(this).saveIntPreferences(Common.KEY_FROM_WHICH_SCREEN, paymented);
//        if (paymented == Common.STATUS_NOT_PAYMENT) {
//            setTitle(getString(R.string.txt_list_table));
//            String fromDate = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC_FROM);
//            String toDate = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC_TO);
//            getSupportActionBar().setSubtitle(fromDate + " đến " + toDate);
//        } else {
//            setTitle(getString(R.string.txt_list_table_no_signed));
//            String fromDate = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC_FROM);
//            String toDate = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC_TO);
//            getSupportActionBar().setSubtitle(fromDate + " đến " + toDate);
//        }
//        MainActivityFragment fragment = new MainActivityFragment();
//        Bundle args = new Bundle();
//        args.putInt(Common.BUNDLE_ITEM_STATUS_PAYMENTED_INVOICE, paymented);
//        fragment.setArguments(args);
//        setFragmentContent(fragment, MainActivityFragment.TAG, R.id.root_layout);

        // chuyen sang man hinh xuat bien lai
        XuatBienLaiFragment fragment = new XuatBienLaiFragment();
        setFragmentContent(fragment, XuatBienLaiFragment.TAG, R.id.root_layout);
    }

    void setTitleHome() {
        int paymented = StoreSharePreferences.getInstance(this).loadIntegerSavedPreferences(Common.KEY_FROM_WHICH_SCREEN);
        if (paymented == Common.STATUS_NOT_PAYMENT) {
            setTitle(getString(R.string.txt_list_table));
            String fromDate = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC_FROM);
            String toDate = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC_TO);
            getSupportActionBar().setSubtitle(fromDate + " đến " + toDate);
        } else {
            setTitle(getString(R.string.txt_list_table_no_signed));
            String fromDate = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC_FROM);
            String toDate = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC_TO);
            getSupportActionBar().setSubtitle(fromDate + " đến " + toDate);
        }
    }


    @Override
    public void onSelectedOption(int typeSelected, int dialogView) {
        switch (dialogView) {
            case Common.DIALOG_SINGLE_SORT_INVOICE: {

                boolean sortBy = false;
                if (typeSelected == 0)//decs
                {
                    MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
                    fragment.actionSort(false);
                    sortBy = false;
                } else { // asc
                    MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
                    fragment.actionSort(true);
                    sortBy = true;
                }
                StoreSharePreferences.getInstance(
                        this).saveBooleanPreferences(
                        Common.REF_KEY_TYPE_SORT, sortBy);
                break;
            }
            default: {
                break;
            }
        }
    }

    // Call Back method  to get the Message form other Activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2

        switch (requestCode) {
            case Common.REQUEST_CODE_ACTIVITY_DETAILS_INVOICE: {
                if (resultCode == RESULT_OK) {
                    MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
                    Common.currentPageInvoice = 0;
                    fragment.actionShowListTable();
                }
                break;
            }

            case Common.REQUEST_CODE_ACTIVITY_DETAILS_FOR_RETURN_INVOICE: {
                if (resultCode == RESULT_OK) {
                    MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
                    Common.currentPageInvoice = 0;
                    fragment.actionShowListTable();
                }
                break;
            }

            case REQUEST_CONNECT_DEVICE:{
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(
                            com.vnpt.printproject.pos58bus.DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    if (BluetoothAdapter.checkBluetoothAddress(address)) {
                        BluetoothDevice device = mBluetoothAdapter
                                .getRemoteDevice(address);
                        // Attempt to connect to the device
                        mPOSPrinter.connect(device);
                    }
                }
                break;
            }
            case REQUEST_ENABLE_BT:{
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a session
                    KeyListenerInit();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
            default:
                break;
        }
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
            case ActionEventConstant.ACTION_GET_ALL_TERM: {
                ArrayList<Term> arrHoaDon = (ArrayList<Term>) modelEvent
                        .getModelData();
                if (arrHoaDon != null) {
                    CharSequence[] arrItems = new CharSequence[arrHoaDon.size()];
                    for (int i = 0; i < arrHoaDon.size(); i++) {
                        arrItems[i] = arrHoaDon.get(i).getName_term();
                    }
                    actionShowDialogChooseTerm(arrItems);
                } else {
                    actionLoadAllTerm();
                }
                break;
            }

            default:
                break;
        }

    }

    public void actionShowDialogChooseTerm(final CharSequence[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Chọn kỳ cước thanh toán");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                int idterm = item + 1;
                StoreSharePreferences.getInstance(
                        MainActivity.this).saveIntPreferences(
                        Common.REFF_KEY_DATA_TERM, idterm);
                dialog.dismiss();
                showHome(Common.STATUS_NOT_PAYMENT);
            }
        });
        builder.show();
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
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST: {
                ToastMessageUtil.showToastShort(MainActivity.this, getString(R.string.text_no_data));
            }
            break;
            case ActionEventConstant.ACTION_GET_ALL_TERM: {
                ArrayList<Term> arrTerm = new ArrayList<>();
                for (int i = 1; i < 5; i++) {
                    Term term = new Term();
                    term.setId_term(i);
                    term.setName_term("Tháng " + i);
                    arrTerm.add(term);
                }
                if (arrTerm != null) {
                    CharSequence[] arrItems = new CharSequence[arrTerm.size()];
                    for (int i = 0; i < arrTerm.size(); i++) {
                        arrItems[i] = arrTerm.get(i).getName_term();
                    }
                    actionShowDialogChooseTerm(arrItems);
                } else {
                    actionLoadAllTerm();
                }
            }
            break;
            case ErrorConstants.ERROR_COMMON: {
                ToastMessageUtil.showToastShort(MainActivity.this, getString(R.string.text_process_err_sys));
            }
            break;
            default:
                ToastMessageUtil.showToastShort(MainActivity.this, getString(R.string.text_process_err_sys));
                break;
        }
    }

    void startActivityCustomer() {
        Intent intent = new Intent(MainActivity.this, AddUpdateInforCustomerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.d("xxxxxxxx", i + " " + i1 + " " + i2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btn_add_customer:
//                // do something
//                startActivityCustomer();
//                break;
            case R.id.btn_scan: {
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
        switch (eventType) {
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_DATE: {
                int paymented = StoreSharePreferences.getInstance(this).loadIntegerSavedPreferences(Common.KEY_FROM_WHICH_SCREEN);
//        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_DATA_ROOM_NO, "");
//        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_DATA_NAME_CUS, "");
                showHome(paymented);
                break;
            }
        }
    }
}
