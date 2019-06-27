package com.maple.appmanager.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maple.appmanager.receiver.MyReceiver
import com.maple.appmanager.utils.SPUtils
import com.maple.appmanager.utils.VirtualTerminal
import java.util.*

/**
 *
 * @author maple
 * @time 2019-06-27
 */
class HomeViewModel : ViewModel() {
    val showInfo = MutableLiveData<String>()
    val canRemove = MutableLiveData<Boolean>().apply {
        this.value = true
    }


    fun clickRefresh() {
        val infoStr = ("系统信息: \n"
                + "Model: " + android.os.Build.MODEL + ",\n"
                + "SDK: " + android.os.Build.VERSION.SDK + ",\n"
                + "版本：" + android.os.Build.VERSION.RELEASE + "\n")
        val msg = SPUtils().getString(MyReceiver.BOOT_KEY, "kai")
        showInfo.value = infoStr + msg
    }


    fun addSingleApp(appNames: ArrayList<String>) {
        val cmdList = ArrayList<String>()
        cmdList.add("su")//root权限
        cmdList.add("mount -o rw,remount /system")//获取system权限
        for (appName in appNames) {
            cmdList.add("mv /system/app/$appName.apk_copy /system/app/$appName.apk")
        }
        val retall = exeCommands(cmdList)
        if (retall) {
            showToast("恢复所有成功！")
        } else {
            showToast("恢复所有失败！")
        }
        canRemove.value = true
    }

    fun removeSingleApp(appNames: ArrayList<String>) {
        val cmdList = ArrayList<String>()
        cmdList.add("su")//root权限
        cmdList.add("mount -o rw,remount /system")//获取system权限
        for (appName in appNames) {
            cmdList.add("mv /system/app/$appName.apk /system/app/$appName.apk_copy")
        }
        val retall = exeCommands(cmdList)
        if (retall) {
            showToast("删除所有成功！")
        } else {
            showToast("删除所有失败！")
        }
        canRemove.value = false
    }


    private fun exeCommands(commands: List<String>): Boolean {
        var log = ""
        var retAll = true
        val startTime = System.currentTimeMillis()
        try {
            val vt = VirtualTerminal("su")
            for (cmd in commands) {
                val r = vt.runCommand(cmd)
                val removeSingle = r.success()
                //                if (cmd.startsWith("mv")) {
                //                    String appName = cmd.substring(cmd.indexOf("app") + 4, cmd.indexOf(".apk"));
                //                    if (removeSingle) {
                //                        showToast(ss + appName + "成功！");
                //                    } else {
                //                        showToast(ss + appName + "失败！");
                //                    }
                //                }
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