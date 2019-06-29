package com.maple.appmanager.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.text.TextUtils

/**
 * @author maple
 * @time 2019-06-24
 */
object PackageUtils {

    /**
     * 根据包名打开app
     */
    fun doStartAppWithPackageName(context: Context, packageName: String) {
        val className = getStartActivityName(context, packageName)
        if (!TextUtils.isEmpty(className)) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.component = ComponentName(packageName, className!!)
            context.startActivity(intent)
        }
    }

    /**
     * 获取app启动页名称
     */
    fun getStartActivityName(context: Context, packageName: String): String? {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        var className: String? = null
        try {
            val packageManager = context.packageManager
            val packageinfo = packageManager.getPackageInfo(packageName, 0)
            if (packageinfo != null) {
                // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
                val resolveIntent = Intent(Intent.ACTION_MAIN, null)
                resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                resolveIntent.setPackage(packageinfo.packageName)

                // 通过getPackageManager()的queryIntentActivities方法遍历
                val resolveinfoList = packageManager.queryIntentActivities(resolveIntent, 0)

                val resolveinfo = resolveinfoList.iterator().next()
                if (resolveinfo != null) {
                    // packagename = 参数packname
                    val appName = resolveinfo.activityInfo.packageName
                    // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
                    className = resolveinfo.activityInfo.name
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return className
    }

}
