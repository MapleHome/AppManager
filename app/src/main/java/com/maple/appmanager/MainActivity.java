package com.maple.appmanager;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.maple.appmanager.databinding.ActivityMainBinding;
import com.maple.appmanager.utils.PackageUtils;
import com.maple.appmanager.utils.SPUtils;
import com.maple.appmanager.utils.VirtualTerminal;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public String AAA = "isStart";
    ActivityMainBinding binding;
    boolean canRemove = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        clickRefresh(null);
        updateButtonState();

        // start other app
        boolean isStart = new SPUtils().getBoolean(AAA, false);
        binding.cbStartApp.setChecked(isStart);
        if (isStart) {
            doStartAppWithPackageName("com.example.androidx.viewpager2");
        }
        binding.cbStartApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new SPUtils().put(AAA, isChecked);
            }
        });
    }

    private void doStartAppWithPackageName(String packageName) {
        String className = PackageUtils.getStartActivityName(this, packageName);
        if (!TextUtils.isEmpty(className)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName(packageName, className));
            startActivity(intent);
//            finish();
        }
    }

    private void updateButtonState() {
        binding.btRemove.setEnabled(canRemove);
        binding.btAdd.setEnabled(!canRemove);
    }

    public void clickRefresh(View view) {
        String info = "系统信息: \n"
                + "Model: " + android.os.Build.MODEL + ",\n"
                + "SDK: " + android.os.Build.VERSION.SDK + ",\n"
                + "版本：" + android.os.Build.VERSION.RELEASE;
        binding.tvInfo.setText(info);
    }

    String[] appNames = {"LeSo", "LeSports", "LeStore", "LeVideo", "StvGallery", "StvWeather"};

    public void clickAddApp(View view) {
        addSingleApp(appNames);
    }

    public void clickRemoveApp(View view) {
        removeSingleApp(appNames);
    }

    public void addSingleApp(String... appNames) {
        List<String> cmdList = new ArrayList<>();
        cmdList.add("su");//root权限
        cmdList.add("mount -o rw,remount /system");//获取system权限
        for (String appName : appNames) {
            cmdList.add("mv /system/app/" + appName + ".apk_copy /system/app/" + appName + ".apk");
        }
        boolean retall = exeCommands(cmdList);
        if (retall) {
            showToast("恢复所有成功！");
        } else {
            showToast("恢复所有失败！");
        }
        canRemove = true;
        updateButtonState();
    }

    public void removeSingleApp(String... appNames) {
        List<String> cmdList = new ArrayList<>();
        cmdList.add("su");//root权限
        cmdList.add("mount -o rw,remount /system");//获取system权限
        for (String appName : appNames) {
            cmdList.add("mv /system/app/" + appName + ".apk /system/app/" + appName + ".apk_copy");
        }
        boolean retall = exeCommands(cmdList);
        if (retall) {
            showToast("删除所有成功！");
        } else {
            showToast("删除所有失败！");
        }
        canRemove = false;
        updateButtonState();
    }

    private boolean exeCommands(List<String> commands) {
        String log = "";
        boolean retAll = true;
        long startTime = System.currentTimeMillis();
        try {
            VirtualTerminal vt = new VirtualTerminal("su");
            for (String cmd : commands) {
                VirtualTerminal.VTCommandResult r = vt.runCommand(cmd);
                boolean removeSingle = r.success();
//                if (cmd.startsWith("mv")) {
//                    String appName = cmd.substring(cmd.indexOf("app") + 4, cmd.indexOf(".apk"));
//                    if (removeSingle) {
//                        showToast(ss + appName + "成功！");
//                    } else {
//                        showToast(ss + appName + "失败！");
//                    }
//                }
                log += cmd + "   执行成功? " + removeSingle + "! \n";
                retAll = (retAll && removeSingle);
            }
            vt.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            log += "Exception: " + e.getMessage() + "\n";
        }
        log += "总耗时: " + (System.currentTimeMillis() - startTime) + "ms \n";
        binding.tvInfo.setText(log);
        return retAll;
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
