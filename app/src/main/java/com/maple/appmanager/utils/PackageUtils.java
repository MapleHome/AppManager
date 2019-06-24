package com.maple.appmanager.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * @author maple
 * @time 2019-06-24
 */
public class PackageUtils {

    /**
     * 获取app启动页名称
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getStartActivityName(Context context, String packageName) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        String className = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageinfo = packageManager.getPackageInfo(packageName, 0);
            if (packageinfo != null) {
                // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
                Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
                resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                resolveIntent.setPackage(packageinfo.packageName);

                // 通过getPackageManager()的queryIntentActivities方法遍历
                List<ResolveInfo> resolveinfoList = packageManager.queryIntentActivities(resolveIntent, 0);

                ResolveInfo resolveinfo = resolveinfoList.iterator().next();
                if (resolveinfo != null) {
                    // packagename = 参数packname
                    String appName = resolveinfo.activityInfo.packageName;
                    // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
                    className = resolveinfo.activityInfo.name;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return className;
    }

}
