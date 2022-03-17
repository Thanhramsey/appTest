package com.vnpt.staffhddtpos58;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.common.ErrorConstants;
import com.vnpt.common.ModelEvent;
import com.vnpt.controller.UserController;
import com.vnpt.dto.Outlet;
import com.vnpt.listener.CustomListener;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.utils.DateTimeUtil;
import com.vnpt.utils.DialogUtils;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.ToastMessageUtil;
import com.vnpt.webservice.AppServices;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.vnpt.utils.Helper.hideSoftKeyboard;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements OnEventControlListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_STORAGE = 0;
    private static final int REQUEST_WRITE_STORAGE = 1;
    static String addressServer = new String();
    Locale myLocale;
    String currentLanguage;
    LinearLayout layout_no_service;
    private AwesomeProgressDialog dg;
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "demokh@vnpt.vn:123:2", "kh@vnpt.vn:123:2", "demo@vnpt.vn:123:1", "test@gmail.com:123:1", "truong@gmail.com:123:1"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button btnForgotPassword;
    private Button btnRetry;
    private ImageButton btn_pattern_serial;

    private Spinner spinner2;
    private LinearLayout emailLoginForm;
    int postionSelectedOutlet = 0;
    private ArrayList<Outlet> mArrOutlet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI(findViewById(R.id.id_parent_layout));
        mArrOutlet = new ArrayList<>();
        layout_no_service = (LinearLayout) findViewById(R.id.layout_no_service);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);
        btnRetry = (Button) findViewById(R.id.btn_retry);

        btn_pattern_serial = (ImageButton) findViewById(R.id.btn_pattern_serial);
        btnForgotPassword.setPaintFlags(btnForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
//                startActivity(intent);

            }
        });
        btn_pattern_serial.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreSharePreferences.getInstance(LoginActivity.this).saveIntPreferences(Common.KEY_FIRST_CONFIG,0);
                showDialogInputPatternSerial();
            }
        });
//        btnRetry.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //showAlertDialogserver();
//                //System.out.println("hahhahahahaha");
//                getData();
//            }
//        });
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        emailLoginForm = (LinearLayout) findViewById(R.id.email_login_form);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        spinner2 = (Spinner) findViewById(R.id.spinnerLanguage);
        currentLanguage = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.KEY_LANGUAGE_DEFAULT);
        if (currentLanguage == null || currentLanguage.equals("")) {
            currentLanguage = "vi";
        }
        // Manually checking internet connection
        checkConnection();
        //getDataOutlet();
        initMultilanguage();
        setAccountDefault();
        int firstConfig = StoreSharePreferences.getInstance(LoginActivity.this).loadIntegerSavedPreferences(Common.KEY_FIRST_CONFIG);
        if(firstConfig==0)
        {
            showDialogInputPatternSerial();
        }
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/"
                + this.getPackageName()
                + "/SignManualHDDT");
        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_PATH_DEFAULT_IMAGE_SIGN_MANUAL, mediaStorageDir.getAbsolutePath() + File.separator);
        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_PATH_DEFAULT_HTML_INVOICE, mediaStorageDir.getAbsolutePath() + File.separator);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkConnection();
        //getDataOutlet();
        App.getInstance().setConnectivityListener(this);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LoginActivity.this);
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


    void showDialogInputPatternSerial() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(getString(R.string.txt_input_pattern_serial));
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pattern_serial, null);
//        View viewInflated = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_input_money, false);
        final EditText edtServer = (EditText) dialogView.findViewById(R.id.edtAddressServer);
        edtServer.setText("" + StoreSharePreferences.getInstance(
                LoginActivity.this).loadStringSavedPreferences(
                Common.KEY_FINAL_ADDRESS_SERVER));

        final EditText edtPattern = (EditText) dialogView.findViewById(R.id.edtPattern);
        edtPattern.setText("" + StoreSharePreferences.getInstance(
                LoginActivity.this).loadStringSavedPreferences(
                Common.KEY_DEFAULT_PATTERN_INVOICES));
        final EditText edtSerial = (EditText) dialogView.findViewById(R.id.edtSerial);
        edtSerial.setText(StoreSharePreferences.getInstance(
                LoginActivity.this).loadStringSavedPreferences(
                Common.KEY_DEFAULT_SERIAL_INVOICES));
        builder.setView(dialogView);
        builder.setIcon(R.mipmap.ic_launcher);
        //2. now setup to change color of the button
        // Set up the buttons

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StoreSharePreferences.getInstance(
                        LoginActivity.this).saveStringPreferences(Common.KEY_DEFAULT_PATTERN_INVOICES, edtPattern.getText().toString());
                StoreSharePreferences.getInstance(
                        LoginActivity.this).saveStringPreferences(Common.KEY_DEFAULT_SERIAL_INVOICES, edtSerial.getText().toString());
                StoreSharePreferences.getInstance(
                        LoginActivity.this).saveStringPreferences(Common.KEY_FINAL_ADDRESS_SERVER, edtServer.getText().toString());
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StoreSharePreferences.getInstance(LoginActivity.this).saveIntPreferences(Common.KEY_FIRST_CONFIG,1);
                dialogInterface.cancel();
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
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
//                getDataOutlet();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(LoginActivity.this,dialog,edtServer,edtPattern,edtSerial ));
    }

    void showAlertDialogserver() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("Address Server");
        alertDialog.setMessage("Enter address server");
        addressServer = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(addressServer);
        alertDialog.setView(input);
        alertDialog.setIcon(R.mipmap.ic_launcher);

        alertDialog.setPositiveButton(getString(R.string.OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addressServer = input.getText().toString();
                        StoreSharePreferences.getInstance(LoginActivity.this).saveStringPreferences(Common.KEY_FINAL_ADDRESS_SERVER, addressServer);
//                        getData();
                        dialog.dismiss();
                    }
                });

        alertDialog.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    void initMultilanguage() {
        setLocale(currentLanguage);
        StoreSharePreferences.getInstance(LoginActivity.this).saveStringPreferences(Common.KEY_LANGUAGE_DEFAULT, "vi");
        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.txt_language_english));
        list.add(getString(R.string.txt_language_vietnamese));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        setLocale("en");
                        StoreSharePreferences.getInstance(LoginActivity.this).saveStringPreferences(Common.KEY_LANGUAGE_DEFAULT, "en");
                        break;
                    case 1:
                        setLocale("vi");
                        StoreSharePreferences.getInstance(LoginActivity.this).saveStringPreferences(Common.KEY_LANGUAGE_DEFAULT, "vi");
                        break;
                    default:
                        setLocale("en");
                        StoreSharePreferences.getInstance(LoginActivity.this).saveStringPreferences(Common.KEY_LANGUAGE_DEFAULT, "en");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @SuppressLint("ObsoleteSdkInt")
    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
