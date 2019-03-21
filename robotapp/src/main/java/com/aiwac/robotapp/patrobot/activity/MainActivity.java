package com.aiwac.robotapp.patrobot.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.utils.ActivityUtil;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.commonlibrary.utils.WifiUtil;
import com.aiwac.robotapp.patrobot.R;
import com.aiwac.robotapp.patrobot.service.WebSocketService;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MainActivity extends AppCompatActivity {
    String macAddress="";
    Button btn_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        initEvent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectWifiAndWebsocket();
    }
    private void ConnectWifiAndWebsocket() {
        boolean isNet = WifiUtil.checkNet(this); //判断是否连接网络
        if (isNet) {
                //开启服务，创建websocket连接
                Intent intent = new Intent(this, WebSocketService.class);
                intent.putExtra(Constant.SERVICE_TIMER_TYPE, Constant.SERVICE_TIMER_TYPE_WEBSOCKET);
                startService(intent);
                LogUtil.d("wifi 连接成功");
        } else {
            //没联网

            ActivityUtil.skipActivity(MainActivity.this, ConnectWifiActivity.class, true);
            finish();

        }
    }
    private void initView(){
        //设置携带mac地址的图片
        setMacImg();
        btn_setting=findViewById(R.id.btn_setting);

    }

    private void initEvent(){
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
    }

    /**
     * 设置携带mac地址的图片
     */
    private void setMacImg(){
        macAddress=getMacAddress(getApplicationContext());
        initCodeImg();
    }


    /**
     *  根据wifi信息获取本地mac
     */
    private String getMacAddress(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac =  winfo.getMacAddress();
        SharedPreferences.Editor editor = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
        editor.putString(Constant.ROBOT_MAC_ADDRESS, mac);
        editor.apply();

        return mac;
    }



    /**
     * 获得了mac地址后，初始化二维码图片
     */
    public void initCodeImg(){
        if(!macAddress.equals("")){
            ImageView mImage= (ImageView) findViewById(R.id.codeImage);
            mImage.setVisibility(View.VISIBLE);
            Bitmap bitmap = null;
            bitmap = CodeUtils.createImage(macAddress,400,400,null);
            mImage.setImageBitmap(bitmap);
            LogUtil.d("mac地址设置完成");
        }
    }


}
