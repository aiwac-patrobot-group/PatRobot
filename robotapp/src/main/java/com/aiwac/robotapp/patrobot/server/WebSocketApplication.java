package com.aiwac.robotapp.patrobot.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.exception.WebSocketException;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.commonlibrary.utils.StringUtil;
import com.aiwac.robotapp.patrobot.PatRobotApplication;

import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**     用于WebSocket建立唯一的连接等操作
 * Created by luwang on 2017/10/31.
 */

public class WebSocketApplication {

    private WebSocketClientHelper webSocketHelper;
    private final static WebSocketApplication webSocketApplication;

    static {
        webSocketApplication = new WebSocketApplication();
    }

    private void init(Context context){
        try{
            SharedPreferences pref = PatRobotApplication.getContext().getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE);
            //String token = pref.getString(Constant.USER_DATA_FIELD_TOKEN, "");
            String macAddress=pref.getString(Constant.ROBOT_MAC_ADDRESS,"");
            Map<String,String> headers=new HashMap<>();
            headers.put("userNumber",macAddress);
            //headers.put("Authorization",token);
            //这里会进行和服务端的握手操作
            webSocketHelper = new WebSocketClientHelper(new URI(Constant.WEBSOCKET_PAT_URL),headers,PatRobotApplication.getContext());
            //WEBSOCKET_PAT_URL=WEBSOCKET_BASE_URL+"/websocketbusiness";

            /*URI uri = new URI(Constant.WEBSOCKET_URL+token);
            LogUtil.d("uri:"+uri);*/
            //URI uri = new URI(Constant.WEBSOCKET_URL);
            Log.d("11", "init: "+webSocketHelper);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.WEBSOCKET_URI_EXCEPTION);
            throw new WebSocketException(Constant.WEBSOCKET_URI_EXCEPTION, e);
        }
    }

    private void connection(Context context){

        if(webSocketHelper == null) {
            init(context);
        }

        if(!webSocketHelper.isOpen() && !webSocketHelper.isConnecting()) {
            try{
                webSocketHelper.connect();
                LogUtil.d( Constant.WEBSOCKET_CONNECTION_SUCCESS);
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.d( Constant.WEBSOCKET_CONNECTION_EXCEPTION);
            }
        }
    }


    private WebSocketApplication(){
        //初始化私有变量
    }

    public static WebSocketApplication getWebSocketApplication(){
        return webSocketApplication;
    }

    private Map<String, String> getDefaultMap(){
        Map<String, String> map = new HashMap<String, String>();
/*
        //获取用户电话号码
        String number = userData.getNumber();
        if(StringUtil.isValidate(number)){
            map.put(Constant.WEBSOCKET_USER_IDENTITY, number);

            //其他需要添加的字段

            return map;
        }else{
            throw new WebSocketException(Constant.WEBSOCKET_USER_IDENTITY_EXCEPTION);
        }*/
        return map;
    }


    public WebSocketClientHelper getWebSocketHelper(){
        return webSocketHelper;
    }




    public void setNull(){
        close();
        webSocketHelper = null;
    }

    public void close(){
        if(webSocketHelper !=null && !webSocketHelper.isClosed()) {
            webSocketHelper.close();
            LogUtil.d( Constant.WEBSOCKET_CONNECTION_CLOSE);
        }
        //startWebSocketConnection(HealthRobotApplication.getContext());
    }

    //在新的线程中开启一个websocket连接
    public void startWebSocketConnection(final Context context){
        LogUtil.d("startWebSocketConnection");
        if(webSocketHelper == null || (!webSocketHelper.isOpen() && !webSocketHelper.isConnecting())) {
            ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        //获得连接
                        webSocketApplication.connection(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.d(Constant.WEBSOCKET_CONNECTION_EXCEPTION);
                    }
                }
            });
        }
    }

    //发送消息
    public void send(String json){
        LogUtil.printJson("json",json,"##");
        webSocketHelper.send(json);
    }




}

