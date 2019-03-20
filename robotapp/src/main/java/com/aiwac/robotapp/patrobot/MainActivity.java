package com.aiwac.robotapp.patrobot;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MainActivity extends AppCompatActivity {
    String macAddress="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        initEvent();

    }

    private void initView(){
        //设置携带mac地址的图片
        setMacImg();


    }

    private void initEvent(){

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
