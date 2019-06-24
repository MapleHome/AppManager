package com.maple.appmanager;

import android.app.Application;

/**
 * @author maple
 * @time 2019-06-24
 */
public class ManagerApp extends Application {

    private static ManagerApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static ManagerApp getApp() {
        return app;
    }

}
