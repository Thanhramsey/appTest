package com.vnpt.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

class CheckConnectionService extends IntentService {


    public CheckConnectionService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
      Bundle extras = intent.getExtras();
      boolean isNetworkConnected = extras.getBoolean("isNetworkConnected");
      // your code

   }

}