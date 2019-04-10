package com.aiwac.robotapp.patrobot.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwac.robotapp.commonlibrary.bean.MessageEvent;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;
import com.aiwac.robotapp.commonlibrary.utils.ActivityUtil;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.commonlibrary.utils.WifiUtil;
import com.aiwac.robotapp.patrobot.R;
import com.aiwac.robotapp.patrobot.server.WebSocketApplication;
import com.aiwac.robotapp.patrobot.service.WebSocketService;
import com.aiwac.robotapp.patrobot.utils.JsonUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import io.vov.vitamio.Vitamio;

import static com.aiwac.robotapp.patrobot.PatRobotApplication.getContext;

public class MainActivity extends AppCompatActivity {
    private final static  int PERMISSION_REQUEST_COARSE_LOCATION=1;
    private static final int REQUEST_FINE_LOCATION = 0;

    String macAddress="";
    Button btnSetting;
    Button btnVideoChat;
    TextView tvMac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();
        initView();
        initEvent();

        Vitamio.isInitialized(getContext());

        //注册消息
        EventBus.getDefault().register(this);
        requestFeedNaviTime();
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
        btnSetting =findViewById(R.id.btn_setting);
        tvMac=findViewById(R.id.tv_mac);
        tvMac.setText(macAddress);
        btnVideoChat=findViewById(R.id.btn_video_chat);
    }

    private void initEvent(){
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
        btnVideoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,VideoChatViewActivity.class));
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if(messageEvent.getTo().equals(Constant.WEBSOCKET_COMMAND_START_VIDEO)){
            //准备打开视频通话
            Intent intent = new Intent(MainActivity.this,VideoChatViewActivity.class);
            startActivity(intent);
        }

    }
    /**
     * 设置携带mac地址的图片
     */
    private void setMacImg(){
        //设置mac
        macAddress=getMacAddress(getApplicationContext());

        SharedPreferences.Editor editor = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
        editor.putString(Constant.ROBOT_MAC_ADDRESS, macAddress);
        editor.apply();
        //显示二维码图片
        initCodeImg();
    }

    /**
     *  根据wifi信息获取本地mac
     */
    private String getMacAddress(Context context){
        String strMac = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            strMac = getMacDefault(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //LogUtil.d("获取MAC信息");
            //strMac = getMacAddress(context);
            strMac = getMacDefault(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            strMac = getMachineHardwareAddress();
            return strMac;

        }
        return strMac;
    }

    /**
     * Android 6.0 之前（不包括6.0）获取mac地址
     * 必须的权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     */
    public static String getMacDefault(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        WifiManager wifi = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        /*if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }*/
        return mac;
    }
    /**
     * Android 6.0-Android 7.0 获取mac地址
     */
    public static String getMacAddress() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            while (null != str) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();//去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }

        return macSerial;
    }


    /**
     * android 7.0及以上 （2）扫描各个网络接口获取mac地址
     * 获取设备HardwareAddress地址
     */
    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }



    /**
     * 获得了mac地址后，初始化二维码图片,需要类里的一个属性 macAddress
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

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.VIBRATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN

                /*,
                Manifest.permission.READ_EXTERNAL_STORAGE*/
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm :permissions){
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()){
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void requestFeedNaviTime(){
        try{
            WebSocketApplication.getWebSocketApplication().send(JsonUtil.time2Json(Constant.WEBSOCKET_SOCKET_AUTOTYPE_AUTO_FEED));
            LogUtil.d("toushixinxihuoqu+++++++++++++++++++++++");
        }catch (Exception e){
            e.printStackTrace();
            Log.d("tag", "FeedList exception");
        }
        try{
            WebSocketApplication.getWebSocketApplication().send(JsonUtil.time2Json(Constant.WEBSOCKET_SOCKET_AUTOTYPE_AUTO_CONTROL));
        }catch (Exception e){
            e.printStackTrace();
            Log.d("tag", "NavigateList exception");
        }
    }
}
