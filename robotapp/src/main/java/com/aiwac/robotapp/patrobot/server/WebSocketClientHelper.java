package com.aiwac.robotapp.patrobot.server;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.patrobot.activity.videoplayer.AudioPlayActivity;
import com.aiwac.robotapp.patrobot.activity.videoplayer.VideoPlayActivity;
import com.aiwac.robotapp.patrobot.bean.aVDetail;
import com.aiwac.robotapp.patrobot.utils.JsonUtil;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

import io.vov.vitamio.Vitamio;


/**     用于WebSocket客户端通信
 * Created by luwang on 2017/10/31.
 */

public class WebSocketClientHelper extends WebSocketClient {

    private Context context;
    protected aVDetail aVDetail;
    protected String link = "noLink";


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
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

       LogUtil.printJson( Constant.WEBSOCKET_MESSAGE_FROM_SERVER ,json,"##");

        try{
            String businessType = JsonUtil.parseBusinessType(json);
            /*if(businessType.equals(Constant.WEBSOCKET_VOICECHAT_BUSSINESSTYPE_CODE)){  //在线问诊房间号
                EventBus.getDefault().postSticky(new MessageEvent(json));//eventbus黏性事件
            }else if(businessType.equals(Constant.WEBSOCKET_REGISTERRESULT_BUSSINESSTYPE_CODE)) { //语音挂号结果
                MessageEvent messageEvent = new MessageEvent("RegisterResult", json);
                EventBus.getDefault().postSticky(messageEvent);
            }*/
            if ((businessType.equals(Constant.WEBSOCKET_VIDEO_DETAIL_TYPE_CODE))) //视频详细信息到达
            {
                aVDetail = JsonUtil.parseAVDetailInfo(json);
                if (aVDetail != null) {
                    link = aVDetail.getLink();

                    Log.d("lecture","link: "+link);
                }

                if ( !link.equals("noLink" ) )
                {
                    Vitamio.isInitialized(context);
                    Toast.makeText(context, "播放视频", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(context, VideoPlayActivity.class);
                    //link  = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
                    //测试
                    Log.d("lecture",link);
                    intent.putExtra("Link",link);
                    context.startActivity(intent);

                }

            }else if ((businessType.equals(Constant.WEBSOCKET_AUDIO_DETAIL_TYPE_CODE))) //音频详细信息到达
            {
                aVDetail = JsonUtil.parseAVDetailInfo(json);
                if (aVDetail != null) {
                    link = aVDetail.getLink();

                    Log.d("lecture","link: "+link);
                }

                if ( !link.equals("noLink" ) )
                {
                    Vitamio.isInitialized(context);
                    Toast.makeText(context, "播放音频", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(context, AudioPlayActivity.class);
                    //link  = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
                    //测试
                    Log.d("lecture",link);
                    intent.putExtra("Link",link);
                    context.startActivity(intent);

                }

            }else if((businessType.equals(Constant.WEBSOCKET_COMMAND_CODE))){

            }


        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( "onMessage : " + e.getMessage());

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


