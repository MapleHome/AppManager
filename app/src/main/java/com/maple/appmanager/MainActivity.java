package com.maple.appmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    TextView tvInfo;
    Button btRemove;
    Button btAdd;
    //    EditText etCMD;
    boolean canRemove = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = findViewById(R.id.tv_info);
        btRemove = findViewById(R.id.bt_remove);
        btAdd = findViewById(R.id.bt_add);
//         etCMD = findViewById(R.id.et_cmd);

        clickRefresh(null);

        Toast.makeText(this, "哈哈，我成功启动了！", Toast.LENGTH_LONG).show();
        Log.e("AutoRun","哈哈，我成功启动了！");

        updateButtonState();
    }

    private void updateButtonState() {
        btRemove.setEnabled(canRemove);
        btAdd.setEnabled(!canRemove);
    }

    public void clickRefresh(View view) {
        String info = "系统信息: \n"
                + "Model: " + android.os.Build.MODEL + ",\n"
                + "SDK: " + android.os.Build.VERSION.SDK + ",\n"
                + "版本：" + android.os.Build.VERSION.RELEASE;
        tvInfo.setText(info);
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
        tvInfo.setText(log);
        return retAll;
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

//    /**
//     * run custom cmd
//     *
//     * @param view
//     */
//    public void clickExe2(View view) {
//        String cmd = etCMD.getText().toString().trim();
//        exeCommands(Arrays.asList(cmd));
//    }

    //---------------------------------------------------------------------

//    public void clickExe1(View view) {
//        String cmd = etCMD.getText().toString().trim();
//        exeCmd(cmd);
//    }
//
//    @Deprecated
//    public void exeCmd(String cmd) {
//        String s = "";
//        try {
//            Process p = Runtime.getRuntime().exec(cmd);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            p.waitFor();
//            if (p.exitValue() != 0) {
//                //说明命令执行失败
//                //可以进入到错误处理步骤中
//            }
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//                s += line + "\n";
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            s += "InterruptedException: " + e.toString() + "\n";
//        } catch (IOException e) {
//            e.printStackTrace();
//            s += "IOException: " + e.toString() + "\n";
//        }
//        s += "time: " + System.currentTimeMillis() + "\n";
//        tvInfo.setText(s);
//    }
}
