package com.vnpt.staffhddtpos58;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.vnpt.common.Common;
import com.vnpt.common.ConstantsApp;
import com.vnpt.dto.User;
import com.vnpt.utils.DateTimeUtil;
import com.vnpt.utils.StoreSharePreferences;

import java.util.Locale;

/**
 * @Description: activity man hinh splash screen
 * @author:truonglt2
 * @since:Feb 7, 2014 4:24:04 PM
 * @version: 1.0
 * @since: 1.0
 */
public class WellcomeActivity extends Activity implements OnClickListener {
    public static String TAG = MainActivityFragment.class.getName();
    private static int SPLASH_TIME = 1000;
    int mTimerStep = 300;
    int timer = 1;
    LinearLayout layoutContainer;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_wellcome);
        layoutContainer = (LinearLayout) findViewById(R.id.layoutContainer);
        layoutContainer.setOnClickListener(this);
        Common.PACKAGE_NAME = getApplicationContext().getPackageName();
        Locale myLocale = new Locale("vi");
        Locale.setDefault(myLocale);
        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_LANGUAGE_DEFAULT, "vi");
//        try {
//            DatabaseHelp.getInstance(this).createDataBase();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_FINAL_ADDRESS_SERVER, ConstantsApp.URL_SERVER_SOAP);
        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_DEFAULT_PATTERN_INVOICES, Common.DEFAULT_PATTERN_INVOICES);
        StoreSharePreferences.getInstance(this).saveStringPreferences(Common.KEY_DEFAULT_SERIAL_INVOICES, Common.DEFAULT_SERIAL_INVOICES);
        StoreSharePreferences.getInstance(this).saveIntPreferences(Common.KEY_TYPE_SYS_MOBILE, ConstantsApp.TypeSysInvMobile.RECEIPT);

        String loadFromDate = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC_FROM);
        String loadToDate = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC_TO);
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        if (loadFromDate == null) {
            StoreSharePreferences.getInstance(this).saveStringPreferences(Common.REF_KEY_DATE_SYNC_FROM, dateTimeUtil.minDateOfMonth());
        }
        if (loadToDate == null) {
            StoreSharePreferences.getInstance(this).saveStringPreferences(Common.REF_KEY_DATE_SYNC_TO, dateTimeUtil.maxDateOfMonth());
        }
        startSplash();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == layoutContainer) {
            timer += SPLASH_TIME;
        }
    }

    public void startSplash() {
        // select background base on screen size
        Thread splashThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(mTimerStep);
                        timer += mTimerStep;
                        if ((timer > SPLASH_TIME) || (timer > 30 * SPLASH_TIME)) {
                            break;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        break;
                    }
                }
                boolean isRegister = StoreSharePreferences.getInstance(
                        WellcomeActivity.this).loadBooleandSavedPreferences(
                        Common.KEY_IS_LOGIN_INFOR_USER);
                if (isRegister) {
                    Gson gson = new Gson();
                    String strUser = StoreSharePreferences.getInstance(
                            WellcomeActivity.this).loadStringSavedPreferences(
                            Common.REFF_KEY_DATA_INFOR_USER_LOGIN);
                    User user = gson.fromJson(strUser, User.class);
                    if (user != null) {
                        Common.userInfo = user;
                        Intent main = new Intent(WellcomeActivity.this,
                                MainActivity.class);
                        startActivity(main);
                    } else {
                        Intent main = new Intent(WellcomeActivity.this,
                                LoginActivity.class);
                        startActivity(main);
                    }

                } else {
                    Intent main = new Intent(WellcomeActivity.this,
                            LoginActivity.class);
                    startActivity(main);
                }
                finish();
            }
        });
        splashThread.start();
    }
}
