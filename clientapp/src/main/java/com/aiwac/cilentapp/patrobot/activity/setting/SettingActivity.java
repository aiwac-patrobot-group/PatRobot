package com.aiwac.cilentapp.patrobot.activity.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.server.WebSocketApplication;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class SettingActivity extends AppCompatActivity {
    private final static int REQ_CODE = 1028;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }
    private void initView(){
        //跳转到扫描机器人
        findViewById(R.id.btn_setting_scan_robot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开始扫描二维码
                LogUtil.d( "onClick: 开始扫描二维码");
                startActivityForResult(new Intent(SettingActivity.this, CaptureActivity.class),REQ_CODE);
                LogUtil.d("onClick: 二维码扫描返回");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQ_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    //Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();

                    LogUtil.d( "onActivityResult: '"+result);
                    if(isMac(result)){
                        Toast.makeText(SettingActivity.this,"二维码扫描成功",Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
                        editor.putString(Constant.ROBOT_MAC_ADDRESS, result);
                        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    //String jsonStr=JsonUtil.configRobotAndPhoneToJson();
                                    //WebSocketApplication.getWebSocketApplication().getWebSocketHelper().send(jsonStr);
                                } catch (Exception e){
                                    e.printStackTrace();
                                    LogUtil.d( "发送mac地址失败");
                                }
                            }
                        });

                    }else{
                        Toast.makeText(SettingActivity.this,"请扫描机器人屏幕上的二维码",Toast.LENGTH_SHORT).show();

                        //startActivityForResult(new Intent(FindRobotActivity.this, CaptureActivity.class),REQ_CODE);
                    }

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(SettingActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 判断是不是mac地址
     * @param target
     * @return
     */
    private boolean isMac(String target){
        if(target.replace(":","-").matches("([A-Fa-f0-9]{2}-){5}[A-Fa-f0-9]{2}")){
            LogUtil.d( "onActivityResult: 是mac");
            return true;
        }else{
            LogUtil.d( "onActivityResult: 不是mac");
            return false;
        }
    }
}
