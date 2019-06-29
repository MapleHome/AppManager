package com.maple.appmanager.ui

import android.content.pm.PackageManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maple.appmanager.data.App
import com.maple.appmanager.utils.VirtualTerminal
import java.util.*

/**
 *
 * @author maple
 * @time 2019-06-27
 */
class HomeViewModel : ViewModel() {
    val showInfo = MutableLiveData<String>()
    val canRemove = MutableLiveData(true)
    val appDatas = MutableLiveData<List<App>>()

    companion object {
        // Config info
        val appNames = arrayListOf("LeSo", "LeSports", "LeStore", "LeVideo", "StvGallery", "StvWeather")
        // const val startAppID = "com.example.androidx.viewpager2"
        const val app1Name = "AppStarter_v1"
        const val app2Name = "dangbeimarket419znds"
    }

    /**
     * 刷新信息
     */
    fun refreshInfo() {
        val infoStr = ("系统信息: \n"
                + "Model: " + android.os.Build.MODEL + ",\n"
                + "SDK: " + android.os.Build.VERSION.SDK + ",\n"
                + "版本：" + android.os.Build.VERSION.RELEASE + "\n")
        showInfo.value = infoStr
    }

    /**
     * 获取已经安装的app列表
     */
    fun getInstallAppList(pm: PackageManager) {
        appDatas.value = pm.getInstalledPackages(0)
                .filter {
                    it.packageName.contains("Le") ||
                            it.packageName.contains("Stv") ||
                            it.packageName.contains("maple")
                }
                .map {
                    val logo = it.applicationInfo.loadIcon(pm)
                    val appName = it.applicationInfo.loadLabel(pm).toString()
                    val version = "V ${it.versionName} - build ${it.versionCode}"
                    val packageName = it.packageName
                    App(logo, appName, version, packageName)
                }
    }

    /**
     * 移动到System目录，将普通app变为系统app
     */
    fun moveToSystem(appName: String = app1Name, appName2: String = app2Name) {
        val cmdList = arrayListOf(
                "su",
                "mount -o rw,remount /system",
                "cp -r /storage/external_storage/sda1/$appName.apk /system/app/$appName.apk",
                "chmod 777 /system/app/$appName.apk",
                "pm install /storage/external_storage/sda1/$appName2.apk"
        )
        exeCommands(cmdList).apply {
            if (this) {
                showToast("移动 $appName 成功！")
            } else {
                showToast("移动 $appName 失败！")
            }
        }
    }

    /**
     * 一键安装多个应用
     */
    fun addSingleApp() {
        val cmdList = ArrayList<String>()
        cmdList.add("su")//root权限
        cmdList.add("mount -o rw,remount /system")//获取system权限
        for (appName in appNames) {
            cmdList.add("mv /system/app/$appName.apk_copy /system/app/$appName.apk")
        }
        exeCommands(cmdList).apply {
            if (this) {
                showToast("恢复所有成功！")
            } else {
                showToast("恢复所有失败！")
            }
        }
        canRemove.value = true
    }

    /**
     * 一键删除
     */
    fun removeSingleApp() {
        val cmdList = ArrayList<String>()
        cmdList.add("su")//root权限
        cmdList.add("mount -o rw,remount /system")//获取system权限
        for (appName in appNames) {
            cmdList.add("mv /system/app/$appName.apk /system/app/$appName.apk_copy")
        }
        exeCommands(cmdList).apply {
            if (this) {
                showToast("删除所有成功！")
            } else {
                showToast("删除所有失败！")
            }
        }
        canRemove.value = false
    }


    /**
     * 执行CMD命令
     */
    private fun exeCommands(commands: List<String>): Boolean {
        var log = ""
        var retAll = true
        val startTime = System.currentTimeMillis()
        try {
            val vt = VirtualTerminal("su")
            for (cmd in commands) {
                val r = vt.runCommand(cmd)
                val removeSingle = r.success()
                log += "$cmd   执行成功? $removeSingle! \n"
                retAll = retAll && removeSingle
            }
            vt.shutdown()
        } catch (e: Exception) {
            e.printStackTrace()
            log += "Exception: " + e.message + "\n"
        }

        log += "总耗时: " + (System.currentTimeMillis() - startTime) + "ms \n"
        showInfo.value = log
        return retAll
    }

    private fun showToast(message: String) {
        // Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }
}