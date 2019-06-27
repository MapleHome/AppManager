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
        String msg = spUtils.getString(BOOT_KEY, "广播日志：\n");
        String action = intent.getAction();
        // 设备重启
        if ("android.intent.action.ACTION_REBOOT".equals(action)) {
            msg += "设备重启：" + System.currentTimeMillis() + "\n";
        }
        // 屏幕开启
        if ("android.intent.action.ACTION_SCREEN_ON".equals(action)) {
            msg += "屏幕开启：" + System.currentTimeMillis() + "\n";
        }
        // 用户唤醒设备
        if ("android.intent.action.ACTION_USER_PRESENT".equals(action)) {
            msg += "用户唤醒设备：" + System.currentTimeMillis() + "\n";
        }
        // 设备启动完成，only one
        if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
            msg += "设备启动完成 ：" + System.currentTimeMillis() + "\n";
            toMainPage(context, false);
        }
        // ACTION_SHUTDOWN
        if ("android.intent.action.ACTION_SHUTDOWN".equals(action)) {
            msg += "ACTION_SHUTDOWN ：" + System.currentTimeMillis() + "\n";
            toMainPage(context, true);
        }
        // MEDIA_MOUNTED
        if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
            msg += "MEDIA_MOUNTED ：" + System.currentTimeMillis() + "\n";
        }
        spUtils.put(BOOT_KEY, msg);

    }

    private void toMainPage(Context context, boolean closed) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MainActivity.CLOSED, closed);
        context.startActivity(intent);
    }

}