package com.maple.appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.maple.appmanager.utils.SPUtils;

/**
 * 开机广播 监听者
 *
 * @author maple
 * @time 2019-06-24
 */
public class MyReceiver extends BroadcastReceiver {
    public static final String BOOT_KEY = "boot log";

    public MyReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SPUtils spUtils = new SPUtils();
        String msg = spUtils.getString(BOOT_KEY, "11");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            msg += "开机广播：" + System.currentTimeMillis() + " \n";
            spUtils.put(BOOT_KEY, msg);

            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

}