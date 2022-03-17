package com.vnpt.staffhddtpos58;

import android.app.Application;

import com.vnpt.broadcast.ConnectivityChangeReceiver;

/**
 * Created by Truonglt on 15/11/2019.
 * Copyright © 2019 VNPT DANANG. All rights reserved.
 */
public class App extends Application {
    private static App sInstance = null;

    /**
     * Get instance of app
     *
     * @return app
     */
    public static synchronized App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

    }

    public void setConnectivityListener(ConnectivityChangeReceiver.ConnectivityReceiverListener listener) {
        ConnectivityChangeReceiver.connectivityReceiverListener = listener;
    }

}
