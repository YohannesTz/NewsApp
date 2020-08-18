package com.yohannes.app.dev.newsapp.util;

import android.app.Application;

public class NewsApp extends Application {
    private static NewsApp nInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        nInstance = this;
    }

    public static synchronized NewsApp getInstance() {
        return nInstance;
    }

    public void setConnectivityListner(ConnectivityReceiver.ConnectivityReceiverListener listner) {
        ConnectivityReceiver.connectivityReceiverListener = listner;
    }
}
