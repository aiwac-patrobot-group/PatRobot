package com.aiwac.robotapp.patrobot.server;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.aiwac.robotapp.commonlibrary.bean.MessageEvent;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.patrobot.R;
import com.aiwac.robotapp.patrobot.activity.videoplayer.AudioPlayActivity;
import com.aiwac.robotapp.patrobot.activity.videoplayer.VideoPlayActivity;
import com.aiwac.robotapp.patrobot.bean.FeedTime;
import com.aiwac.robotapp.patrobot.bean.MessageTransform;
import com.aiwac.robotapp.patrobot.bean.aVDetail;
import com.aiwac.robotapp.patrobot.utils.JsonUtil;


import org.greenrobot.eventbus.EventBus;
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
    protected MessageTransform messageTransform;
    protected FeedTime feedTime;


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

            }else if((businessType.equals(Constant.WEBSOCKET_MESSAGE_FEEDTRANSFORM_CODE))){
                feedTime = JsonUtil.parseFeedNavigateTransform(json);
                String timeFeed = feedTime.getTimePoints();
                //得到喂食时间，后续硬件进行处理
            }else if(businessType.equals(Constant.WEBSOCKET_MESSAGE_NAVIGATETRANSFORM_CODE)) {
                feedTime = JsonUtil.parseFeedNavigateTransform(json);
                String timeFeed = feedTime.getTimePoints();
                //得到巡航时间，后续硬件进行处理
            }else if((businessType.equals(Constant.WEBSOCKET_MESSAGE_TRANSFORM_CODE))){//指令转发
                String dataJsonStr=JsonUtil.parseMessageTransData(json);
                String commantType=JsonUtil.parseCommantType(dataJsonStr);
                if(commantType.equals(Constant.WEBSOCKET_COMMAND_VIDEO_CODE)){//指令是1001，开启视频通话
                    String uuid=JsonUtil.parseUUID(dataJsonStr);
                    //通知MainActivity跳转到语音通话
                    MessageEvent messageEvent = new MessageEvent(Constant.WEBSOCKET_COMMAND_GET_UUID, uuid);
                    EventBus.getDefault().post(messageEvent);
                }if(commantType.equals(Constant.WEBSOCKET_COMMAND_END_VIDEO_CODE)){//指令是1002，结束视频通话
                    //通知MainActivity跳转到语音通话
                    MessageEvent messageEvent = new MessageEvent(Constant.WEBSOCKET_COMMAND_END_VIDEO);
                    EventBus.getDefault().post(messageEvent);
                }

                /*messageTransform = JsonUtil.parseMessageTransform(json);
                String messageType[] = messageTransform.getData().split("：");
<<<<<<< HEAD
                dealMessageTransform(messageType);
            }else if((businessType.equals(Constant.WEBSOCKET_MESSAGE_FEEDTRANSFORM_CODE))){
                feedTime = JsonUtil.parseFeedNavigateTransform(json);
                String timeFeed = feedTime.getTimePoints();
                //得到喂食时间，后续硬件进行处理
            }else if(businessType.equals(Constant.WEBSOCKET_MESSAGE_NAVIGATETRANSFORM_CODE)){
                feedTime = JsonUtil.parseFeedNavigateTransform(json);
                String timeFeed = feedTime.getTimePoints();
                //得到巡航时间，后续硬件进行处理

=======
                dealMessageTransform(messageType);*/

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
    private void dealMessageTransform(String messageType[]){
        if (messageType[0].equals("PlayVideo")){
            Intent intent = new Intent(context,VideoPlayActivity.class);
            intent.putExtra("Link",messageType[1]);
            context.startActivity(intent);
        }else if (messageType[0].equals("PlayAudio")){
            Intent intent = new Intent(context,AudioPlayActivity.class);
            intent.putExtra("Link",messageType[1]);
            context.startActivity(intent);
        }else if (messageType[0].equals("PauseVideo")){
            if(VideoPlayActivity.mVideoView.isPlaying()){
                VideoPlayActivity.mVideoView.pause();
                VideoPlayActivity.mIvPlay.setImageResource(R.drawable.video_play);
                VideoPlayActivity.mHandler.removeMessages(VideoPlayActivity.UPDATE_PALY_TIME);
                VideoPlayActivity.mHandler.removeMessages(VideoPlayActivity.HIDE_CONTROL_BAR);
                VideoPlayActivity.showControlBar();
            }
        }else if(messageType[0].equals("ContinueVideo")){
            VideoPlayActivity.mVideoView.start();
            VideoPlayActivity.mIvPlay.setImageResource(R.drawable.video_pause);
            VideoPlayActivity.mHandler.sendEmptyMessage(VideoPlayActivity.UPDATE_PALY_TIME);
            VideoPlayActivity.mHandler.sendEmptyMessageDelayed(VideoPlayActivity.HIDE_CONTROL_BAR, VideoPlayActivity.HIDE_TIME);
        }else if (messageType[0].equals("PauseAudio")){
            if(AudioPlayActivity.mVideoView.isPlaying()){
                AudioPlayActivity.mVideoView.pause();
                AudioPlayActivity.mIvPlay.setImageResource(R.drawable.video_play);
                AudioPlayActivity.mHandler.removeMessages(AudioPlayActivity.UPDATE_PALY_TIME);
                AudioPlayActivity.mHandler.removeMessages(AudioPlayActivity.HIDE_CONTROL_BAR);
                AudioPlayActivity.showControlBar();
            }
        }else if(messageType[0].equals("ContinueAudio")){
            AudioPlayActivity.mVideoView.start();
            AudioPlayActivity.mIvPlay.setImageResource(R.drawable.video_pause);
            AudioPlayActivity.mHandler.sendEmptyMessage(AudioPlayActivity.UPDATE_PALY_TIME);
            AudioPlayActivity.mHandler.sendEmptyMessageDelayed(AudioPlayActivity.HIDE_CONTROL_BAR, AudioPlayActivity.HIDE_TIME);
        }

    }




}


