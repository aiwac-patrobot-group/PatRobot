package com.aiwac.cilentapp.patrobot.server;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.aiwac.cilentapp.patrobot.activity.videoplayer.VideoPlayActivity;
import com.aiwac.cilentapp.patrobot.bean.User;
import com.aiwac.cilentapp.patrobot.bean.aVDetail;
import com.aiwac.cilentapp.patrobot.bean.videoAbstractInfo;
import com.aiwac.cilentapp.patrobot.utils.JsonUtil;
import com.aiwac.robotapp.commonlibrary.bean.MessageEvent;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import io.vov.vitamio.Vitamio;


/**     用于WebSocket客户端通信
 * Created by luwang on 2017/10/31.
 */

public class WebSocketClientHelper extends WebSocketClient {

    private Context context;
    private User user;
    protected videoAbstractInfo videoAllInfo;
    protected videoAbstractInfo audioAllInfo;
    protected aVDetail aVDetail;



    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public videoAbstractInfo getLectureVideoAllInfo(){
        return videoAllInfo;
    }
    public videoAbstractInfo getLectureAudioAllInfo(){
        return  audioAllInfo;
    }
    public aVDetail getAudioDetail(){
        return aVDetail;
    }

    public aVDetail getVideoDetail(){
        return aVDetail;
    }












    public WebSocketClientHelper(URI serverUri, Map<String, String> httpHeaders, Context context) {
        this(serverUri, new Draft_6455(), httpHeaders, 0, context);
        //LogUtil.d( "serverUri  : " + serverUri);
    }

    public WebSocketClientHelper(URI serverUri, Draft draft, Map<String, String> httpHeaders, Context context) {
        this(serverUri, draft, httpHeaders, 0, context);
    }

    public WebSocketClientHelper(URI serverUri, Draft draft, Map<String, String> httpHeaders, int connectionTimeout, Context context) {
        super(serverUri,draft,httpHeaders,connectionTimeout);

        //获取全局唯一的context对象，否则activity不能销毁问题
        this.context = context.getApplicationContext();
    }



    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LogUtil.d( Constant.WEBSOCKET_CONNECTION_OPEN + getRemoteSocketAddress());

        //开启连接的时候检查要不要同步数据
       // checkSyncTimer();
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        LogUtil.d( Constant.WEBSOCKET_CONNECTION_CLOSE + i + s + b);
        WebSocketApplication.getWebSocketApplication().setNull();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        LogUtil.d( Constant.WEBSOCKET_CONNECTION_EXCEPTION);
    }

    @Override
    public void onMessage(final String json) {
        //处理具体逻辑
        //ping  pong
        if(json.equals("ping")){
            WebSocketApplication.getWebSocketApplication().send("pong");
        }else{
            LogUtil.printJson( Constant.WEBSOCKET_MESSAGE_FROM_SERVER ,json,"##");

            try {
                String businessType = JsonUtil.parseBusinessType(json);
                if (businessType.equals(Constant.WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE)) {
                    if(JsonUtil.parseLectureAVAbstractInfo(json).getLectureCourseAbstracts().get(0).getType().equals("video")){
                        videoAllInfo = JsonUtil.parseLectureAVAbstractInfo(json);
                    }else if(JsonUtil.parseLectureAVAbstractInfo(json).getLectureCourseAbstracts().get(0).getType().equals("audio")){
                        audioAllInfo = JsonUtil.parseLectureAVAbstractInfo(json);
                    }
                } else if (businessType.equals(Constant.WEBSOCKET_BUSSINESS_MACADDRESS_CODE)) {  //绑定机器人mac地址
                    if (JsonUtil.parseErrorCode(json).equals(Constant.RETURN_JSON_ERRORCODE_VALUE_SUCCEED)) {
                        //发送消息广播
                        EventBus.getDefault().postSticky(new MessageEvent(Constant.WEBSOCKET_BUSSINESS_MACADDRESS_SUCCEEDED, json));
                    } else {
                        //mac绑定失败
                        EventBus.getDefault().postSticky(new MessageEvent(Constant.WEBSOCKET_BUSSINESS_MACADDRESS_FAILED, json));
                    }
                }else if(businessType.equals(Constant.WEBSOCKET_SOCKET_GET_TIME_LIST)) {//获取时间段
                    LogUtil.d("获得时间段");
                    MessageEvent messageEvent = new MessageEvent(Constant.WEBSOCKET_SOCKET_GET_TIME_LIST, json);
                    EventBus.getDefault().postSticky(messageEvent);
                }
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.d( "onMessage : " + e.getMessage());
            }

        }

    }

    //判断是否重新安装，是否需要同步
    private void checkSyncTimer(){
        /*SharedPreferences pref = context.getSharedPreferences(Constant.DB_USER_TABLENAME, Context.MODE_PRIVATE);
        String timerSync = pref.getString(Constant.USER_DATA_FIELD_TIMER_SYNC, "");
        if(!StringUtil.isValidate(timerSync)) { // 新的线程中发送同步请求
            ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        //发送同步请求
                        String json = JsonUtil.timerSync2Json();
                        send(json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/
    }




}


