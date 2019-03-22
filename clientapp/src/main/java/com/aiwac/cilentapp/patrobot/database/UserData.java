package com.aiwac.cilentapp.patrobot.database;

import android.util.Log;

import com.aiwac.robotapp.commonlibrary.common.Constant;

import java.util.HashMap;
import java.util.Map;


/**     保存用户相关的数据  单例模式
 * Created by luwang on 2017/10/27.
 */

public class UserData {

    private final static String LOG_TAG = "UserData";
    private final static UserData userData = new UserData();
    private boolean isNetwork = false; //用户是否联网标志
    private  Map<String, String> map;


    //初始化操作
    private void init() {
        map = new HashMap<String, String>();
        map.put(Constant.USER_DATA_FIELD_PASSWORD, "");
        map.put(Constant.USER_DATA_FIELD_NUMBER, "");
        map.put(Constant.WEBSOCKET_MESSAGE_CLIENTID,"");
        isNetwork = true;
    }

    private UserData(){
        init();
    }

    public static UserData getUserData(){
        return userData;
    }

    public String getPassword(){
        return map.get(Constant.USER_DATA_FIELD_PASSWORD);
    }
    public void setPassword(String password){
        map.put(Constant.USER_DATA_FIELD_PASSWORD, password);
        Log.d(LOG_TAG, "UserData setPassword : " + password);
    }
    public String getClientID(){
        return map.get(Constant.WEBSOCKET_MESSAGE_CLIENTID);
    }
    public void setClientID(String clientid){
        map.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, clientid);
        Log.d(LOG_TAG, "UserData setPassword : " + clientid);
    }

    public String getNumber(){
        return map.get(Constant.USER_DATA_FIELD_NUMBER);
    }
    public void setNumber(String number){
        map.put(Constant.USER_DATA_FIELD_NUMBER, number);
        Log.d(LOG_TAG, "UserData setNumber : " + number);
    }

    public boolean isNetwork() {
        return isNetwork;
    }
    public void setNetwork(boolean network) {
        isNetwork = network;
    }


}
