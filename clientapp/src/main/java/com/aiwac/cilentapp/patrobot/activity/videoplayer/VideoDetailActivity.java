package com.aiwac.cilentapp.patrobot.activity.videoplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.bean.aVDetail;
import com.aiwac.cilentapp.patrobot.bean.videoInfo;
import com.aiwac.cilentapp.patrobot.server.WebSocketApplication;
import com.aiwac.cilentapp.patrobot.service.TimerService;
import com.aiwac.cilentapp.patrobot.utils.JsonUtil;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.bumptech.glide.Glide;


public class VideoDetailActivity extends AppCompatActivity {

    protected videoInfo lectureCourseNow;
    protected ImageView lectureCover;
    protected TextView videoTitle, videoDescription;
    private Button backButton, buttonplay_pause1,buttonplay_pause2,buttonplay_pause3;
    protected String link = "noLink";

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case 1:   //在这里变换按钮为暂停播放
                    buttonplay_pause2.setText("暂停播放");
                break;
                case 2:   //在这里变换按钮为暂停播放
                    buttonplay_pause2.setText("继续播放");
                    break;
                case 3:   //在这里变换按钮为机器人端播放
                    buttonplay_pause2.setText("机器人端播放");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("lecture","viodeo detail");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_detail);


        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

        setView();
        //Vitamio.isInitialized(this);

    }

    private void setView() {
        lectureCourseNow = (videoInfo) getIntent().getSerializableExtra("videoInfo");

        lectureCover = (ImageView)findViewById(R.id.lecture_cover);
        videoTitle = (TextView)findViewById(R.id.lecture_name);
        videoDescription = (TextView)findViewById(R.id.lecture_description);
        buttonplay_pause1 = (Button)findViewById(R.id.buttonPlayPause) ;
        buttonplay_pause2 = (Button)findViewById(R.id.buttonPlayPause2);
        buttonplay_pause3 = (Button)findViewById(R.id.buttonPlayPause3);

        buttonplay_pause1.setSelected(false);
        buttonplay_pause2.setSelected(false);
        buttonplay_pause2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonplay_pause2.getText().equals("机器人端播放")){
                    if (link.equals("noLink")) {
                        Toast.makeText(VideoDetailActivity.this, "抱歉，暂无相关资源", Toast.LENGTH_SHORT).show();

                    } else {
                        //测试
//                    link  = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
                        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = 1;//按钮变为暂停播放
                                handler.sendMessage(msg);
                            }
                        });
                        LogUtil.d("已发送到机器人端播放"+link);
                        //buttonplay_pause2.setText("暂停播放");
                        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    WebSocketApplication.getWebSocketApplication().send(JsonUtil.videoPlay2Json(Constant.WEBSOCKET_COMMAND_VIDEO_PLAY_CODE,link));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d("tag", "LoadVideoAsync onPostExecute setOnItemClickListener exception");
                                }
                            }
                        });
                        Toast.makeText(VideoDetailActivity.this, "已发送到机器人端播放", Toast.LENGTH_SHORT).show();
    //                    MediaPlayer mediaPlayer = new MediaPlayer();
//                        try{
//                            mediaPlayer.setDataSource(link);
//                            mediaPlayer.prepare();
//                        }catch(Exception o){
//                            o.printStackTrace();
//                        }
                    }
                }else if(buttonplay_pause2.getText().equals("暂停播放")){
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 2;//按钮变为继续播放
                            handler.sendMessage(msg);
                        }
                    });
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                WebSocketApplication.getWebSocketApplication().send(JsonUtil.videoPlay2Json(Constant.WEBSOCKET_COMMAND_VIDEO_PAUSE_CODE,"Pause"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("tag", "LoadVideoAsync onPostExecute setOnItemClickListener exception");
                            }
                        }
                    });

                }else if(buttonplay_pause2.getText().equals("继续播放")){
                    //buttonplay_pause2.setText("暂停播放");
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 1;//按钮变为暂停播放
                            handler.sendMessage(msg);
                        }
                    });
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                WebSocketApplication.getWebSocketApplication().send(JsonUtil.videoPlay2Json(Constant.WEBSOCKET_COMMAND_VIDEO_CONTINUE_CODE,"Continue"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("tag", "LoadVideoAsync onPostExecute setOnItemClickListener exception");
                            }
                        }
                    });

                }
                }
        });
        buttonplay_pause1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if ( link.equals("noLink" ) )
                    {
                        Toast.makeText(VideoDetailActivity.this, "抱歉，暂无相关资源", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent(VideoDetailActivity.this, VideoPlayActivity.class);
                        Log.d("Video",link);
                        intent.putExtra("Link",link);
                        startActivity(intent);
                    }

                    buttonplay_pause1.setSelected(false);
                }

        });
        buttonplay_pause3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 3;//按钮变为机器人端播放
                        handler.sendMessage(msg);
                    }
                });
                ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WebSocketApplication.getWebSocketApplication().send(JsonUtil.videoPlay2Json(Constant.WEBSOCKET_COMMAND_VIDEO_STOP_CODE,"Stop"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("tag", "LoadVideoAsync onPostExecute setOnItemClickListener exception");
                        }
                    }
                });
            }
        });

        backButton = (Button)findViewById(R.id.backButton) ;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Glide.with(VideoDetailActivity.this).load(lectureCourseNow.getCover()).into(lectureCover);
        videoTitle.setText(lectureCourseNow.getTitle());
        videoDescription.setText(lectureCourseNow.getDescription());
        link = lectureCourseNow.getLink();

    }

}