//            res.updateConfiguration(conf, dm);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                conf.setLocale(myLocale);
//            } else {
//                conf.locale = myLocale;
//            }
//            conf.locale = myLocale;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                getApplicationContext().createConfigurationContext(conf);
//            } else {
            res.updateConfiguration(conf, dm);
//            }
//            Intent refresh = new Intent(this, LoginActivity.class);
//            startActivity(refresh);
//            finish();
        }

//        } else {
//            Toast.makeText(LoginActivity.this, getString(R.string.txt_language_already_selected), Toast.LENGTH_SHORT).show();
//        }
    }

//    public void onBackPressed() {
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
//        System.exit(0);
//    }

    void getDataOutlet() {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_GET_INFOR_OUTLET;
        e.sender = this;
        Bundle bundle = new Bundle();
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }

    void setAccountDefault() {
        String email = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.KEY_USER_NAME);
        String password = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.KEY_USER_PASS);
//        mEmailView.setText("congtrinhdothigliadmin");
//        mEmailView.setText("blaihccongthaadmin");
//        mPasswordView.setText("123456aA@");
        mEmailView.setText(email);
        mPasswordView.setText(password);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                        }
                    });
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }
        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
//        try {
//            if (mArrOutlet != null && mArrOutlet.size() > 0) {
//                String nameOutlet = mArrOutlet.get(postionSelectedOutlet-1).getName();
//                StoreSharePreferences.getInstance(
//                        LoginActivity.this).saveStringPreferences(
//                        Common.KEY_OUTLET_LOGIN, nameOutlet);
//                StoreSharePreferences.getInstance(
//                        this).saveStringPreferences(
//                        Common.KEY_LOGIN_USER_OUTLET, mArrOutlet.get(postionSelectedOutlet-1).getName());
//            } else {
//                getDataOutlet();
//            }
//        } catch (Exception ex) {
//
//        }
//        int firstConfig = StoreSharePreferences.getInstance(LoginActivity.this).loadIntegerSavedPreferences(Common.KEY_FIRST_CONFIG);
//        if(firstConfig==0)
//        {
//            DialogUtils.showInfoDialog(LoginActivity.this, getString(R.string.message), getString(R.string.text_input_pattern_serial));
//            return;
//        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if(show)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                dg =  DialogUtils.showLoadingDialog(getString(R.string.text_proccessing),LoginActivity.this, null);
            } else {
                dg =  DialogUtils.showLoadingDialog(getString(R.string.text_proccessing),LoginActivity.this, null);
            }
        }
        else
            {
                if (dg != null) {
                    //if (dg.isShowing())
                    dg.hide();
                }
            }

    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            AppServices appServices = new AppServices(LoginActivity.this);
            String result = appServices.login(mEmail, mPassword);
            if (result != null && !result.equals("ERR:1")) {
                try {
                    if(result.equals("ERR:3")){
                        StoreSharePreferences.getInstance(
                                LoginActivity.this).saveStringPreferences(
                                Common.KEY_USER_NAME, mEmail);
                        StoreSharePreferences.getInstance(
                                LoginActivity.this).saveStringPreferences(
                                Common.KEY_USER_PASS, mPassword);
                        StoreSharePreferences.getInstance(
                                LoginActivity.this).saveStringPreferences(
                                Common.KEY_TOKEN_LOGIN, result);
                        StoreSharePreferences.getInstance(
                                LoginActivity.this).saveStringPreferences(
                                Common.KEY_USER_LOGINLAST, DateTimeUtil.getCurrentDateTime().toString());
                        return Common.DATA_SUCCESS;
                    }
                    return Common.DATA_ERROR;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                return Common.DATA_INVALIDATE;
            }
            return Common.DATA_ERROR;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            mAuthTask = null;

            if (success == Common.DATA_SUCCESS) {
                StoreSharePreferences.getInstance(LoginActivity.this).saveIntPreferences(Common.KEY_FROM_WHICH_SCREEN,Common.STATUS_NOT_PAYMENT);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Common.KEY_FROM_WHICH_SCREEN, Common.STATUS_NOT_PAYMENT);
                intent.putExtra("", bundle);
                startActivity(intent);
                finish();
            } else if (success == Common.DATA_INVALIDATE) {
                ToastMessageUtil.showToastShort(LoginActivity.this, getString(R.string.action_sign_in_failed_account));
            } else {
                ToastMessageUtil.showToastShort(LoginActivity.this, getString(R.string.action_sign_in_failed));
            }
            showProgress(false);
            if (dg != null) {
                //if (dg.isShowing())
                dg.hide();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
            if (dg != null) {
                //if (dg.isShowing())
                dg.hide();
            }
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
        super.onEvent(eventType, control, data);
        switch (eventType) {
//            case ACTION_GET_INFOR_OUTLET: {
//                mArrOutlet  = (ArrayList<Outlet>) data;
//                List<String> list = new ArrayList<String>();
//                list.add(getString(R.string.txt_choose_outlet));
//                for (int i = 0; i < mArrOutlet.size(); i++) {
//                    list.add(mArrOutlet.get(i).getName());
//                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner1.setAdapter(adapter);
//            }
            default:
                break;
        }
    }

    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
        ActionEvent act = modelEvent.getActionEvent();

        switch (act.action) {
            case ActionEventConstant.ACTION_GET_INFOR_OUTLET: {
                mArrOutlet = (ArrayList<Outlet>) modelEvent.getModelData();
                if (mArrOutlet != null && mArrOutlet.size() > 0) {
                    List<String> list = new ArrayList<String>();
                    list.add(getString(R.string.txt_choose_outlet));
                    for (int i = 0; i < mArrOutlet.size(); i++) {
                        list.add(mArrOutlet.get(i).getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinner1.setAdapter(adapter);
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
            case ActionEventConstant.ACTION_GET_INFOR_OUTLET: {
                ToastMessageUtil.showToastShort(LoginActivity.this, getString(R.string.no_connect_to_server));
            }
            case ErrorConstants.ERROR_COMMON: {
                ToastMessageUtil.showToastShort(LoginActivity.this, getString(R.string.text_process_err_sys));
                break;
            }
            default:
                ToastMessageUtil.showToastShort(LoginActivity.this, getString(R.string.text_process_err_sys));
                break;
        }
        if (dg != null) {
            //if (dg.isShowing())
            dg.hide();
        }

    }

}

